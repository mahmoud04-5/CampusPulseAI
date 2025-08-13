package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.domain.DTO.Request.CreateEventRequest;
import com.example.campuspulseai.domain.DTO.Response.CreateEventResponse;
import com.example.campuspulseai.domain.DTO.Response.GetEventResponse;
import com.example.campuspulseai.service.IEventService;
import com.example.campuspulseai.southBound.entity.Event;
import com.example.campuspulseai.southBound.repository.IEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements IEventService {

    private final IEventRepository eventRepository;
    @Override
    public CreateEventResponse createEvent(CreateEventRequest createEventRequest) {
        return null;
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
    public List<GetEventResponse> getAllEvents() {
        return List.of();
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
    public void attendEvent(Long eventId) {

    }

    @Override
    public void unattendEvent(Long eventId) {

    }

    @Override
    public List<GetEventResponse> getAttendeesByEventId(Long eventId) {
        return List.of();
    }

    @Override
    public List<GetEventResponse> getUpcomingEvents(ZonedDateTime dateTime, String label) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Helsinki")); // 06:34 PM EEST, August 12, 2025
        ZonedDateTime filterDate = (dateTime != null) ? dateTime : now;

        List<Event> events;
        if (label != null && !label.isEmpty()) {
            events = eventRepository.findByTimeDateAfterAndLabel(dateTime,label);
        } else {
            events = eventRepository.findByTimeDateAfter(filterDate);
        }

        return events.stream()
                .filter(e -> e.getTimeDate().isAfter(filterDate))
                .map(e -> new GetEventResponse(
                        e.getId(), e.getTitle(), e.getClub(), e.getDescription(), e.getTimeDate(), e.getLabel()
                ))
                .collect(Collectors.toList());    }

    @Override
    public GetEventResponse getEventDetails(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        return new GetEventResponse(
                event.getId(), event.getTitle(), event.getClub(), event.getDescription(), event.getTimeDate(), event.getLabel()
        );

    }
}
