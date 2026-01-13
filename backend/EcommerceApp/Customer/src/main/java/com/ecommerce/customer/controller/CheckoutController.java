package com.ecommerce.customer.controller;

import com.ecommerce.library.customerDto.CheckoutResponseDto;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.service.CheckoutService;
import com.ecommerce.library.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/checkout") // prefix for API endpoints
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // React app
public class CheckoutController {

    private final CustomerService customerService;
    private final CheckoutService checkoutService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckoutResponseDto> getCheckOutResponse(Principal principal) {
        CustomerDto customerDto = customerService.getCustomer(principal.getName());
        CheckoutResponseDto checkoutResponseDto = checkoutService.getCheckoutResponse(customerDto);
        return ResponseEntity.ok(checkoutResponseDto);
    }
}
