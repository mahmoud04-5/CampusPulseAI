package com.example.campuspulseai.NorthBound.Controller;

import com.example.campuspulseai.Service.IEventService;
import com.example.campuspulseai.domain.DTO.Request.CreateEventRequest;
import com.example.campuspulseai.domain.DTO.Response.CreateEventResponse;
import com.example.campuspulseai.domain.DTO.Response.GetEventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Event endpoints", description = "Endpoints for event operations")
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final IEventService eventService;

    @Operation(summary = "Create a new event", description = "Creates a new event with the provided details.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateEventResponse createEvent(CreateEventRequest createEventRequest) {
        return eventService.createEvent(createEventRequest);
    }

    @Operation(summary = "Update an existing event", description = "Updates an existing event with the provided details.")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CreateEventResponse updateEvent(CreateEventRequest createEventRequest) {
        return eventService.updateEvent(createEventRequest);
    }

    @Operation(summary = "Get event by ID", description = "Retrieves an event by its ID.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreateEventResponse getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @Operation(summary = "Delete event by ID", description = "Deletes an event by its ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventById(@PathVariable Long id) {
        // Logic to delete the event by ID
        eventService.deleteEventById(id);
    }

    @Operation(summary = "Get all events", description = "Retrieves a list of all events, offering options for pagination and filtering.")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> getAllEvents() {
        // Logic to get all events
        //TODO: Implement pagination!, add option to filter by date, location, etc.
        return eventService.getAllEvents();
    }

    @Operation(summary = "Suggests events to attend", description = "Searches for events based on user preferences.")
    @GetMapping("/suggest-Attend")
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> suggestEventsToAttend() {
        // Logic to suggest events based on interests
        return eventService.suggestEventsToAttend();
    }

    @Operation(summary = "Suggest events to create", description = "Suggests events that the Organizer might want to create based on students interests.")
    @GetMapping("/suggest-Create")
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> suggestEventsToCreate() {
        // Logic to suggest events based on user interests
        return eventService.suggestEventsToCreate();
    }

    @Operation(summary = "Get events by club ID", description = "Retrieves a list of events associated with a specific club.")
    @GetMapping("/club/{clubId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> getEventsByClubId(@PathVariable Long clubId) {
        // Logic to get events by club ID
        return eventService.getEventsByClubId(clubId);
    }

    @Operation(summary = "Get events created by the authenticated organizer", description = "Retrieves a list of events created by the currently authenticated club organizer.")
    @GetMapping("/created")
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> getMyEvents() {
        // Logic to get events created by the authenticated user
        return eventService.getMyEvents();
    }

    @Operation(summary = "Get events the user is attending", description = "Retrieves a list of events that the currently authenticated user is attending.")
    @GetMapping("/attending")
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> getEventsAttending() {
        // Logic to get events the user is attending
        return eventService.getEventsAttending();
    }

    @Operation(summary = " Attend an event", description = "Allows the currently authenticated user to RSVP for an event by its ID.")
    @PostMapping("/RSVP/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public void attendEvent(@PathVariable Long eventId) {
        // Logic to mark the user as attending the event
        eventService.attendEvent(eventId);
    }

    @Operation(summary = "Unattend an event", description = "Allows the currently authenticated user to cancel their RSVP for an event by its ID.")
    @DeleteMapping("/RSVP/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unattendEvent(@PathVariable Long eventId) {
        eventService.unattendEvent(eventId);
    }

    @Operation(summary = "Get attendees of an event", description = "Retrieves a list of users attending a specific event by its ID.")
    @GetMapping("/attendees/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> getAttendeesByEventId(@PathVariable Long eventId) {
        // Logic to get attendees of an event by event ID
        return eventService.getAttendeesByEventId(eventId);
    }
}


