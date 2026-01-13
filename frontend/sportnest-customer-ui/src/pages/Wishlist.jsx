import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import { getWishlist, removeFromWishlist } from "../services/wishlistService";
import { addToCart } from "../services/cartService";

const Wishlist = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  /* ================= FETCH WISHLIST ================= */

  const fetchWishlist = async () => {
    try {
      const res = await getWishlist();
      setProducts(res.data || []);
    } catch (err) {
      toast.error("Failed to load wishlist");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchWishlist();
  }, []);

  /* ================= ADD SINGLE ITEM TO CART ================= */

  const handleAddToCart = async (productId) => {
    try {
      await addToCart(productId, 1);
      await removeFromWishlist(productId);

      setProducts((prev) => prev.filter(p => p.id !== productId));
      toast.success("Product added to cart");
    } catch (err) {
      toast.error("Failed to add to cart");
      console.error(err);
    }
  };

  /* ================= REMOVE FROM WISHLIST ================= */

  const handleRemove = async (productId) => {
    try {
      await removeFromWishlist(productId);
      setProducts((prev) => prev.filter(p => p.id !== productId));
      toast.success("Removed from wishlist");
    } catch (err) {
      toast.error("Failed to remove item");
      console.error(err);
    }
  };

  /* ================= ADD ALL TO CART ================= */

  const addAllToCart = async () => {
    try {
      for (const product of products) {
        if (product.currentQuantity > 0) {
          await addToCart(product.id, 1);
          await removeFromWishlist(product.id);
        }
      }
      setProducts([]);
      toast.success("All items added to cart");
    } catch (err) {
      toast.error("Failed to add all items");
      console.error(err);
    }
  };

  /* ================= LOADING ================= */

  if (loading) {
    return <div className="container mt-5 text-center">Loading...</div>;
  }

  /* ================= UI ================= */

  return (
    <div className="container mt-4">

      {products.length === 0 && (
        <div className="text-center">
          <h3 className="mt-3">Oops! Your wishlist looks empty.</h3>
          <img
            src="/images/empty_shopping_cart.jpeg"
            alt="Empty wishlist"
            height="300"
          />
        </div>
      )}

      {products.length > 0 && (
        <div className="row align-items-center mb-4">
        <div className="col-md-6">
          <h3>Your Wishlist</h3>
        </div>

        <div className="col-md-4 text-end">
          {products.length > 0 && (
            <button
              className="btn btn-success"
              onClick={addAllToCart}
            >
              Add All To Cart
            </button>
          )}
        </div>

        <div className="col-md-2">
          <p className="bg-dark text-white text-center p-2 rounded-pill">
            Total Items: {products.length}
          </p>
        </div>
      </div>
      )}

      {/* TABLE */}
      {products.length > 0 && (
        <table className="table bg-white">
          <thead className="bg-dark text-white">
            <tr>
              <th>Product</th>
              <th>Name</th>
              <th>Price</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {products.map(product => (
              <tr key={product.id}>
                <td>
                  <img
                    src={`data:image/webp;base64,${product.image}`}
                    alt={product.name}
                    height="100"
                    width="100"
                  />
                </td>

                <td>{product.name}</td>
                <td>₹{product.costPrice}</td>

                <td>
                  {product.currentQuantity > 0 ? (
                    <>
                      <span className="text-success">In Stock</span>
                      {product.currentQuantity <= 5 && (
                        <div className="text-warning">
                          Only {product.currentQuantity} left
                        </div>
                      )}
                    </>
                  ) : (
                    <span className="text-danger">Out of Stock</span>
                  )}
                </td>

                <td>
                  {product.currentQuantity > 0 && (
                    <button
                      className="btn btn-outline-primary me-2"
                      onClick={() => handleAddToCart(product.id)}
                    >
                      Add to Cart
                    </button>
                  )}

                  <button
                    className="btn btn-danger"
                    onClick={() => handleRemove(product.id)}
                  >
                    Remove
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default Wishlist;
