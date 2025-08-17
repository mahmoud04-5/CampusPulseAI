package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.dto.request.CreateEventRequest;
import com.example.campuspulseai.domain.dto.request.EditEventRequest;
import com.example.campuspulseai.domain.dto.response.CreateEventResponse;
import com.example.campuspulseai.domain.dto.response.GetEventResponse;
import com.example.campuspulseai.southbound.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface IEventService {

    CreateEventResponse createEvent(CreateEventRequest createEventRequest) throws Exception;

    CreateEventResponse updateEvent(Long id, EditEventRequest editEventRequest) throws Exception;

    CreateEventResponse getEventById(Long id);

    void deleteEventById(Long id) throws Exception;

    List<GetEventResponse> getAllEventsForCurrentUser();

    List<GetEventResponse> getAllEventsWithFilters(Long clubId, LocalDateTime eventDateTime, Integer page, Integer size);

    List<GetEventResponse> suggestEventsToAttend(int limit) throws Exception;

    List<GetEventResponse> suggestEventsToCreate();

    List<GetEventResponse> getEventsAttending();

    void attendEvent(Long eventId);


    void unattendEvent(Long eventId);

    List<GetEventResponse> getAttendeesByEventId(Long eventId);

    List<GetEventResponse> getUpcomingEvents(LocalDateTime startDate, String category);

    GetEventResponse getEventDetails(Long id);

    User getCurrentUser();

    User getDummyUser();
}