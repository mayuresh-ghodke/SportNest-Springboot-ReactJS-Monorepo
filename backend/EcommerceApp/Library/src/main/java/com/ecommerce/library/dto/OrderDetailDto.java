package com.ecommerce.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private Long productId;
    private String productName;
    private String productImage;

    private double price;
    private int quantity;
    private double subTotal;
}
