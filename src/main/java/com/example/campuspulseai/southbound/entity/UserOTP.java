package com.example.campuspulseai.southbound.entity;

import jakarta.persistence.*;
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

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String otpCode;

    private Boolean isVerified;

    @CreationTimestamp
    private Timestamp createdAt;
}
