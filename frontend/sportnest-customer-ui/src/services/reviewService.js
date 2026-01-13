// src/services/reviewService.js
import api from "./customer-helper";


export const getReviewsByProduct = async (productId) => {
  try {
    const response = await api.get(`/review/product/${productId}`);
    return response.data; 
  } catch (error) {
    console.error("Failed to fetch product reviews:", error);
    throw error;
  }
};

export const getMyReview = async (productId) => {
  try {
    const response = await api.get(`/review/product/${productId}/my-review`);
    return response.data; 
  } catch (error) {
    console.error("Failed to fetch customer's review:", error);
    return null;
  }
};

export const submitReview = async (reviewRequest) => {
  try {
    const response = await api.post("/review/saveOrUpdate", reviewRequest);
    return response.data;
  } catch (error) {
    console.error("Failed to submit review:", error);
    throw error;
  }
};

export const deleteReview = async (reviewId) => {
  try {
    const response = await api.delete(`/review/delete/${reviewId}`);
    return response.data;
  } catch (error) {
    console.error("Failed to delete review:", error);
    throw error;
  }
};

export const canCustomerReview = async (productId) => {
  try {
    const response = await api.get(`/review/can-review/${productId}`);
    return response.data.canReview; 
  } catch (error) {
    console.error("Failed to check if customer can review:", error);
    return false;
  }
};
