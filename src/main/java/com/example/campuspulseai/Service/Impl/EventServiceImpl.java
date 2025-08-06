package com.example.campuspulseai.Service.Impl;

import com.example.campuspulseai.Service.IEventService;
import com.example.campuspulseai.domain.DTO.Request.CreateEventRequest;
import com.example.campuspulseai.domain.DTO.Response.CreateEventResponse;
import com.example.campuspulseai.domain.DTO.Response.GetEventResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements IEventService {
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
    public List<GetEventResponse> getEventsByClubId(Long clubId) {
        return List.of();
    }

    @Override
    public List<GetEventResponse> getMyEvents() {
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
}
