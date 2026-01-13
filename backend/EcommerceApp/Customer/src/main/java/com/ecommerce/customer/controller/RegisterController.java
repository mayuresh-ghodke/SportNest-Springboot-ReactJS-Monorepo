package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ForgotPasswordRequestDto;
import com.ecommerce.library.dto.email.EmailOtpVerifyRequestDto;
import com.ecommerce.library.dto.email.EmailRequestDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.EmailSenderService;
import com.ecommerce.library.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.ecommerce.library.dto.CustomerDto;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RegisterController {


    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> registerCustomer(
            @Valid @RequestBody CustomerDto customerDto,
            BindingResult result) {

        // Validation errors
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", "error",
                            "message", result.getFieldErrors()
                                    .stream()
                                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                                    .toList()
                    )
            );
        }

        // Password match
        if (!customerDto.getPassword().equals(customerDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", "Password and Confirm Password must match")
            );
        }

        // check for password length
        if (customerDto.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", "Password must be at least 8 characters")
            );
        }

        // is email already exists
        if (customerService.isEmailExists(customerDto.getUsername())) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", "Email already registered")
            );
        }

        // is phone already exists
        if (customerService.existsByPhoneNumber(customerDto.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", "Phone number already registered")
            );
        }

        customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        Customer registeredCustomer = customerService.save(customerDto);

        // Send email
        emailSenderService.sendSimpleEmail(
                registeredCustomer.getUsername(),
                "Dear " + registeredCustomer.getFirstName() + ",\n\nWelcome to SportNest!",
                "Registration Successful"
        );
        return ResponseEntity.ok(Map.of("status", "success", "message", "Registration successful"));
    }
}

