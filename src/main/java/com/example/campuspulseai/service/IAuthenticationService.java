package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.dto.request.AuthenticationRequest;
import com.example.campuspulseai.domain.dto.request.RegisterRequest;
import com.example.campuspulseai.domain.dto.response.LoginResponse;

public interface IAuthenticationService {

    void register(RegisterRequest input) throws Exception;

    LoginResponse login(AuthenticationRequest request);

    void requestOtp(String email) throws Exception;

    void verifyOtp(String email, String otp) throws Exception;

    void resetPassword(String email, String newPassword) throws Exception;
}
