package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.domain.DTO.Request.AuthenticationRequest;
import com.example.campuspulseai.domain.DTO.Request.RegisterRequest;
import com.example.campuspulseai.domain.DTO.Response.AuthenticationResponse;
import com.example.campuspulseai.service.IAuthenticationService;
import com.example.campuspulseai.southBound.entity.User;
import com.example.campuspulseai.southBound.mapper.AuthMapper;
import com.example.campuspulseai.southBound.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;


@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authenticationMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) throws Exception {

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User user = authenticationMapper.mapToUser(registerRequest);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword())
        );

        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + authenticationRequest.getEmail()));

        HashMap claims = new HashMap<>();
        claims.put("authorities", user.getAuthorities());

        String jwtToken = jwtService.generateToken(claims, user);

        return new AuthenticationResponse(jwtToken);
    }

}
