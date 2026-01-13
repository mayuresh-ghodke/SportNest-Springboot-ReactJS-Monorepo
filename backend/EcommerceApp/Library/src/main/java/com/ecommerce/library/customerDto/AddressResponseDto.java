package com.ecommerce.library.customerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDto {
    private Long id;
    private String street;
    private String city;
    private String pincode;
    private String state;
    private String country;
    private String firstName;
    private String lastName;
}
