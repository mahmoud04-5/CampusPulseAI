package com.example.campuspulseai.southBound.mapper;

import com.example.campuspulseai.domain.DTO.Request.RegisterRequest;
import com.example.campuspulseai.southBound.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = {AuthMapper.class})
public interface AuthMapper {


    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);


    @Mapping(target = "group", expression = "java(new com.example.campuspulseai.southBound.entity.Group(1L, \"GROUP_STUDENTS\", null))")
    @Mapping(target = "isActive", constant = "true")
    User mapToUser(RegisterRequest registerRequest);
}
