package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.domain.dto.request.CreateEventRequest;
import com.example.campuspulseai.domain.dto.request.EditEventRequest;
import com.example.campuspulseai.domain.dto.response.CreateEventResponse;
import com.example.campuspulseai.domain.dto.response.GetEventResponse;
import com.example.campuspulseai.southbound.entity.Event;
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

    List<GetEventResponse> toGetEventResponseList(List<Event> byClubId);
}