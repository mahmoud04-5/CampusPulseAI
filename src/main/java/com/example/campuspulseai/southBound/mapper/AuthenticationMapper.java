package com.example.campuspulseai.southBound.mapper;

import com.example.campuspulseai.domain.DTO.Request.RegisterRequest;
import com.example.campuspulseai.southBound.entity.Group;
import com.example.campuspulseai.southBound.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationMapper {
    private final PasswordEncoder passwordEncoder;

    public User mapToUser(RegisterRequest registerRequest) {
        return new User(
                null,
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                true,
                setGroup(),
                null,
                null,
                null,
                null
        );
    }

    private Group setGroup() {
        return new Group(1L, "GROUP_STUDENTS", null);
    }
}
