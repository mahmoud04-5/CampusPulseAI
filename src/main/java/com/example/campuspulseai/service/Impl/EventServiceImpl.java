package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.common.Util.IAuthUtils;
import com.example.campuspulseai.domain.dto.Request.CreateEventRequest;
import com.example.campuspulseai.domain.dto.Response.CreateEventResponse;
import com.example.campuspulseai.domain.dto.Response.GetEventResponse;
import com.example.campuspulseai.service.IEventService;
import com.example.campuspulseai.southBound.entity.*;
import com.example.campuspulseai.southBound.mapper.EventMapper;
import com.example.campuspulseai.southBound.repository.IClubRepository;
import com.example.campuspulseai.southBound.repository.IEventRepository;
import com.example.campuspulseai.southBound.repository.IUserEventRepository;
import com.example.campuspulseai.southBound.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
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
    public CreateEventResponse updateEvent(CreateEventRequest createEventRequest) {
        return null;
    }

    @Override
    public CreateEventResponse getEventById(Long id) {
        return null;
    }

    @Override
    public void deleteEventById(Long id) {

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
                .collect(Collectors.toList());
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
        userEvent.setRsvpDateTime(ZonedDateTime.now(ZoneId.of("Europe/Helsinki")));

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
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
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

}
