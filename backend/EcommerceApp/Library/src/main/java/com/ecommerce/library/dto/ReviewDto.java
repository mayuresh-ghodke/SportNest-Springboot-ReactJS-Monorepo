package com.ecommerce.library.dto;

public class ReviewDto {

    private Long reviewId;
    private int ratingNumber;
    private String feedback;
    private Long productId;

    // Customer info needed at frontend
    private Long customerId;
    private String customerName;

    public ReviewDto() {}

    public ReviewDto(Long reviewId, int ratingNumber, String feedback,
                     Long productId, Long customerId, String customerName) {
        this.reviewId = reviewId;
        this.ratingNumber = ratingNumber;
        this.feedback = feedback;
        this.productId = productId;
        this.customerId = customerId;
        this.customerName = customerName;
    }

    // Getters & setters
    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public int getRatingNumber() {
        return ratingNumber;
    }

    public void setRatingNumber(int ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
