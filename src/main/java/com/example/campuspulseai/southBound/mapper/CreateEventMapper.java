package com.example.campuspulseai.southBound.mapper;

import com.example.campuspulseai.domain.DTO.Request.CreateClubRequest;
import com.example.campuspulseai.domain.DTO.Request.CreateEventRequest;
import com.example.campuspulseai.southBound.entity.Club;
import com.example.campuspulseai.southBound.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CreateEventMapper {

    CreateEventMapper INSTANCE = Mappers.getMapper(CreateEventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "startDate", expression = "java(java.sql.Timestamp.valueOf(request.getStartTime()))")
    Event mapToClub(CreateEventRequest request);
}
