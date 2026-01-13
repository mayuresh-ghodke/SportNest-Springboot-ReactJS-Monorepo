package com.ecommerce.library.service;

import com.ecommerce.library.customerDto.CheckoutResponseDto;
import com.ecommerce.library.dto.CustomerDto;

public interface CheckoutService {

    CheckoutResponseDto getCheckoutResponse(CustomerDto customerDto);
}
