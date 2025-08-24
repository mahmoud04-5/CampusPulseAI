package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.domain.dto.SuggestedEventParts;
import com.example.campuspulseai.domain.dto.request.CreateEventRequest;
import com.example.campuspulseai.domain.dto.request.EditEventRequest;
import com.example.campuspulseai.domain.dto.response.CreateEventResponse;
import com.example.campuspulseai.domain.dto.response.GetEventResponse;
import com.example.campuspulseai.domain.dto.response.GetEventSuggestionResponse;
import com.example.campuspulseai.southbound.entity.Event;
import com.example.campuspulseai.southbound.entity.SuggestedOrganizerEvent;
import com.example.campuspulseai.southbound.entity.SuggestedUserEvent;
import com.example.campuspulseai.southbound.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "timeDate", source = "startTime") // assuming DTO has startTime
    Event mapToEvent(CreateEventRequest request);

    // Entity -> CreateEventResponse
    @Mapping(target = "eventId", source = "id")
    CreateEventResponse mapToCreateEventResponse(Event event);

    // Entity -> GetEventResponse
    @Mapping(target = "startTime", source = "event.timeDate")
    @Mapping(target = "userAttending", source = "isAttending")
    @Mapping(target = "totalAttendees", source = "event.totalAttendees")
    GetEventResponse mapToEventResponseDetails(Event event, boolean isAttending);

    // List<Entity> -> List<GetEventResponse>
    List<GetEventResponse> mapToEventResponseDetailsList(List<Event> events);

    // Edit Event -> Update Entity (partial update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "timeDate", source = "startTime")
    void mapToEventForEdit(EditEventRequest editEventRequest, @MappingTarget Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    SuggestedUserEvent mapToSuggestedUserEvent(Event event, User user);

    GetEventSuggestionResponse mapToGetEventSuggestionResponse(SuggestedOrganizerEvent suggestedOrganizerEvent);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "title", expression = "java(events.getValues()[0].trim())")
    @Mapping(target = "description", expression = "java(events.getValues()[1].trim())")
    @Mapping(target = "category", expression = "java(events.getValues()[2].trim())")
    SuggestedOrganizerEvent mapToSuggestedOrganizerEvent(SuggestedEventParts events);

    @Named("mapTitle")
    default String mapTitle(String[] events) {
        return events.length > 0 ? events[0].trim() : null;
    }

    @Named("mapDescription")
    default String mapDescription(String[] events) {
        return events.length > 1 ? events[1].trim() : null;
    }

    @Named("mapCategory")
    default String mapCategory(String[] events) {
        return events.length > 2 ? events[2].trim() : null;
    }
}

    List<GetEventResponse> toGetEventResponseList(List<Event> byClubId);
}