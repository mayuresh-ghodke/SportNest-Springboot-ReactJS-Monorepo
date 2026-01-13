import api from "./customer-helper";

export const sendOtp = async (email) =>{
  const res = await api.post("/forgot-password/send-otp", { email });
  return res.data;
}

export const verifyOtp = async (email, otp) => {
  const res = await api.post("/forgot-password/verify-otp", { email, otp });
  return res.data;
}

export const resetPassword = async (data) => {
  const res = await api.post("/forgot-password/reset", data);
  return res.data;
}