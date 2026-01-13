import { useContext, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getProductById } from "../services/productService";
import { addToCart } from "../services/cartService";
import { addToWishlist } from "../services/wishlistService";
import { AuthContext } from "../context/AuthContext";
import { toast } from "react-toastify";
import "../css/ProductCard.css";

const ProductDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated } = useContext(AuthContext);

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchProduct();
  }, [id]);

  const fetchProduct = async () => {
    try {
      const res = await getProductById(id);
      setProduct(res.data);
    } catch (err) {
      toast.error("Product not found");
      navigate("/shop/catalog");
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      navigate("/shop/login");
      return;
    }

    try {
      const res = await addToCart(product.id, 1);
      const items = res.data.items || [];
      const lastItem = items[items.length - 1];
      toast.success(
        lastItem?.productName
          ? `${lastItem.productName} added to cart`
          : "Product added to cart"
      );
    } catch {
      toast.error("Failed to add to cart");
    }
  };

  const handleAddToWishlist = async () => {
    if (!isAuthenticated) {
      navigate("/shop/login");
      return;
    }

    try {
      const res = await addToWishlist(product.id);
      toast.success(res.data.message);
    } catch {
      toast.error("Failed to add to wishlist");
    }
  };

  if (loading) {
    return (
      <div className="text-center my-5">
        <div className="spinner-border text-primary" />
      </div>
    );
  }

  if (!product) {
    return (
      <div className="text-center my-5">
        <p>Product not found</p>
      </div>
    );
  }

  return (
    <div className="container py-5">
      <div className="row g-5">
        <div className="col-md-6">
          <div className="border rounded p-3 text-center">
            <img
              src={`data:image/jpeg;base64,${product.image}`}
              alt={product.name}
              className="img-fluid rounded"
              style={{ maxHeight: "400px", objectFit: "contain" }}
            />
          </div>
        </div>

        <div className="col-md-6">
          <h2 className="mb-3">{product.name}</h2>

          <h4 className="text-primary mb-3">
            ₹ {product.costPrice}
          </h4>

          <p className="text-muted mb-4">
            {product.description}
          </p>

          <div className="d-grid gap-3">
            <button
              className="btn btn-lg add-cart-pill "
              onClick={handleAddToCart}
            >
              <i className="fas fa-shopping-cart me-2"></i>
              Add to Cart
            </button>

            <button
              className="btn btn-outline-secondary"
              onClick={handleAddToWishlist}
            >
              <i className="far fa-heart me-2"></i>
              Add to Wishlist
            </button>
          </div>

          <div className="mt-4 small text-muted">
            <div><i className="fas fa-truck me-2 text-primary"></i>Free shipping</div>
            <div><i className="fas fa-undo me-2 text-primary"></i>30-day returns</div>
            <div><i className="fas fa-shield-alt me-2 text-primary"></i>2-year warranty</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;
