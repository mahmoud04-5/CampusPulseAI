package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.common.exception.ResourceNotFoundException;
import com.example.campuspulseai.common.util.IAuthUtils;
import com.example.campuspulseai.domain.dto.request.CreateEventRequest;
import com.example.campuspulseai.domain.dto.request.EditEventRequest;
import com.example.campuspulseai.domain.dto.response.CreateEventResponse;
import com.example.campuspulseai.domain.dto.response.GetEventResponse;
import com.example.campuspulseai.service.IEventService;
import com.example.campuspulseai.southbound.entity.*;
import com.example.campuspulseai.southbound.mapper.EventMapper;
import com.example.campuspulseai.southbound.repository.IClubRepository;
import com.example.campuspulseai.southbound.repository.IEventRepository;
import com.example.campuspulseai.southbound.repository.IUserEventRepository;
import com.example.campuspulseai.southbound.repository.IUserRepository;
import com.example.campuspulseai.southbound.specification.IEventSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
    private final EventMapper eventMapper;
    private final IClubRepository clubRepository;
    private final IEventSpecifications eventSpecifications;

    @Override
    public CreateEventResponse createEvent(CreateEventRequest createEventRequest) throws Exception {
        User user = authUtils.getAuthenticatedUser();
        Event event = eventMapper.mapToClub(createEventRequest);
        Club club = getCluByOwnerId(user.getId());
        event.setClub(club);
        Event createdEvent = eventRepository.save(event);
        return eventMapper.mapToCreateEventResponse(createdEvent);
    }


    @Override
    public CreateEventResponse updateEvent(Long id, EditEventRequest editEventRequest) throws Exception {
        Event event = getEventFromDBById(id);
        Club club = clubRepository.getById(event.getClub().getId());
        User user = authUtils.getAuthenticatedUser();
        validateUserClubownership(user, club);
        validateEventTime(event);
        eventMapper.mapToEventForEdit(editEventRequest, event);
        Event updatedEvent = eventRepository.save(event);
        return eventMapper.mapToCreateEventResponse(updatedEvent);
    }

    @Override
    public CreateEventResponse getEventById(Long id) {
        return null;
    }

    @Override
    public void deleteEventById(Long id) throws Exception {
        Event event = getEventFromDBById(id);
        Club club = clubRepository.getById(event.getClub().getId());
        User user = authUtils.getAuthenticatedUser();
        validateUserClubownership(user, club);
        validateEventTime(event);
        event.setIsActive(false);
        eventRepository.save(event);
    }

    @Override
    public List<GetEventResponse> suggestEventsToAttend() {
        return List.of();
    }

    @Override
    public List<GetEventResponse> suggestEventsToCreate() {
        return List.of();
    }


    //Retrieves a list of events that the current user has RSVP’d for
    @Override
    public List<GetEventResponse> getEventsAttending() {
        User currentUser = getCurrentUser();
        return List.of();
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
    public List<GetEventResponse> getAllEventsForCurrentUser() {
        User currentUser = getCurrentUser();
        List<UserEvent> userEvents = userEventRepository.findByUserId(currentUser.getId());
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
                .toList();
    }

    @Override
    public void attendEvent(Long eventId) {
        //User currentUser = getCurrentUser();
        User dummyUser = getDummyUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        UserEvent userEvent = new UserEvent();
        userEvent.setUserId(dummyUser.getId());
        userEvent.setEventId(eventId);
        //userEvent.setUser(currentUser);
        userEvent.setUser(dummyUser);
        userEvent.setEvent(event);
        userEvent.setRsvpDateTime(LocalDateTime.now());

        userEventRepository.save(userEvent);
    }

    @Override
    public void unattendEvent(Long eventId) {
        //User currentUser = getCurrentUser();
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
        LocalDateTime now = LocalDateTime.now(); // 06:34 PM EEST, August 12, 2025
        LocalDateTime filterDate = (startDate != null) ? startDate : now;

        List<Event> events = (category != null && !category.isEmpty()) ?
                eventRepository.findByTimeDateAfterAndCategory(startDate, category) :
                eventRepository.findByTimeDateAfterAndCategory(filterDate, category);

        return events.stream()
                .filter(e -> e.getStartDate().isAfter(filterDate) && e.getStartDate() != null)
                .map(eventMapper::mapToEventResponseDetails)
                .collect(Collectors.toList());
    }

    @Override
    public GetEventResponse getEventDetails(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return eventMapper.mapToEventResponseDetails(event);

    }


    @Override
    public User getCurrentUser() {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        } else {
            throw new RuntimeException("User not authenticated");
        }
    }

    @Override
    public User getDummyUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Dummy user not found"));
    }

    private Club getCluByOwnerId(Long ownerId) {
        return clubRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not own a club"));
    }

    private Event getEventFromDBById(Long id) {
        return eventRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    private void validateUserClubownership(User user, Club club) {
        if (!Objects.equals(user.getId(), club.getOwner().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this event");
        }
    }

    private void validateEventTime(Event event) {
        if (event.getTimeDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot edit an event that has already started");
        }
    }

}
