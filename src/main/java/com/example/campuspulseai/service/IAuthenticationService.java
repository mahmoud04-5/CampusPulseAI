package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.dto.Request.AuthenticationRequest;
import com.example.campuspulseai.domain.dto.Request.RegisterRequest;
import com.example.campuspulseai.domain.dto.Response.LoginResponse;

public interface IAuthenticationService {

    void register(RegisterRequest input) throws Exception;

    LoginResponse login(AuthenticationRequest request);
}
