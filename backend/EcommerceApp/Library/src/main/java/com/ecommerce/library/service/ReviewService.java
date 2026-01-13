package com.ecommerce.library.service;

import java.util.List;
import java.util.Map;

import com.ecommerce.library.model.Review;

import java.util.List;

public interface ReviewService {

    boolean canCustomerReview(Long customerId, Long productId);

    Review getReviewByCustomerAndProduct(Long customerId, Long productId);

    Review submitReview(Long customerId, Long productId, int ratingNumber, String feedback);

    List<Review> getReviewsByProduct(Long productId);

    List<Review> getReviewsByCustomer(Long customerId);
}

