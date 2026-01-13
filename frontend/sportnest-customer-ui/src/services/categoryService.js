import api from "./customer-helper";

export const allCategories = async () => {
    const res = await api.get("/category/get/all");
    return res.data;
};
