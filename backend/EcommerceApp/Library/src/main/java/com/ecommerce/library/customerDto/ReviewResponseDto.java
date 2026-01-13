package com.ecommerce.library.customerDto;

import lombok.Data;

@Data
public class ReviewResponseDto {
    private Long reviewId;
    private int ratingNumber;
    private String feedback;
    private Long productId;
    private String productName;
    private Long customerId;
    private String customerName;
}

