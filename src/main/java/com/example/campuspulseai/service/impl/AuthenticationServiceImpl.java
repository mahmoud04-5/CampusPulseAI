package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.domain.dto.Request.AuthenticationRequest;
import com.example.campuspulseai.domain.dto.Request.RegisterRequest;
import com.example.campuspulseai.domain.dto.Response.LoginResponse;
import com.example.campuspulseai.service.IAuthenticationService;
import com.example.campuspulseai.southbound.entity.User;
import com.example.campuspulseai.southbound.mapper.AuthMapper;
import com.example.campuspulseai.southbound.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public LoginResponse login(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();

        HashMap claims = new HashMap<>();
        claims.put("authorities", user.getAuthorities());

        String jwtToken = jwtService.generateToken(claims, user);

        return authenticationMapper.mapToLoginResponse(user, jwtToken);
    }

}
