import { useEffect, useState } from "react";
import { getCart, updateCartItem, removeCartItem } from "../services/cartService";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const Cart = () => {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchCart();
  }, []);

  const fetchCart = async () => {
    try {
      const res = await getCart();   
      setCart(res.data);             
    } 
    catch (err) {
      setCart(null);
    } 
    finally {
      setLoading(false);
    }
  };

  const changeQty = async (productId, newQty) => {

    if (newQty < 1) return;

    try {
      const res = await updateCartItem(productId, newQty);

      setCart(prev => ({
        ...prev,
        totalItems: res.data.totalItems,
        totalPrice: res.data.totalPrice,
        items: prev.items.map(item =>
          item.productId === productId
            ? { ...item, quantity: newQty }
            : item
        )
      }));
    } 
    catch (err) {
      toast.error(`Failed to update. Something went wrong.`);
    }
  };

  const removeItem = async (productId) => {

    if (!window.confirm("Remove this item?")) return;

    try {
      const res = await removeCartItem(productId);
      setCart(res.data);
      toast.info(`Removed from cart.`);
    } 
    catch (err) {
      toast.error(`Unable to remove from cart. Something went wrong.`);
    }
  };


  if (loading) {
    return <h4 className="text-center mt-5">Loading cart...</h4>;
  }

  if (!cart || cart.items.length === 0) {
    return (
      <div className="text-center mt-5">
        <h3>Your Cart is Empty</h3>
        <img
          src="/images/empty_shopping_cart.jpeg"
          alt="Empty Cart"
          style={{ width: 350 }}
        />
      </div>
    );
  }

  const goToCheckout = () => {
      if (!cart || cart.items.length === 0) {
        toast.info("Your cart is empty");
        return;
      }
      navigate("/shop/checkout");
    };



  return (
    <div className="container mt-4">
      <h3 className="text-center mb-4">My Cart</h3>

      <div className="row">

        {/* CART ITEMS */}
        <div className="col-md-8">
          {cart.items.map(item => (
            <div className="card mb-3" key={item.id}>
              <div className="card-body d-flex align-items-center">

                <img
                  src={`data:image/jpeg;base64,${item.productImage}`}
                  alt={item.productName}
                  style={{ width: 90 }}
                />

                <div className="ms-3 flex-grow-1">
                  <h6>{item.productName}</h6>
                  <p className="text-muted">₹ {item.unitPrice}</p>

                  <div className="d-flex align-items-center gap-2">
                    <button
                      className="btn btn-sm btn-outline-secondary"
                      onClick={() => changeQty(item.productId, item.quantity - 1)}
                    >−</button>

                    <span>{item.quantity}</span>

                    <button
                      className="btn btn-sm btn-outline-secondary"
                      onClick={() => changeQty(item.productId, item.quantity + 1)}
                    >+</button>
                  </div>
                </div>

                <div className="text-end">
                  <p className="fw-bold mb-2">
                    ₹ {item.unitPrice * item.quantity}
                  </p>
                  <button
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => removeItem(item.productId)}
                  >
                    Remove
                  </button>
                </div>

              </div>
            </div>
          ))}
        </div>

        {/* SUMMARY */}
        <div className="col-md-4">
          <div className="card">
            <div className="card-body">
              <h5>Order Summary</h5>

              <div className="d-flex justify-content-between">
                <span>Items</span>
                <span>{cart.totalItems}</span>
              </div>

              <div className="d-flex justify-content-between">
                <span>Subtotal</span>
                <span>₹ {cart.totalPrice}</span>
              </div>

              <div className="d-flex justify-content-between">
                <span>Shipping</span>
                <span>Free</span>
              </div>

              <hr />

              <div className="d-flex justify-content-between fw-bold">
                <span>Total</span>
                <span>₹ {cart.totalPrice}</span>
              </div>

              <button className="btn btn-primary w-100 mt-3" onClick={goToCheckout}>
                Proceed to Checkout
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;
