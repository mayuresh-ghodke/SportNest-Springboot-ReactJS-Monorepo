package com.ecommerce.library.customerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInformationResponseDto {

    private OrderResponseDto orderResponseDto;
    private CustomerResponseDto customerResponseDto;
    private AddressResponseDto addressResponseDto;
}
