package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.common.exception.ResourceNotFoundException;
import com.example.campuspulseai.common.util.IAuthUtils;
import com.example.campuspulseai.domain.dto.request.CreateEventRequest;
import com.example.campuspulseai.domain.dto.request.EditEventRequest;
import com.example.campuspulseai.domain.dto.response.CreateEventResponse;
import com.example.campuspulseai.domain.dto.response.GetEventResponse;
import com.example.campuspulseai.domain.dto.response.GetEventSuggestionResponse;
import com.example.campuspulseai.domain.dto.response.GetUserResponse;
import com.example.campuspulseai.service.IEventRecommendationService;
import com.example.campuspulseai.service.IEventService;
import com.example.campuspulseai.southbound.entity.*;
import com.example.campuspulseai.southbound.mapper.EventMapper;
import com.example.campuspulseai.southbound.mapper.UserEventMapper;
import com.example.campuspulseai.southbound.mapper.UserMapper;
import com.example.campuspulseai.southbound.repository.*;
import com.example.campuspulseai.southbound.specification.impl.EventSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {

    private final IAuthUtils authUtils;
    private final IEventRepository eventRepository;
    private final IUserEventRepository userEventRepository;
    private final IClubRepository clubRepository;
    private final EventMapper eventMapper;
    private final EventSpecifications eventSpecifications;
    private final UserMapper userMapper;
    private final UserEventMapper userEventMapper;
    private final IEventRecommendationService eventRecommendationService;
    private final ISuggestedUserEventsRepository suggestedUserEventsRepository;
    private final ISuggestedOrganizerEventsRepository suggestedOrganizerEventsRepository;
    private static final String EVENT_NOT_FOUND = "Event not found with id: ";



    @SneakyThrows
    @Override
    public CreateEventResponse createEvent(CreateEventRequest createEventRequest) {
        User user = authUtils.getAuthenticatedUser();
        Event event = eventMapper.mapToEvent(createEventRequest);

        Club club = getClubByOwnerId(user.getId());
        event.setClub(club);

        Event createdEvent = eventRepository.save(event);
        return eventMapper.mapToCreateEventResponse(createdEvent);
    }


    private Club getClubByOwnerId(Long ownerId) {
        return clubRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Club not found for user with id: " + ownerId));
    }


    @SneakyThrows
    @Override
    public CreateEventResponse updateEvent(Long id, EditEventRequest editEventRequest) {
        Event event = getEventFromDBById(id);
        Club club = event.getClub();
        User user = authUtils.getAuthenticatedUser();
        validateUserClubOwnership(user, club);
        validateEventTime(event);
        eventMapper.mapToEventForEdit(editEventRequest, event);
        Event updatedEvent = eventRepository.save(event);
        return eventMapper.mapToCreateEventResponse(updatedEvent);
    }

    @Override
    public CreateEventResponse getEventById(Long id) {
        Event event = getEventFromDBById(id);
        return eventMapper.mapToCreateEventResponse(event);
    }

    @SneakyThrows
    @Override
    public void deleteEventById(Long id) {
        Event event = getEventFromDBById(id);
        Club club = event.getClub();
        User user = authUtils.getAuthenticatedUser();
        validateUserClubOwnership(user, club);
        validateEventTime(event);
        event.setIsActive(false);
        eventRepository.save(event);
    }

    @Override
    public List<GetEventResponse> getAllEventsWithFilters(Long clubId, LocalDateTime eventDateTime, Integer page, Integer size) {
        Specification<Event> spec = eventSpecifications.isActive()
                .and(eventSpecifications.hasClubId(clubId))
                .and(eventSpecifications.hasEventDate(eventDateTime));

        Pageable pageable = PageRequest.of(page, size);

        List<Event> events = eventRepository.findAll(spec, pageable).getContent();

        return eventMapper.mapToEventResponseDetailsList(events);
    }


    @Override
    @Transactional
    public List<GetEventResponse> suggestEventsToAttend(int limit) throws Exception {
        User user = authUtils.getAuthenticatedUser();
        List<Event> suggestedEvents = getSuggestedUserEventsByUserId(user);

        if (!suggestedEvents.isEmpty()) {
            return getEventResponsesFromSuggestedEvents(user, suggestedEvents, limit);
        }

        List<Long> eventIds = eventRecommendationService.getRecommendedEventIds(user.getId());
        if (eventIds != null && !eventIds.isEmpty()) {
            List<Event> savedEvents = getAllEventsByIds(eventIds);

            saveAllRecommendedEvents(savedEvents, user);
            return getEventResponsesFromSuggestedEvents(user, savedEvents, limit);
        }
        return List.of();
    }

    @Override
    @Transactional
    public List<GetEventSuggestionResponse> suggestEventsToCreate() {
        List<SuggestedOrganizerEvent> suggestedEvents = suggestedOrganizerEventsRepository.findAll();
        if (suggestedEvents.isEmpty()) {
            List<SuggestedOrganizerEvent> newSuggestions = eventRecommendationService.getSuggestedOrganizerEvents();
            suggestedOrganizerEventsRepository.saveAll(newSuggestions);
            return eventMapper.mapToGetEventSuggestionResponse(newSuggestions);
        }
        return eventMapper.mapToGetEventSuggestionResponse(suggestedEvents);
    }

    @Override
    public List<GetEventResponse> getEventsAttending() {
        return List.of();
    }

    @SneakyThrows
    @Override
    public List<GetEventResponse> getAllEventsForCurrentUser() {
        User user = authUtils.getAuthenticatedUser();
        List<UserEvent> userEvents = userEventRepository.findByUserId(user.getId());

        return userEvents.stream()
                .map(UserEvent::getEvent)
                .filter(Objects::nonNull)
                .map(event -> eventMapper.mapToEventResponseDetails(event, true))
                .toList();
    }


    @SneakyThrows
    @Override
    public void attendEvent(Long eventId) {
        User user = authUtils.getAuthenticatedUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(EVENT_NOT_FOUND + eventId));
        if (event.getTotalAttendees() >= event.getCapacity()) {
            throw new RuntimeException("Event is full. Cannot RSVP.");
        }
        boolean alreadyAttending = userEventRepository.existsByUserIdAndEventId(user.getId(), eventId);
        if (alreadyAttending) {
            throw new RuntimeException("User already RSVP’d to this event.");
        }

        UserEvent userEvent = userEventMapper.toUserEvent(user, event);
        event.setTotalAttendees(event.getTotalAttendees() + 1);
        userEventRepository.save(userEvent);
    }

    @SneakyThrows
    @Override
    public void unattendEvent(Long eventId) {
        User user = authUtils.getAuthenticatedUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(EVENT_NOT_FOUND + eventId));
        UserEventId id = new UserEventId(user.getId(), eventId);
        userEventRepository.deleteById(id);
        event.setTotalAttendees(event.getTotalAttendees() - 1);
    }

    @Override
    public List<GetUserResponse> getAttendeesByEventId(Long eventId) {
        List<UserEvent> userEvents = userEventRepository.findByEventId(eventId);

        return userEvents.stream()
                .map(UserEvent::getUser)
                .filter(Objects::nonNull)
                .map(userMapper::mapToUserResponse)
                .filter(Objects::nonNull)
                .toList();
    }


    @SneakyThrows
    @Override
    public List<GetEventResponse> getUpcomingEvents(LocalDateTime startDate, String category) {
        LocalDateTime filterDate = (startDate != null)
                ? startDate
                : LocalDateTime.now(ZoneId.of("Europe/Helsinki"));

        List<Event> events = (category != null && !category.isEmpty())
                ? eventRepository.findByStartTimeAfterAndCategory(filterDate, category)
                : eventRepository.findByStartTimeAfter(filterDate);

        User user = authUtils.getAuthenticatedUser();

        return events.stream()
                .map(event -> {
                    boolean isUserAttending = userEventRepository.existsById(new UserEventId(user.getId(), event.getId()));
                    return eventMapper.mapToEventResponseDetails(event, isUserAttending);
                })
                .toList();
    }



    @SneakyThrows
    @Override
    public GetEventResponse getEventDetails(Long id) {
        Event event = getEventFromDBById(id);
        User user = authUtils.getAuthenticatedUser();
        boolean isUserAttending = userEventRepository.existsById(new UserEventId(user.getId(), id));
        return eventMapper.mapToEventResponseDetails(event, isUserAttending);
    }




    // Helper
    private Event getEventFromDBById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EVENT_NOT_FOUND + id));
    }

    private void validateUserClubOwnership(User user, Club club) {
        if (!Objects.equals(club.getOwner().getId(), user.getId())) {
            throw new AccessDeniedException("User does not own this club");
        }
    }

    private void validateEventTime(Event event) {
        if (event.getStartDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Event start time cannot be in the past"
            );
        }
    }

    private List<Event> getSuggestedUserEventsByUserId(User user) {
        return suggestedUserEventsRepository.findAllByUserId(user.getId())
                .stream()
                .map(SuggestedUserEvent::getEvent)
                .toList();
    }

    private List<GetEventResponse> getEventResponsesFromSuggestedEvents(User user, List<Event> suggestedEvents, int limit) {
        return suggestedEvents.stream()
                .map(event -> {
                    boolean isUserAttending = userEventRepository.existsById(new UserEventId(user.getId(), event.getId()));
                    return eventMapper.mapToEventResponseDetails(event, isUserAttending);
                })
                .limit(limit)
                .toList();
    }

    private List<Event> getAllEventsByIds(List<Long> eventIds) {
        return eventRepository.findAllById(eventIds).stream()
                .toList();
    }

    private void saveAllRecommendedEvents(List<Event> savedEvents, User user) {
        suggestedUserEventsRepository.saveAll(savedEvents.stream()
                .map(event -> eventMapper.mapToSuggestedUserEvent(event, user))
                .toList()
        );
    }

}
