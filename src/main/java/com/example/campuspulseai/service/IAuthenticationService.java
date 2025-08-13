package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.DTO.Request.AuthenticationRequest;
import com.example.campuspulseai.domain.DTO.Request.RegisterRequest;
import com.example.campuspulseai.domain.DTO.Response.LoginResponse;

public interface IAuthenticationService {

    void register(RegisterRequest input) throws Exception;

    LoginResponse login(AuthenticationRequest request);
}
