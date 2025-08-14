package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.domain.dto.Request.CreateEventRequest;
import com.example.campuspulseai.domain.dto.Request.EditEventRequest;
import com.example.campuspulseai.domain.dto.Response.CreateEventResponse;
import com.example.campuspulseai.domain.dto.Response.GetEventResponse;
import com.example.campuspulseai.southbound.entity.Event;
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
}
