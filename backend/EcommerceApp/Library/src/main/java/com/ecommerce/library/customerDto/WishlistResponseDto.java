package com.ecommerce.library.customerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponseDto {
    private Long wishlistId;

    private Long productId;
    private String name;
    private double price;
    private int quantity;
    private String image;

    private boolean inStock;
}
