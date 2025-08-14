package com.example.campuspulseai.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateToken(UserDetails userDetails);

    String generateToken(String username);
}
