package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.domain.dto.response.GetUserResponse;
import com.example.campuspulseai.southbound.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    GetUserResponse mapToUserResponse(User user);
}
