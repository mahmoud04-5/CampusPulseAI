package com.example.campuspulseai.southBound.mapper;

import com.example.campuspulseai.domain.DTO.Request.RegisterRequest;
import com.example.campuspulseai.southBound.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "group", expression = "java(new com.example.campuspulseai.southBound.entity.Group(1L, \"GROUP_STUDENTS\", null))")
    User mapToUser(RegisterRequest registerRequest);
}

