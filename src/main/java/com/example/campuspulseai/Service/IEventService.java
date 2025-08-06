package com.example.campuspulseai.Service;

import com.example.campuspulseai.domain.DTO.Request.CreateEventRequest;
import com.example.campuspulseai.domain.DTO.Response.CreateEventResponse;
import com.example.campuspulseai.domain.DTO.Response.GetEventResponse;

import java.util.List;

public interface IEventService {
    /**
     * Creates a new event
     *
     * @param createEventRequest The event details
     * @return The created event response
     */
    CreateEventResponse createEvent(CreateEventRequest createEventRequest);

    /**
     * Updates an existing event
     *
     * @param createEventRequest The updated event details
     * @return The updated event response
     */
    CreateEventResponse updateEvent(CreateEventRequest createEventRequest);

    /**
     * Retrieves an event by its ID
     *
     * @param id The event ID
     * @return The event response
     */
    CreateEventResponse getEventById(Long id);

    /**
     * Deletes an event by its ID
     *
     * @param id The event ID
     */
    void deleteEventById(Long id);

    /**
     * Retrieves all events
     *
     * @return List of event responses
     */
    List<GetEventResponse> getAllEvents();

    /**
     * Suggests events for users to attend based on their preferences
     *
     * @return List of suggested event responses
     */
    List<GetEventResponse> suggestEventsToAttend();

    /**
     * Suggests events for organizers to create based on student interests
     *
     * @return List of suggested event responses
     */
    List<GetEventResponse> suggestEventsToCreate();

    /**
     * Retrieves all events for a specific club
     *
     * @param clubId The club ID
     * @return List of event responses
     */
    List<GetEventResponse> getEventsByClubId(Long clubId);

    /**
     * Retrieves all events created by the authenticated organizer
     *
     * @return List of event responses
     */
    List<GetEventResponse> getMyEvents();

    /**
     * Retrieves all events the authenticated user is attending
     *
     * @return List of event responses
     */
    List<GetEventResponse> getEventsAttending();

    /**
     * Registers the authenticated user to attend an event
     *
     * @param eventId The event ID
     */
    void attendEvent(Long eventId);

    /**
     * Removes the authenticated user's attendance from an event
     *
     * @param eventId The event ID
     */
    void unattendEvent(Long eventId);

    /**
     * Retrieves all attendees for a specific event
     *
     * @param eventId The event ID
     * @return List of event responses containing attendee information
     */
    List<GetEventResponse> getAttendeesByEventId(Long eventId);
}