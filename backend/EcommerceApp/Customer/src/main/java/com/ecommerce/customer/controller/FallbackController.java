package com.ecommerce.customer.controller;

import com.ecommerce.library.exception.PageNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // prefix for API endpoints
//@CrossOrigin(origins = "http://localhost:3000") // React app
public class FallbackController {

    @RequestMapping("/shop/api/**")
    public ResponseEntity<?> handleInvalidPath() {
        throw new PageNotFoundException("Invalid path. Please check the URL.");
    }
}

