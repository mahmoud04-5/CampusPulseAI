package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.common.util.IAuthUtils;
import com.example.campuspulseai.domain.dto.request.CreateEventRequest;
import com.example.campuspulseai.domain.dto.request.EditEventRequest;
import com.example.campuspulseai.domain.dto.response.CreateEventResponse;
import com.example.campuspulseai.domain.dto.response.GetEventResponse;
import com.example.campuspulseai.service.IEventService;
import com.example.campuspulseai.southbound.entity.*;
import com.example.campuspulseai.southbound.repository.*;

import com.example.campuspulseai.southbound.mapper.EventMapper;
import com.example.campuspulseai.southbound.specification.impl.EventSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {

    private final IAuthUtils authUtils;
    private final IEventRepository eventRepository;
    private final IUserRepository userRepository;
    private final IUserEventRepository userEventRepository;
    private final IClubRepository clubRepository;
    private final EventMapper eventMapper;
    private final EventSpecifications eventSpecifications;

    @Override
    public CreateEventResponse createEvent(CreateEventRequest createEventRequest) throws AccessDeniedException {
        User user = authUtils.getAuthenticatedUser();
        Event event = eventMapper.mapToEvent(createEventRequest);
        Club club = clubRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new RuntimeException("Club not found for user"));
        event.setClub(club);
        Event createdEvent = eventRepository.save(event);
        return eventMapper.mapToCreateEventResponse(createdEvent);
    }

    @Override
    public CreateEventResponse updateEvent(Long id, EditEventRequest editEventRequest) throws AccessDeniedException {
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

    @Override
    public void deleteEventById(Long id) throws AccessDeniedException {
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
        return eventRepository.findAll(spec, pageable).stream()
                .map(eventMapper::mapToEventResponseDetails)
                .toList();
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

    @Override
    public List<GetEventResponse> getAllEventsForCurrentUser() {
        User dummyUser = getDummyUser();
        List<UserEvent> userEvents = userEventRepository.findByUserId(dummyUser.getId());
        return userEvents.stream()
                .map(userEvent -> {
                    Event event = eventRepository.findById(userEvent.getEventId())
                            .orElseThrow(() -> new RuntimeException("Event not found with id: " + userEvent.getEventId()));
                    return new GetEventResponse(
                            event.getId(),
                            event.getTitle(),
                            event.getDescription(),
                            event.getStartDate()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public void attendEvent(Long eventId) {
        User dummyUser = getDummyUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        UserEvent userEvent = new UserEvent();
        userEvent.setUserId(dummyUser.getId());
        userEvent.setEventId(eventId);
        userEvent.setUser(dummyUser);
        userEvent.setEvent(event);
        userEvent.setRsvpDateTime(LocalDateTime.now());
        userEventRepository.save(userEvent);
    }

    @Override
    public void unattendEvent(Long eventId) {
        User dummyUser = getDummyUser();
        UserEventId id = new UserEventId(dummyUser.getId(), eventId);
        userEventRepository.deleteById(id);
    }

    @Override
    public List<GetEventResponse> getAttendeesByEventId(Long eventId) {
        return List.of();
    }

    @Override
    public List<GetEventResponse> getUpcomingEvents(LocalDateTime startDate, String category) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Helsinki"));
        LocalDateTime filterDate = (startDate != null) ? startDate : now.toLocalDateTime();

        List<Event> events = (category != null && !category.isEmpty())
                ? eventRepository.findByTimeDateAfterAndCategory(filterDate, category)
                : eventRepository.findByTimeDateAfter(filterDate);

        return events.stream()
                .filter(e -> e.getStartDate() != null && e.getStartDate().isAfter(filterDate))
                .map(e -> new GetEventResponse(
                        e.getId(),
                        e.getTitle(),
                        e.getClub(),
                        e.getDescription(),
                        e.getStartDate()
                ))
                .toList();
    }


    @Override
    public GetEventResponse getEventDetails(Long id) {
        Event event = getEventFromDBById(id);
        return new GetEventResponse(
                event.getId(), event.getTitle(), event.getClub(), event.getDescription(), event.getStartDate()
        );
    }

    @Override
    public User getDummyUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Dummy user not found"));
    }

    // Helper
    private Event getEventFromDBById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    private void validateUserClubOwnership(User user, Club club) {
        if (!Objects.equals(club.getOwner().getId(), user.getId())) {
            throw new RuntimeException("User does not own this club");
        }
    }

    private void validateEventTime(Event event) {
        if (event.getStartDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Event start time cannot be in the past");
        }
    }
}
