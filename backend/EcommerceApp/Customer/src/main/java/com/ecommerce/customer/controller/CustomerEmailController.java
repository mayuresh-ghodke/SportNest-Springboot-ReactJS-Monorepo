package com.ecommerce.customer.controller;

import com.ecommerce.library.customerDto.EmailResponseDto;
import com.ecommerce.library.dto.email.EmailRequestDto;
import com.ecommerce.library.dto.email.EmailOtpVerifyRequestDto;
import com.ecommerce.library.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/email")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CustomerEmailController {

    private final CustomerService customerService;

    @PostMapping("/change")
    public ResponseEntity<?> requestEmailChange(
            @Valid @RequestBody EmailRequestDto req,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String oldEmail = principal.getName();
        customerService.sendEmailChangeOtp(oldEmail, req.getNewEmail());
        return ResponseEntity.ok("OTP sent to new email");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmailChange(
            @Valid @RequestBody EmailOtpVerifyRequestDto req,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String oldEmail = principal.getName();

        boolean isVerified = customerService.verifyOtpAndUpdateEmail(
                oldEmail,
                req.getNewEmail(),
                req.getOtp()
        );

        if (!isVerified) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired OTP");
        }

        EmailResponseDto dto = new EmailResponseDto();
        dto.setUserName(req.getNewEmail());
        return ResponseEntity.ok(dto);
    }

}
