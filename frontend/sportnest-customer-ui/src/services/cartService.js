import api from "./customer-helper";

export const getCart = async () => {
  const res = await api.get("/cart");
  return res.data;
};


export const addToCart = async (productId, quantity = 1) => {
    const res = await api.post("/cart/add", null, {
        params: { productId, quantity }
    });
    return res.data;
};

export const updateCartItem = async (productId, quantity) => {
    const res = await api.put("/cart/update", null, {
        params: { productId, quantity }
    });
    return res.data;
};

export const removeCartItem = async (productId) => {
    const res = await api.delete(`/cart/remove/${productId}`);
    return res.data;
};