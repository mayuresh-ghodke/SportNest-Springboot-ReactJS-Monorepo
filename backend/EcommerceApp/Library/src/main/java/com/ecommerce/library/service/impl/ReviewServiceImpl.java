package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.ProductRepository;
import org.springframework.stereotype.Service;

import com.ecommerce.library.model.Review;
import com.ecommerce.library.repository.ReviewRepository;
import com.ecommerce.library.service.ReviewService;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public boolean canCustomerReview(Long customerId, Long productId) {
        return reviewRepository.findByCustomerIdAndProductId(customerId, productId) == null;
    }

    public Review getReviewByCustomerAndProduct(Long customerId, Long productId) {
        return reviewRepository.findByCustomerIdAndProductId(customerId, productId);
    }

    public Review submitReview(Long customerId, Long productId, int ratingNumber, String feedback) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if review already exists
        Review review = reviewRepository.findByCustomerIdAndProductId(customerId, productId);
        if (review == null) {
            review = new Review();
            review.setCustomer(customer);
            review.setProduct(product);
        }

        review.setRatingNumber(ratingNumber);
        review.setFeedback(feedback);

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> getReviewsByCustomer(Long customerId) {
        return reviewRepository.findByCustomerId(customerId);
    }
}

