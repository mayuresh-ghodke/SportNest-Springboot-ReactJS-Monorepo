package com.ecommerce.library.service;

import com.ecommerce.library.customerDto.AddressResponseDto;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.dto.AddressRequestDto;

public interface AddressService {

   Address save(Address address);

   Address getAddressByCustomerId(Long id);

   AddressResponseDto saveOrUpdateAddress(AddressRequestDto dto, String username);
} 