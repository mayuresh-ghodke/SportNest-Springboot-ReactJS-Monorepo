package com.ecommerce.library.dto;

import lombok.Data;

@Data
public class AddressRequestDto {
    private String street;
    private String city;
    private String pincode;
    private String state;
    private String country;
}

