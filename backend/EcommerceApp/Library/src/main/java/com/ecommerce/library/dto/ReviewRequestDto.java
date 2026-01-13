package com.ecommerce.library.dto;

import lombok.Data;

@Data
public class ReviewRequestDto {

    private Long productId;
    private int ratingNumber;
    private String feedback;
}

