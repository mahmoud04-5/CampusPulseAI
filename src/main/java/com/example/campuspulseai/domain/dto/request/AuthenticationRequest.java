package com.example.campuspulseai.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationRequest {

    private String email;
    private String password;
}
