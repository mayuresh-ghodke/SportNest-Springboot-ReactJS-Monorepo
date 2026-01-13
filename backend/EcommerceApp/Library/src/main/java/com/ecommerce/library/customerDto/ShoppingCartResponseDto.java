package com.ecommerce.library.customerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartResponseDto {

    private Long cartId;
    private double totalPrice;
    private int totalItems;
    private Set<CartItemResponseDto> items;
}

