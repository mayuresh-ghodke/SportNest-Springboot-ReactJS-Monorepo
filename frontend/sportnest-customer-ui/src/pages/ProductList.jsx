import { useContext, useEffect, useState } from "react";
import {
  getAllProductsPaged,
  getProductsByCategoryPaged,
  getProductsBySubCategoryPaged
} from "../services/productService";
import ProductCard from "../components/ProductCard";
import { addToCart } from "../services/cartService";
import { addToWishlist } from "../services/wishlistService";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { toast } from "react-toastify";

const PAGE_SIZE = 8;

const ProductList = ({ categoryId, subCategoryId }) => {

  const [products, setProducts] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const { isAuthenticated } = useContext(AuthContext);

  const isFiltered = categoryId || subCategoryId;

  useEffect(() => {
    if (isFiltered) {
      fetchFilteredProducts(0);
    } else {
      fetchAllProducts(0);
    }
  }, [categoryId, subCategoryId]);

  const fetchAllProducts = async (page) => {
    setLoading(true);
    try {
      const data = await getAllProductsPaged(page, PAGE_SIZE);
      setProducts(data.products);
      setCurrentPage(data.currentPage);
      setTotalPages(data.totalPages);
    } catch (err) {
      console.error("Failed to fetch products");
    } finally {
      setLoading(false);
    }
  };


  const fetchFilteredProducts = async (page) => {
    setLoading(true);
    try {
      let data;

      if (subCategoryId) {
        data = await getProductsBySubCategoryPaged(subCategoryId, page, PAGE_SIZE);
      } else if (categoryId) {
        data = await getProductsByCategoryPaged(categoryId, page, PAGE_SIZE);
      }

      if (!data) return;

      setProducts(data.products);
      setCurrentPage(data.currentPage);
      setTotalPages(data.totalPages);
    } catch (err) {
      console.error("Failed to fetch filtered products");
    } finally {
      setLoading(false);
    }
  };


  const handlePageChange = (page) => {
    if (isFiltered) {
      fetchFilteredProducts(page);
    } else {
      fetchAllProducts(page);
    }
  };

  const handleAddToCart = async (productId) => {
    if (!isAuthenticated) {
      navigate("/shop/login");
      return;
    }

    try {
      const res = await addToCart(productId, 1);
      const lastItem = res.data.items[res.data.items.length - 1];
      toast.success(`${lastItem.productName} added to cart`);
    } catch (err) {
      toast.error("Failed to add to cart");
    }
  };

  const handleAddToWishlist = async (productId) => {
    if (!isAuthenticated) {
      navigate("/shop/login");
      return;
    }

    try {
      const res = await addToWishlist(productId);
      toast.success(res.data.message);
    } catch (err) {
      toast.error("Failed to add to wishlist");
    }
  };

  return (
    <>
      {loading ? (
        <div className="text-center my-5">
          <div className="spinner-border text-primary" />
        </div>
      ) : (
        <div className="row g-3">
          {products.map(product => (
            <div key={product.id} className="col-6 col-md-4 col-lg-3">
              <ProductCard
                product={product}
                onAddToCart={handleAddToCart}
                onAddToWishlist={handleAddToWishlist}
              />
            </div>
          ))}
        </div>
      )}

      {totalPages > 1 && (
        <nav className="d-flex justify-content-center mt-4">
          <ul className="pagination pagination-sm">

            <li className={`page-item ${currentPage === 0 ? "disabled" : ""}`}>
              <button
                className="page-link"
                onClick={() => handlePageChange(currentPage - 1)}
              >
                Prev
              </button>
            </li>

            {[...Array(totalPages)].map((_, i) => (
              <li key={i} className={`page-item ${currentPage === i ? "active" : ""}`}>
                <button
                  className="page-link"
                  onClick={() => handlePageChange(i)}
                >
                  {i + 1}
                </button>
              </li>
            ))}

            <li className={`page-item ${currentPage === totalPages - 1 ? "disabled" : ""}`}>
              <button
                className="page-link"
                onClick={() => handlePageChange(currentPage + 1)}
              >
                Next
              </button>
            </li>

          </ul>
        </nav>
      )}
    </>
  );
};

export default ProductList;
