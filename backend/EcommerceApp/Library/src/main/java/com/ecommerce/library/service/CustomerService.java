package com.ecommerce.library.service;

import java.util.List;

import com.ecommerce.library.customerDto.CustomerResponseDto;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.CustomerProfileUpdateRequest;
import com.ecommerce.library.dto.ForgotPasswordRequestDto;
import com.ecommerce.library.model.Customer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public interface CustomerService {
    Customer save(CustomerDto customerDto);

    Customer findByUsername(String username);

    Customer update(CustomerDto customerDto);

    Customer changePass(CustomerDto customerDto);

    CustomerDto getCustomer(String username);

    List<Customer> getAllCustomers();

    Long getCustomerId(String username);

    Customer getCustomerById(Long id);

    boolean deleteById(Long Id);

    CustomerResponseDto updateProfile(String username, CustomerProfileUpdateRequest customerProfileUpdateRequest);

    void sendEmailChangeOtp(String oldEmail, @Email @NotEmpty String newEmail);

    boolean verifyOtpAndUpdateEmail(String oldEmail, @NotEmpty String newEmail, @NotEmpty String otp);

    boolean isEmailExists(@Email @NotEmpty String email);

    void resetPassword(CustomerDto customerDto);

    boolean existsByPhoneNumber(@Size(min = 10, max = 10, message = "Phone number contains 10 digits") String phoneNumber);
}
