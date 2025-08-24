package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.southbound.entity.Event;
import com.example.campuspulseai.southbound.entity.User;
import com.example.campuspulseai.southbound.entity.UserEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEventMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "event", source = "event")
    @Mapping(target = "rsvpDateTime", expression = "java(java.time.LocalDateTime.now())")
    UserEvent toUserEvent(User user, Event event);
}
