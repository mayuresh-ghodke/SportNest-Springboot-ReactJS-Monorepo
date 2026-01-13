package com.ecommerce.customer.controller;

import com.ecommerce.library.customerDto.AddressResponseDto;
import com.ecommerce.library.customerDto.CustomerResponseDto;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.CustomerProfileUpdateRequest;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.dto.AddressRequestDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.EmailSenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CustomerController {

    private final CustomerService customerService;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponseDto> getProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        CustomerDto customerDto = customerService.getCustomer(username);
        CustomerResponseDto customerResponseDto = ResponseDtoMapper.mapCustomerDtoToCustomerResponseDto(customerDto);
        return new ResponseEntity<>(customerResponseDto, HttpStatus.OK);
    }

    @PatchMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponseDto> updateProfile(
            @Valid @RequestBody CustomerProfileUpdateRequest customerProfileUpdateRequest,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        CustomerResponseDto customerResponseDto = customerService.updateProfile(username, customerProfileUpdateRequest);
        return new ResponseEntity<>(customerResponseDto, HttpStatus.OK);
    }

    @PutMapping("/address/save")
    public ResponseEntity<AddressResponseDto> saveAddress(@RequestBody AddressRequestDto addressRequestDto,
                                                          Principal principal){
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        AddressResponseDto addressResponseDto = addressService.saveOrUpdateAddress(addressRequestDto, username);
        return new ResponseEntity<>(addressResponseDto, HttpStatus.OK);
    }

    @GetMapping("/address/get")
    public ResponseEntity<AddressResponseDto> getAddress(Principal principal){
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        Address address = addressService.getAddressByCustomerId(customer.getId());
        AddressResponseDto addressResponseDto = ResponseDtoMapper.mapAddressToAddressResponseDto(address);
        return new ResponseEntity<>(addressResponseDto, HttpStatus.OK);
    }

    @PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request,
                                            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        String repeatPassword = request.get("repeatNewPassword");

        CustomerDto customer = customerService.getCustomer(principal.getName());

        if (passwordEncoder.matches(oldPassword, customer.getPassword())
                && !passwordEncoder.matches(newPassword, customer.getPassword())
                && repeatPassword.equals(newPassword)
                && newPassword.length() >= 5) {

            customer.setPassword(passwordEncoder.encode(newPassword));
            customerService.changePass(customer);

            emailSenderService.sendSimpleEmail(customer.getUsername(),
                    "Your password has been changed successfully.",
                    "SPORTNEST : Password Changed Success");

            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        }
        emailSenderService.sendSimpleEmail(customer.getUsername(),
                "Your attempt to change password failed.",
                "SPORTNEST : Password Change Failed");
        return ResponseEntity.badRequest()
                .body(Map.of("message", "Password validation failed"));
    }
}

