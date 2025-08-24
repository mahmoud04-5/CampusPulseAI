package com.example.campuspulseai.common.util;

import com.example.campuspulseai.southbound.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AuthUtils implements IAuthUtils {

    private final SecureRandom random = new SecureRandom();

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("Authentication is required");
        }

        return (User) authentication.getPrincipal();
    }

    @Override
    public String generateOtp(int length) {
        int randomNumber = random.nextInt(1000000); // Generate a random number between 0 and 999999
        return String.format("%06d", randomNumber);
    }


}
