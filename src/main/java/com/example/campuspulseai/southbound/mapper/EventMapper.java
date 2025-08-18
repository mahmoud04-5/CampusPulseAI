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

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "timeDate", source = "request.startTime")
    Event mapToClub(CreateEventRequest request);

    @Mapping(target = "eventId", source = "event.id")
    CreateEventResponse mapToCreateEventResponse(Event event);

    @Mapping(target = "startTime", source = "event.timeDate")
    GetEventResponse mapToEventResponseDetails(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "timeDate", source = "startTime")
        // map only if not null
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
