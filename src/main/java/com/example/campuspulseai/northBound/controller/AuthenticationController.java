package com.example.campuspulseai.northBound.controller;

import com.example.campuspulseai.domain.DTO.Request.AuthenticationRequest;
import com.example.campuspulseai.domain.DTO.Request.RegisterRequest;
import com.example.campuspulseai.domain.DTO.Response.AuthenticationResponse;
import com.example.campuspulseai.service.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(AuthenticationRequest authRequest) {
        return null; //TODO
    }

}
