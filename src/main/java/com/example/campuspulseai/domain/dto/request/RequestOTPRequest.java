package com.example.campuspulseai.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RequestOTPRequest {
    @Email
    @NotBlank(message = "Email is required")
    private String email;
}
