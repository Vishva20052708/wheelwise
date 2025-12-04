package com.example.WheelWise.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.WheelWise.entities.OtpVerification;

import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    // Find OTP verification record by email
    Optional<OtpVerification> findByEmail(String email);

    @Query(value = "SELECT * FROM otp_verification WHERE email = :email ORDER BY expiration_time DESC LIMIT 1", nativeQuery = true)
    Optional<OtpVerification> findLatestOtpByEmail(@Param("email") String email);

    // Find OTP verification record by email and OTP
    Optional<OtpVerification> findByEmailAndOtp(String email, String otp);

    // Optionally, delete OTP by email (useful for cleanup after verification)
    void deleteByEmail(String email);
}
