package com.ecommerce.library.repository;

import com.ecommerce.library.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find review by customer and product
    Review findByCustomerIdAndProductId(Long customerId, Long productId);

    // using entity references:
    // Review findByCustomerAndProduct(Customer customer, Product product);

    // Find all reviews by a customer
    List<Review> findByCustomerId(Long customerId);

    // List<Review> findByCustomer(Customer customer);

    // Find all reviews for a product
    List<Review> findByProductId(Long productId);

    // using entity reference:
    // List<Review> findByProduct(Product product);

    // Find review by its ID
    Review findByReviewId(Long reviewId);
}
