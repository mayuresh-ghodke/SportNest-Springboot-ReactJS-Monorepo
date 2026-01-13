package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OtpService;
import com.ecommerce.library.enumstatus.OtpPurpose;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forgot-password")
@CrossOrigin(origins = "http://localhost:3000")
public class ForgotPasswordController {

    private final OtpService otpService;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (!customerService.isEmailExists(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email not registered"));
        }

        otpService.generateAndSendOtp(email, "Forgot Password OTP", OtpPurpose.FORGOT_PASSWORD);
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email").trim().toLowerCase();
        String otp = request.get("otp").trim();

        boolean valid = otpService.verifyOtp(
                email,
                otp,
                OtpPurpose.FORGOT_PASSWORD
        );

        if (!valid) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid or expired OTP"));
        }

        return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
    }


    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        if (!otpService.isOtpVerified(email, OtpPurpose.FORGOT_PASSWORD)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "OTP verification required"));
        }

        if (!newPassword.equals(confirmPassword) || newPassword.length() < 6) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Password validation failed"));
        }

        CustomerDto customerDto = customerService.getCustomer(email);
        customerDto.setPassword(passwordEncoder.encode(newPassword));
        customerService.resetPassword(customerDto);

        otpService.clearOtp(email, OtpPurpose.FORGOT_PASSWORD);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }
}


