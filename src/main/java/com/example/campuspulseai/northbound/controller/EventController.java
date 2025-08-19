package com.example.campuspulseai.northbound.controller;

import com.example.campuspulseai.domain.dto.request.CreateEventRequest;
import com.example.campuspulseai.domain.dto.request.EditEventRequest;
import com.example.campuspulseai.domain.dto.response.CreateEventResponse;
import com.example.campuspulseai.domain.dto.response.GetEventResponse;
import com.example.campuspulseai.domain.dto.response.GetUserResponse;
import com.example.campuspulseai.service.IEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Event endpoints", description = "Endpoints for event operations")
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final IEventService eventService;

    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Create a new event", description = "Creates a new event with the provided details.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateEventResponse createEvent(@Valid @RequestBody CreateEventRequest createEventRequest) throws Exception {
        return eventService.createEvent(createEventRequest);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Update an existing event", description = "Updates an existing event with the provided details.")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreateEventResponse updateEvent(@PathVariable Long id, @Valid @RequestBody EditEventRequest editEventRequest) throws Exception {
        return eventService.updateEvent(id, editEventRequest);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get all events",
            description = "Retrieves a list of all events, offering options for pagination and filtering. Optional ClubId parameter to filter events for a certain club.")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> getAllEvents(
            @RequestParam(required = false) Long clubId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventDateTime,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return eventService.getAllEventsWithFilters(clubId, eventDateTime, page, size);
    }

    @Operation(summary = "Get event details", description = "Retrieves detailed info for a specific event by its ID.")
    @GetMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetEventResponse> getEventDetails(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventDetails(id));
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Delete event by ID", description = "Deletes an event by its ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventById(@PathVariable Long id) throws Exception {
        eventService.deleteEventById(id);
    }


    @Operation(summary = "Suggests events to attend", description = "Searches for events based on user preferences.")
    @GetMapping("/attend-suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> suggestEventsToAttend() {
        // Logic to suggest events based on interests
        return eventService.suggestEventsToAttend();
    }

    @Operation(summary = "Suggest events to create", description = "Suggests events that the Organizer might want to create based on students interests.")
    @GetMapping("/create-suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> suggestEventsToCreate() {
        // Logic to suggest events based on user interests
        return eventService.suggestEventsToCreate();
    }


    @Operation(summary = "Get events the user is attending", description = "Retrieves a list of events that the currently authenticated user is attending.")
    @GetMapping("/my-events")
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventResponse> getEventsAttending() {
        return eventService.getAllEventsForCurrentUser();
    }

    @Operation(summary = " Attend an event", description = "Allows the currently authenticated user to RSVP for an event by its ID.")
    @PostMapping("/{eventId}/rsvp")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> attendEvent(@PathVariable Long eventId) {
        eventService.attendEvent(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Unattend an event", description = "Allows the currently authenticated user to cancel their RSVP for an event by its ID.")
    @DeleteMapping("/{eventId}/rsvp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unattendEvent(@PathVariable Long eventId) {
        eventService.unattendEvent(eventId);
    }

    @Operation(summary = "Get attendees of an event", description = "Retrieves a list of users attending a specific event by its ID.")
    @GetMapping("/{eventId}/attendees")
    @ResponseStatus(HttpStatus.OK)
    public List<GetUserResponse> getAttendeesByEventId(@PathVariable Long eventId) {
        return eventService.getAttendeesByEventId(eventId);
    }


    @Operation(summary = "Get upcoming events", description = "Retrieves a list of upcoming events based on the provided date and label.")
    @GetMapping("/upcoming")
    public ResponseEntity<List<GetEventResponse>> getEvents(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(eventService.getUpcomingEvents(dateTime, category));
    }

}


