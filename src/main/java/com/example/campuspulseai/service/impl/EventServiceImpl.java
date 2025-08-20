package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.common.exception.ResourceNotFoundException;
import com.example.campuspulseai.common.util.IAuthUtils;
import com.example.campuspulseai.domain.dto.request.CreateEventRequest;
import com.example.campuspulseai.domain.dto.request.EditEventRequest;
import com.example.campuspulseai.domain.dto.response.CreateEventResponse;
import com.example.campuspulseai.domain.dto.response.GetEventResponse;
import com.example.campuspulseai.domain.dto.response.GetUserResponse;
import com.example.campuspulseai.service.IEventService;
import com.example.campuspulseai.southbound.entity.*;
import com.example.campuspulseai.southbound.mapper.UserMapper;
import com.example.campuspulseai.southbound.repository.*;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;




import com.example.campuspulseai.southbound.mapper.EventMapper;
import com.example.campuspulseai.southbound.specification.impl.EventSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        Club club = clubRepository.getById(event.getClub().getId());
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
        Club club = clubRepository.getById(event.getClub().getId());
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
    public List<GetEventResponse> suggestEventsToAttend() {
        return List.of();
    }

    @Override
    public List<GetEventResponse> suggestEventsToCreate() {
        return List.of();
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
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        UserEvent userEvent = new UserEvent();
        userEvent.setUserId(user.getId());
        userEvent.setEventId(eventId);
        userEvent.setUser(user);
        userEvent.setEvent(event);
        userEvent.setRsvpDateTime(LocalDateTime.now());
        event.setTotalAttendees(event.getTotalAttendees() + 1);
        userEventRepository.save(userEvent);
    }

    @SneakyThrows
    @Override
    public void unattendEvent(Long eventId) {
        User user = authUtils.getAuthenticatedUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
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
                ? eventRepository.findByTimeDateAfterAndCategory(filterDate, category)
                : eventRepository.findByTimeDateAfter(filterDate);

        User user = authUtils.getAuthenticatedUser();

        return events.stream()
                .map(event -> {
                    boolean isUserAttending = userEventRepository.existsById(new UserEventId(user.getId(), event.getId()));
                    return eventMapper.mapToEventResponseDetails(event, isUserAttending);
                })
                .collect(Collectors.toList());
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
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
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
}
