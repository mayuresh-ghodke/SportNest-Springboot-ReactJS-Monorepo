import { createContext, useState, useEffect } from "react";
import axios from "axios";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  
  const [token, setToken] = useState(() =>
    localStorage.getItem("token")
  );

  const isAuthenticated = !!token;

  const api = axios.create({
    baseURL: "http://localhost:8020/shop/api"
  });

  api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });

  api.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response && error.response.status === 401) {
        localStorage.removeItem("token");
        window.location.replace("/shop/login"); 
      }
      return Promise.reject(error);
    }
  );

  // attach token automatically
  // api.interceptors.request.use(
  //   (config) => {
  //     if (token) {
  //       config.headers.Authorization = `Bearer ${token}`;
  //     }
  //     return config;
  //   },
  //   (error) => Promise.reject(error)
  // );

  const logout = () => {
    setToken(null);
    localStorage.removeItem("token");
  };

  // persist token
  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token);
    }
  }, [token]);

  return (
    <AuthContext.Provider
      value={{
        token,
        setToken,
        isAuthenticated,
        logout,
        api
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
