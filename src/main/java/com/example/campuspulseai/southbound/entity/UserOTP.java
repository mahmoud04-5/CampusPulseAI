package com.example.campuspulseai.southbound.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Table(name = "user_otp")
@Entity
public class UserOTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "OTP code cannot be blank")
    @Column(name = "otp_code", nullable = false)
    private String otp;

    private Boolean isVerified;

    @CreationTimestamp
    private Timestamp createdAt;

    private Timestamp expiryDate;
}
