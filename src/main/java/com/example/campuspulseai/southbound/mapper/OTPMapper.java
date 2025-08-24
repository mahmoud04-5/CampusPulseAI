package com.example.campuspulseai.southbound.mapper;


import com.example.campuspulseai.southbound.entity.UserOTP;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;

@Mapper(componentModel = "spring")
    public interface OTPMapper {

        @Mapping(source = "email", target = "email")
        @Mapping(source = "otp", target = "otp")
        @Mapping(source = "expiryTime", target = "expiryDate")
        @Mapping(target = "isVerified", constant = "false")
        @Mapping(target = "id", ignore = true)
        UserOTP toEntity(String email, String otp, Timestamp expiryTime);
}

