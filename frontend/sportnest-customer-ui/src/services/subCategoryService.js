import api from "./customer-helper";

export const getAllSubCategories = async () => {
    const res = await api.get("/subCategory/get/all");
    return res.data;
};

export const getSubCategoriesByCategoryId = async (id) => {
    const res = await api.get(`/subcategory/get/all/by-category/${id}`);
    return res.data;
};
