// src/services/wishlistService.js
import api from "./customer-helper";


export const getWishlist = () => {
  return api.get("/wishlist");
};


export const addToWishlist = (productId) => {
  return api.post(`/wishlist/add/${productId}`);
};

export const removeFromWishlist = (productId) => {
  return api.delete(`/wishlist/delete/${productId}`);
};
