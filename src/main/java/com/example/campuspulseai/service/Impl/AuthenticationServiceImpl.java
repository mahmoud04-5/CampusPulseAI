package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.domain.DTO.Request.AuthenticationRequest;
import com.example.campuspulseai.domain.DTO.Request.RegisterRequest;
import com.example.campuspulseai.domain.DTO.Response.AuthenticationResponse;
import com.example.campuspulseai.service.IAuthenticationService;
import com.example.campuspulseai.southBound.entity.User;
import com.example.campuspulseai.southBound.mapper.AuthenticationMapper;
import com.example.campuspulseai.southBound.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationMapper authenticationMapper;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) throws Exception {

        User user = authenticationMapper.mapToUser(registerRequest);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponse login(AuthenticationRequest request) {
        return null;
    }

}
