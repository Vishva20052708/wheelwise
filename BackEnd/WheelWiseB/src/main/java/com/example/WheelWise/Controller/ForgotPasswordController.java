package com.example.WheelWise.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.WheelWise.entities.OtpVerification;
import com.example.WheelWise.entities.User;
import com.example.WheelWise.repositories.OtpVerificationRepository;
import com.example.WheelWise.services.EmailSenderService;
import com.example.WheelWise.services.UserService;
@RestController
public class ForgotPasswordController {

	
	@Autowired
	private EmailSenderService emailSenderService;
	
	@Autowired
	UserService userService;
	@Autowired
	OtpVerificationRepository otpVerificationRepository;

	@RequestMapping("/forgot-password")
	public ResponseEntity<Map<String, String>> renderForgotPasswordPage() {
	    Map<String, String> response = new HashMap<>();
	    response.put("status", "success");
	    response.put("message", "Forgot password page requested");
	    return ResponseEntity.ok(response);
	}

	
	@PostMapping("/send-otp")
	public ResponseEntity<Map<String, String>> sendOtp(@RequestParam String email) {
	    User foundUser = userService.findByEmail(email).orElse(null);

	    Map<String, String> response = new HashMap<>();

	    if (foundUser == null) {
	        response.put("status", "error");
	        response.put("message", "Email not found");
	        return ResponseEntity.badRequest().body(response);
	    }

	    String otp = generateOtp(); // Implement OTP generation logic
	    OtpVerification otpVerification = new OtpVerification();
	    otpVerification.setEmail(email);
	    otpVerification.setOtp(otp);
	    otpVerification.setExpirationTime(LocalDateTime.now().plusMinutes(5));
	    otpVerificationRepository.save(otpVerification);

	    emailSenderService.sendSimpleEmail(email, "Your OTP", "Your OTP is: " + otp);

	    response.put("status", "success");
	    response.put("message", "OTP sent successfully");
	    return ResponseEntity.ok(response);
	}

	private String generateOtp() {
	    return String.valueOf(new Random().nextInt(900000) + 100000); // Generates a 6-digit OTP
	}
	@PostMapping("/set-password")
	public ResponseEntity<Map<String,String>> setNewPassword(@RequestParam String email,
	                                                         @RequestParam String otp,
	                                                         @RequestParam String newPassword,
	                                                         @RequestParam String confirmPassword) {
	    Map<String, String> response = new HashMap<>();

	    // Fetch the latest OTP record for the given email
	    Optional<OtpVerification> otpVerification = otpVerificationRepository.findLatestOtpByEmail(email);

	    // Check if the OTP is valid or has expired
	    if (otpVerification.isEmpty() || !otpVerification.get().getOtp().equals(otp) ||
	        LocalDateTime.now().isAfter(otpVerification.get().getExpirationTime())) {
	        response.put("message", "Invalid or expired OTP");
	        return ResponseEntity.badRequest().body(response);
	    }

	    // Validate that newPassword and confirmPassword match
	    if (!newPassword.equals(confirmPassword)) {
	        response.put("message", "Passwords do not match");
	        return ResponseEntity.badRequest().body(response);
	    }

	    // Securely update the password using the UserService
	    boolean passwordUpdated = userService.updateUserPassword(email, newPassword);  // Ensure hashing in the updateUserPassword method

	    if (!passwordUpdated) {
	        response.put("message", "Error updating password");
	        return ResponseEntity.badRequest().body(response);
	    }

	    // Clear OTP record after successful password reset
	    otpVerificationRepository.delete(otpVerification.get());
	    response.put("message", "Password reset successfully");

	    return ResponseEntity.ok(response);
	}

}
