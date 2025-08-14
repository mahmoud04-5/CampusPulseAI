package com.example.campuspulseai.domain.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LoginResponse {
    private String email;
    private String token;
    private String type = "Bearer";
}
