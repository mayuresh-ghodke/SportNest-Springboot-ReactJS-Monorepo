import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { getCart } from "../services/cartService";
import { placeAnOrder} from "../services/orderService";
import {
  createPaymentOrder,
  verifyPayment,
  openRazorpay
} from "../services/paymentService";
import { useNavigate } from "react-router-dom";
import AddressForm from "../components/AddressForm";


const Checkout = () => {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [placingOrder, setPlacingOrder] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    fetchCart();
  }, []);

  const fetchCart = async () => {
    try {
      const res = await getCart();
      setCart(res.data);
    } catch {
      setCart(null);
    } finally {
      setLoading(false);
    }
  };

  const placeOrder = async () => {
  try {
    setPlacingOrder(true);

    const placedOrder = await placeAnOrder();
    toast.success("Shopping order created successfully");

    const payment = await createPaymentOrder(
      placedOrder.totalPrice,
      placedOrder.orderId
    );

    openRazorpay({
      payment,
      onSuccess: async (response) => {
        try {
          await verifyPayment(response);
          toast.success("Payment successful");
          navigate("/shop/orders", { replace: true });
        } catch {
          toast.error("Payment verification failed");
          setPlacingOrder(false);
        }
      },
      onFailure: () => {
        toast.error("Payment failed");
        setPlacingOrder(false);
      }
    });

  } catch (err) {
    const message =
      err?.response?.data?.message || "Please add delivery address first";
    toast.error(message);
    setPlacingOrder(false);
  }
};


  if (loading) {
    return <h4 className="text-center mt-5">Loading checkout...</h4>;
  }

  if (!cart || !cart.items || cart.items.length === 0) {
    return (
      <div className="text-center mt-5">
        <h4>Your cart is empty</h4>
      </div>
    );
  }

  return (
    <div className="container mt-5">
      <div className="row">

        {/* CART ITEMS */}
        <div className="col-md-7">
          <AddressForm /> 

          <hr></hr>
          
          <h4>Order Items</h4>

          {cart.items.map(item => (
            <div key={item.id} className="border-bottom mb-3 pb-2 d-flex">
              <img
                src={`data:image/jpeg;base64,${item.productImage}`}
                alt={item.productName}
                style={{ width: 70 }}
              />
              <div className="ms-3">
                <strong>{item.productName}</strong>
                <div className="text-muted">
                  ₹ {item.unitPrice} × {item.quantity}
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* SUMMARY */}
        <div className="col-md-5">
          <div className="card">
            <div className="card-body">
              <h5>Order Summary</h5>

              <div className="d-flex justify-content-between mt-2">
                <span>Total Items</span>
                <span>{cart.totalItems}</span>
              </div>

              <div className="d-flex justify-content-between mt-2">
                <span>Shipping</span>
                <span>Free</span>
              </div>

              <hr />

              <div className="d-flex justify-content-between fw-bold">
                <span>Total Amount</span>
                <span>₹ {cart.totalPrice}</span>
              </div>

              <button
                className="btn btn-dark w-100 mt-4"
                onClick={placeOrder}
                disabled={placingOrder}
              >
                {placingOrder ? "Processing..." : "Pay & Place Order"}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Checkout;
