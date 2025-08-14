package com.example.campuspulseai.northBound.controller;

import com.example.campuspulseai.domain.dto.Request.AuthenticationRequest;
import com.example.campuspulseai.domain.dto.Request.RegisterRequest;
import com.example.campuspulseai.domain.dto.Response.LoginResponse;
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
    public LoginResponse login(AuthenticationRequest authRequest) {
        return authenticationService.login(authRequest);
    }

}
