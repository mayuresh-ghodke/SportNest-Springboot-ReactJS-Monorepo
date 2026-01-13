import api from "./customer-helper";

export const getAllProducts = async () => {
    const res = await api.get("/product/get/all");
    return res.data;
};

export const getProductById = async (id) => {
  const res =  await api.get(`/product/get/${id}`);
  return res;
};

export const getAllProductsPaged = async (page = 0, size = 8) => {
    const res = await api.get("/product/get/all/paged", {
        params: { page, size }
    });
    return res.data;
};

export const getProductsByCategoryPaged = async (categoryId, page = 0, size = 8) => {
    const res = await api.get("/product/get/by-category/paged", {
        params: { categoryId, page, size }
    });
    return res.data;
};

export const getProductsBySubCategoryPaged = async (subCategoryId, page = 0, size = 8) => {
    const res = await api.get("/product/get/by-subcategory/paged", {
        params: { subCategoryId, page, size }
    });
    return res.data;
};
