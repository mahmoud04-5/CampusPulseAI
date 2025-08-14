package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.DTO.Request.CreateEventRequest;
import com.example.campuspulseai.domain.DTO.Response.CreateEventResponse;
import com.example.campuspulseai.domain.DTO.Response.GetEventResponse;
import com.example.campuspulseai.southBound.entity.Event;
import com.example.campuspulseai.southBound.entity.User;

import java.time.ZonedDateTime;
import java.util.List;

public interface IEventService {

    CreateEventResponse createEvent(CreateEventRequest createEventRequest);

    CreateEventResponse updateEvent(CreateEventRequest createEventRequest);

    CreateEventResponse getEventById(Long id);

    void deleteEventById(Long id);

    List<GetEventResponse> getAllEventsForCurrentUser();


    List<GetEventResponse> suggestEventsToAttend();

    List<GetEventResponse> suggestEventsToCreate();

    List<GetEventResponse> getEventsAttending();

    void attendEvent(Long eventId);


    void unattendEvent(Long eventId);

    List<GetEventResponse> getAttendeesByEventId(Long eventId);

    List<GetEventResponse> getUpcomingEvents(ZonedDateTime startDate, String category);

    GetEventResponse getEventDetails(Long id);

    User getCurrentUser();

    User getDummyUser();
}