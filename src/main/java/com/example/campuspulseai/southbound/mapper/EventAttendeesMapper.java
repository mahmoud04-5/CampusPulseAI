package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.southbound.entity.Event;
import com.example.campuspulseai.southbound.entity.EventAttendees;
import com.example.campuspulseai.southbound.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventAttendeesMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "eventId", source = "event.id")
    EventAttendees toEventAttendees(User user, Event event);
}
