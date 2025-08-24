package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.UserOTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.Optional;

public interface IUserOTPRepository extends JpaRepository<UserOTP, Long> {
    Optional<UserOTP> findByEmail(String email);

    Optional<UserOTP> findByOtpAndEmail(String otpCode, String email);

    void deleteByCreatedAtBefore(Timestamp expiryDate);
    void deleteAllByEmail(String email);

    Boolean existsByOtp(String otp);

}
