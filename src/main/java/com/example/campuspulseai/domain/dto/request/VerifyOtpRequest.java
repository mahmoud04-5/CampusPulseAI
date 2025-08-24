package com.example.campuspulseai.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyOtpRequest {
    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, max = 6, message = "OTP code must be 6 characters")
    @NotBlank(message = "OTP code is required")
    private String otpCode;
}
