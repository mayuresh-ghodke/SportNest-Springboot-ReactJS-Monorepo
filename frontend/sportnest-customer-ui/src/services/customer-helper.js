
import axios from "axios";
import { toast } from "react-toastify";

const api = axios.create({
  baseURL: "http://localhost:8020/shop/api",
});

// Attach JWT before every request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token"); // or jwtToken
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// GLOBAL RESPONSE ERROR HANDLER
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Backend not reachable / network error
    if (!error.response) {
      toast.error("Server is not reachable. Please try again later.");
    } 
    // Unauthorized (JWT expired or invalid)
    else if (error.response.status === 401) {
      toast.error("Session expired. Please login again.");
      localStorage.removeItem("token");
      window.location.href = "/shop/login";
    } 
    // Forbidden
    else if (error.response.status === 403) {
      toast.error("You are not authorized to access this resource.");
    } 
    // Server error
    else if (error.response.status >= 500) {
      toast.error("Something went wrong on server. Try again.");
    }

    return Promise.reject(error);
  }
);

export default api;
