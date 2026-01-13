import api from "./customer-helper";

export const placeAnOrder = async () => {
  const res = await api.post("/order/save");
  return res.data;
};

export const getMyOrders = async () => {
  const res = await api.get("/order/get/all");
  return res.data;
};

export const cancelOrder = async (id) => {
  const res = await api.put(`/order/cancel/${id}`);
  return res.data;
};

export const viewOrderInformation = async (id) => {
  const res = await api.get(`/order/get/${id}/info`);
  return res.data;
};