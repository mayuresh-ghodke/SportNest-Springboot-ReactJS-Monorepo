import api from "./customer-helper";

export const getAddress = async () => {
  const res = await api.get("/customer/address/get");
  return res.data;
};

export const saveOrUpdateAddress = async (address) => {
  const res = await api.put("/customer/address/save", address);
  return res.data;
};
