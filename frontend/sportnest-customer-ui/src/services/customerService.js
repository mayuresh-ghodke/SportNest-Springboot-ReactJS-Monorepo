import api from "./customer-helper";

/* ===================== PROFILE ===================== */

export const registerCustomer = async (data) => {
  const res = await api.post("/register", data);
  return res.data;
}

// Get customer profile
export const getCustomerProfile = () => {
  return api.get("/customer/profile");
};

export const updateCustomerProfile = async (data) => {
  const res = await api.patch("/customer/profile", data);
  return res.data;
};


/* ===================== PASSWORD ===================== */

// Change customer password
export const changeCustomerPassword = (data) => {
  return api.post("/customer/change-password", data);
};
