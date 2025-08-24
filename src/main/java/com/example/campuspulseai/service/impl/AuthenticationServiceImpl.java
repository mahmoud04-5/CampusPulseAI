package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.common.exception.ResourceNotFoundException;
import com.example.campuspulseai.common.util.IAuthUtils;
import com.example.campuspulseai.domain.dto.request.AuthenticationRequest;
import com.example.campuspulseai.domain.dto.request.RegisterRequest;
import com.example.campuspulseai.domain.dto.request.VerifyOtpRequest;
import com.example.campuspulseai.domain.dto.response.LoginResponse;
import com.example.campuspulseai.service.IAuthenticationService;
import com.example.campuspulseai.service.IEmailService;
import com.example.campuspulseai.southbound.entity.User;
import com.example.campuspulseai.southbound.entity.UserOTP;
import com.example.campuspulseai.southbound.mapper.AuthMapper;
import com.example.campuspulseai.southbound.mapper.OTPMapper;
import com.example.campuspulseai.southbound.repository.IUserOTPRepository;
import com.example.campuspulseai.southbound.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
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
    private final IAuthUtils authUtils;
    private final OTPMapper otpMapper;

    private static final String OTP_SUBJECT = "Your OTP Code";


    @PreAuthorize("permitAll()")
    @Override
    public void register(RegisterRequest registerRequest) throws Exception {

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User user = authenticationMapper.mapToUser(registerRequest);
        userRepository.save(user);
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
    @Transactional
    public void requestOtp(String email) throws Exception {
        userOTPRepository.deleteAllByEmail(email);
        String otp = generateUniqueOtp();
        Timestamp expiryTime = calculateExpiry();
        UserOTP userOtp = otpMapper.toEntity(email, otp, expiryTime);
        userOTPRepository.save(userOtp);
        emailService.sendEmail(email, OTP_SUBJECT,
                "Your OTP code is: " + otp + ". It will expire in " + OTP_EXPIRATION_MINUTES + " minutes.");
    }

    private Timestamp calculateExpiry() {
        return Timestamp.from(
                java.time.Instant.now().plusSeconds(OTP_EXPIRATION_MINUTES * 60L)
        );
    }

    @Transactional
    @Override
    public void verifyOtp(VerifyOtpRequest request) throws Exception {

        UserOTP token = userOTPRepository.findByOtpAndEmail(request.getOtpCode(), request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or used OTP for email: " + request.getEmail()));

        validateOTPIsNotVerified(token);

        validateOTPExpiry(token);

        token.setIsVerified(true);
        userOTPRepository.save(token);

    }

    @Override
    @Transactional
    public void resetPassword(String email, String newPassword) throws Exception {
        UserOTP userOTP = getUserOTPByEmail(email);

        validateOTPIsVerified(userOTP);
        validateOTPExpiry(userOTP);

        User user = getUserByEmail(email);
        validateNewpassword(user, newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        userOTPRepository.deleteById(userOTP.getId());
    }


    private UserOTP getUserOTPByEmail(String email) {
        return userOTPRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("No OTP request found for this email"));
    }

    private void validateOTPIsVerified(UserOTP userOTP) {
        if (Boolean.FALSE.equals(userOTP.getIsVerified())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP not verified");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private String generateUniqueOtp() {
        String otp = authUtils.generateOtp(6);
        // check database for existence
        if (Boolean.TRUE.equals(userOTPRepository.existsByOtp(otp))) {
            return generateUniqueOtp(); // recursion
        }
        return otp;
    }

    private void validateOTPIsNotVerified(UserOTP userOTP) {
        if (Boolean.TRUE.equals(userOTP.getIsVerified())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "OTP already used/verified");
        }
    }

    private void validateOTPExpiry(UserOTP userOTP) {
        Timestamp expiryTime = Timestamp.valueOf(
                userOTP.getCreatedAt().toLocalDateTime().plusMinutes(OTP_EXPIRATION_MINUTES)
        );
        if (expiryTime.before(new Timestamp(System.currentTimeMillis()))) {
            userOTPRepository.deleteById(userOTP.getId());
            throw new ResponseStatusException(HttpStatus.GONE, "OTP is expired");
        }
    }


    private void validateNewpassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be different from the old password");
        }
    }

    @Scheduled(cron = "0 0 * * * *") // Runs hourly
    @Transactional
    public void cleanupExpiredOtps() {
        userOTPRepository.deleteByCreatedAtBefore(
                Timestamp.valueOf(LocalDateTime.now().minusMinutes(OTP_EXPIRATION_MINUTES))
        );
    }
}
