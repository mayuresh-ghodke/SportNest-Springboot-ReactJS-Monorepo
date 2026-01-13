package com.ecommerce.library.service.impl;

import com.ecommerce.library.customerDto.CustomerResponseDto;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.CustomerProfileUpdateRequest;
import com.ecommerce.library.enumstatus.OtpPurpose;
import com.ecommerce.library.exception.ResourceNotFoundException;
import com.ecommerce.library.exception.EmailAlreadyExistsException;
import com.ecommerce.library.mapper.Mapper;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OtpService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final OtpService otpService;

    @Override
    public Customer save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(customerDto.getPassword());
        customer.setUsername(customerDto.getUsername());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setRoles(Collections.singletonList(roleRepository.findByName("CUSTOMER")));
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public CustomerDto getCustomer(String username) {
        CustomerDto customerDto = new CustomerDto();
        Customer customer = customerRepository.findByUsername(username);
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPassword(customer.getPassword());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        return customerDto;
    }

    @Override
    public Customer changePass(CustomerDto customerDto) {
        Customer customer = customerRepository.findByUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(CustomerDto dto) {
        Customer customer = customerRepository.findByUsername(dto.getUsername());
        customer.setPhoneNumber(dto.getPhoneNumber());
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Long getCustomerId(String username) {
       return customerRepository.findByUsername(username).getId();
    }

    @Override
    public Customer getCustomerById(Long id) {
        Customer customer = customerRepository.findCustomerById(id);
        if(customer==null){
            throw new ResourceNotFoundException("Customer with ID-"+id+" not found.");
        }
        return customer;
    }

    @Override
    public boolean deleteById(Long Id) {
        getCustomerById(Id);
        customerRepository.deleteById(Id);
        return true;
    }

    @Override
    public CustomerResponseDto updateProfile(String username, CustomerProfileUpdateRequest customerProfileUpdateRequest) {
        Customer customer = customerRepository.findByUsername(username);
        customer.setFirstName(customerProfileUpdateRequest.getFirstName());
        customer.setLastName(customerProfileUpdateRequest.getLastName());
        customer.setPhoneNumber(customerProfileUpdateRequest.getPhoneNumber());
        customer.setUsername(username);
        Customer updatedCustomer =  customerRepository.save(customer);
        return Mapper.mapCustomerToCustomerResponseDto(updatedCustomer);
    }

    @Transactional
    public void sendEmailChangeOtp(String oldEmail, String newEmail) {
        if (customerRepository.existsByUsername(newEmail)) {
            throw new EmailAlreadyExistsException("Email already in use");
        }
        otpService.generateAndSendOtp(newEmail, "Email change", OtpPurpose.EMAIL_CHANGE); // to send otp to new email
    }

    @Transactional
    public boolean verifyOtpAndUpdateEmail(String oldEmail,String newEmail,String otp) {
        boolean isVerified = otpService.verifyOtp(newEmail, otp, OtpPurpose.EMAIL_CHANGE);
        if(isVerified){
            Customer customer = customerRepository.findByUsername(oldEmail);
            customer.setUsername(newEmail);
            customerRepository.save(customer);
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmailExists(String email) {
        return customerRepository.existsByUsername(email);
    }

    @Override
    public void resetPassword(CustomerDto customerDto) {
        Customer customer = customerRepository.findByUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customerRepository.save(customer);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return customerRepository.existsByPhoneNumber(phoneNumber);
    }
}
