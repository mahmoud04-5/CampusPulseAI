package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.domain.dto.Request.RegisterRequest;
import com.example.campuspulseai.domain.dto.Response.LoginResponse;
import com.example.campuspulseai.southbound.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = {AuthMapper.class})
public interface AuthMapper {


    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);


    @Mapping(target = "group", expression = "java(new com.example.campuspulseai.southbound.entity.Group(1L, \"GROUP_STUDENTS\", null))")
    @Mapping(target = "isActive", constant = "true")
    User mapToUser(RegisterRequest registerRequest);


    @Mapping(target = "type", constant = "Bearer")
    LoginResponse mapToLoginResponse(User user, String token);
}
