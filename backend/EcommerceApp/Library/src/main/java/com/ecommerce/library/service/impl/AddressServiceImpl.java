package com.ecommerce.library.service.impl;

import com.ecommerce.library.customerDto.AddressResponseDto;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.dto.AddressRequestDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.library.model.Address;
import com.ecommerce.library.repository.AddressRepository;
import com.ecommerce.library.service.AddressService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address getAddressByCustomerId(Long id) {
        return addressRepository.findAddressByCustomerId(id);
    }

    @Override
    public AddressResponseDto saveOrUpdateAddress(AddressRequestDto dto, String username) {
        Customer customer = customerRepository.findByUsername(username);
        Address address = getAddressByCustomerId(customer.getId());
        if(address == null){
            address = new Address();
        }
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setPincode(dto.getPincode());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setCustomer(customer);
        Address savedAddress = addressRepository.save(address);

        return ResponseDtoMapper.mapAddressToAddressResponseDto(savedAddress);
    }
} 
