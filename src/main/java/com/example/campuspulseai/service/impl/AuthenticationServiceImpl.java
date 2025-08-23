package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.common.exception.ResourceNotFoundException;
import com.example.campuspulseai.domain.dto.request.AuthenticationRequest;
import com.example.campuspulseai.domain.dto.request.RegisterRequest;
import com.example.campuspulseai.domain.dto.response.LoginResponse;
import com.example.campuspulseai.service.IAuthenticationService;
import com.example.campuspulseai.service.IEmailService;
import com.example.campuspulseai.southbound.entity.User;
import com.example.campuspulseai.southbound.entity.UserOTP;
import com.example.campuspulseai.southbound.mapper.AuthMapper;
import com.example.campuspulseai.southbound.repository.IUserOTPRepository;
import com.example.campuspulseai.southbound.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authenticationMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;
    private final IEmailService emailService;
    private final IUserOTPRepository userOTPRepository;
    private static final int OTP_EXPIRATION_MINUTES = 2;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) throws Exception {

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User user = authenticationMapper.mapToUser(registerRequest);
        userRepository.save(user);
        sendWelcomeEmail(user);
    }

    @Override
    public LoginResponse login(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(user);

        return authenticationMapper.mapToLoginResponse(user, jwtToken);
    }

    @Override
    public void requestOtp(String email) throws Exception {
        // TODO: Implement OTP generation and sending logic
    }

    @Override
    public void verifyOtp(String email, String otp) throws Exception {
        // TODO verify the OTP
    }

    @Override
    @Transactional
    public void resetPassword(String email, String newPassword) throws Exception {
        UserOTP userOTP = getUserOTPByEmail(email);

        validateOTP(userOTP);

        User user = getUserByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        userOTPRepository.deleteById(userOTP.getId());
    }

    private void sendWelcomeEmail(User user) {
        String subject = "Welcome to Campus Pulse AI!";
        String body = "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n\nThank you for registering with Campus Pulse AI. We are excited to have you on board!\n\nBest regards,\nCampus Pulse AI Team";
        emailService.sendEmail(user.getEmail(), subject, body);
    }

    private UserOTP getUserOTPByEmail(String email) {
        return userOTPRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("No OTP request found for this email"));
    }

    private void validateOTP(UserOTP userOTP) {
        if (!userOTP.getIsVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP not verified");
        }
        if (userOTP.getCreatedAt().toLocalDateTime().isAfter(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES))) {
            userOTPRepository.deleteById(userOTP.getId());
            throw new ResponseStatusException(HttpStatus.GONE, "OTP is expired");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
