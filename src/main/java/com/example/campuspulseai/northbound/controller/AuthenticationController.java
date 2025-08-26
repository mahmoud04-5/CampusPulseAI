package com.example.campuspulseai.northbound.controller;

import com.example.campuspulseai.domain.dto.request.AuthenticationRequest;
import com.example.campuspulseai.domain.dto.request.RegisterRequest;
import com.example.campuspulseai.domain.dto.request.RequestOTPRequest;
import com.example.campuspulseai.domain.dto.request.VerifyOtpRequest;
import com.example.campuspulseai.domain.dto.response.LoginResponse;
import com.example.campuspulseai.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Authentication endpoints", description = "Endpoints for user operations related to register and login")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    @Operation(summary = "Register a new user", description = "Creates a new user in the database.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        authenticationService.register(registerRequest);
    }

    @Operation(summary = "Login a user", description = "Authenticates a user and returns an authentication response.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody AuthenticationRequest authRequest) {
        return authenticationService.login(authRequest);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/forgot-password/request-otp")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Request OTP", description = "Sends a One-Time Password (OTP) to the user's email to reset the password.")
    public void requestOTP(@Valid @RequestBody RequestOTPRequest requestOTPRequest) throws Exception {
        authenticationService.requestOtp(requestOTPRequest.getEmail());
    }

    @PostMapping("/forgot-password/verify-otp")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*")
    @Operation(summary = "Validate OTP", description = "Validates the provided OTP for the user's email.")
    public void validateOTP(@Valid @RequestBody VerifyOtpRequest request) throws Exception {
        authenticationService.verifyOtp(request);
    }

    @PostMapping("/forgot-password/reset")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reset Password", description = "Resets the user's password using the provided OTP.")
    public void resetPassword(@Valid @RequestBody AuthenticationRequest authRequest) throws Exception {
        authenticationService.resetPassword(authRequest.getEmail(), authRequest.getPassword());
    }
}
