package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.dto.request.AuthenticationRequest;
import com.example.campuspulseai.domain.dto.request.RegisterRequest;
import com.example.campuspulseai.domain.dto.response.LoginResponse;

public interface IAuthenticationService {

    void register(RegisterRequest input) throws Exception;

    LoginResponse login(AuthenticationRequest request);
}
