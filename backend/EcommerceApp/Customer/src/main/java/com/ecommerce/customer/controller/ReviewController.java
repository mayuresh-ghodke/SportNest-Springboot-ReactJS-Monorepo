package com.ecommerce.customer.controller;

import com.ecommerce.library.customerDto.ReviewResponseDto;
import com.ecommerce.library.dto.ReviewRequestDto;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.model.Review;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // React app
public class ReviewController {

    private final CustomerService customerService;
    private final ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProduct(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProduct(productId);
        List<ReviewResponseDto> dtoList = reviews.stream()
                .map(ResponseDtoMapper::mapReviewToReviewResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/product/{productId}/my-review")
    public ResponseEntity<?> getMyReview(@PathVariable Long productId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not logged in"));
        }

        Long customerId = customerService.getCustomerId(principal.getName());
        Review review = reviewService.getReviewByCustomerAndProduct(customerId, productId);

        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Review not found"));
        }

        ReviewResponseDto dto = ResponseDtoMapper.mapReviewToReviewResponseDto(review);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/saveOrUpdate")
    public ResponseEntity<?> saveOrUpdateReview(@RequestBody ReviewRequestDto reviewRequest, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not logged in"));
        }

        Long customerId = customerService.getCustomerId(principal.getName());
        Review review = reviewService.submitReview(
                customerId,
                reviewRequest.getProductId(),
                reviewRequest.getRatingNumber(),
                reviewRequest.getFeedback()
        );

        String message = (review.getReviewId() != null) ? "Review added/updated successfully" : "Review added successfully";
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", message));
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not logged in"));
        }

        reviewService.getReviewsByCustomer(principal != null ? customerService.getCustomerId(principal.getName()) : null)
                .stream()
                .filter(r -> r.getReviewId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewService.submitReview(null, null, 0, null); // You can create a delete method in service instead for cleaner approach
        return ResponseEntity.ok(Map.of("message", "Review deleted successfully"));
    }

    // ================= GET ALL REVIEWS BY LOGGED-IN CUSTOMER =================
    @GetMapping("/my-reviews")
    public ResponseEntity<?> getMyReviews(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not logged in"));
        }

        Long customerId = customerService.getCustomerId(principal.getName());
        List<Review> reviews = reviewService.getReviewsByCustomer(customerId);
        List<ReviewResponseDto> dtoList = reviews.stream()
                .map(ResponseDtoMapper::mapReviewToReviewResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // ================= CHECK IF CUSTOMER CAN REVIEW =================
    @GetMapping("/can-review/{productId}")
    public ResponseEntity<Map<String, Boolean>> canCustomerReview(@PathVariable Long productId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("canReview", false));
        }

        Long customerId = customerService.getCustomerId(principal.getName());
        boolean canReview = reviewService.canCustomerReview(customerId, productId);
        return ResponseEntity.ok(Map.of("canReview", canReview));
    }
}


