import "../css/ProductCard.css";
import { Link } from "react-router-dom";


const ProductCard = ({ product, onAddToCart, onAddToWishlist }) => {
  return (
    <div className="product-card">

      <div className="product-img-wrapper">
        <img
          src={`data:image/webp;base64,${product.image}`}
          alt={product.name}
          className="product-img"
        />

        <button
          className="wishlist-float"
          onClick={() => onAddToWishlist(product.id)}
          title="Add to Wishlist"
        >
          <i className="fas fa-heart"></i>
        </button>
      </div>

      <div className="product-body">
        <Link
          to={`/shop/product/${product.id}`}
          className="text-decoration-none text-dark"
        ><h6 className="product-title" title={product.name}>
          {product.name}
        </h6></Link>

        <div className="product-price">
          ₹{product.costPrice}
        </div>

        <button
          className="add-cart-pill"
          onClick={() => onAddToCart(product.id)}
        >
          <i className="fas fa-shopping-cart me-2"></i>
          Add to Cart
        </button>
      </div>
    </div>
  );
};

export default ProductCard;
