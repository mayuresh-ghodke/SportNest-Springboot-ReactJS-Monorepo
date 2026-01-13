package com.ecommerce.library.dto;

import lombok.Data;

@Data
public class CustomerLoginRequestDto {
    private String username;
    private String password;
}
