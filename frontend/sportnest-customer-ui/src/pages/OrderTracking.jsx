import { useState } from "react";
import api from "../services/customer-helper.js";
import "bootstrap/dist/css/bootstrap.min.css";
import "@fortawesome/fontawesome-free/css/all.min.css";

const STATUS_FLOW = [
  "PENDING",
  "CONFIRMED",
  "ASSIGNED",
  "SHIPPED",
  "DELIVERED",
];

const OrderTracking = () => {
  const [orderId, setOrderId] = useState("");
  const [order, setOrder] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const trackOrder = async (e) => {
    e.preventDefault();
    setError("");
    setOrder(null);
    setLoading(true);

    try {
      const res = await api.get(`/order/${orderId}/track`);
      setOrder(res.data.orderResponseDto);
    } catch (err) {
      if (err.response?.status === 401) {
        setError("Please login to track your order.");
      } else if (err.response?.status === 403) {
        setError("You are not allowed to view this order.");
      } else {
        setError("Order not found.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5 p-5">
      <h3 className="text-center text-primary fw-bold mb-4">
        Track Your Order
      </h3>

      {/* Search Card */}
      <div className="card shadow col-md-6 offset-md-3">
        <div className="card-body">
          <form onSubmit={trackOrder}>
            <input
              type="number"
              className="form-control mb-3"
              placeholder="Enter Order ID"
              value={orderId}
              onChange={(e) => setOrderId(e.target.value)}
              required
            />

            <button className="btn btn-primary w-100" disabled={loading}>
              {loading ? "Tracking..." : "Track Order"}
            </button>
          </form>

          {error && (
            <div className="alert alert-danger mt-3 text-center">
              {error}
            </div>
          )}
        </div>
      </div>

      {/* Order Details */}
      {order && (
        <div className="mt-5">
          <div className="card shadow">
            <div className="card-body row text-center">
              <div className="col-md-3">
                <strong>Order ID</strong>
                <p>{order.orderId}</p>
              </div>

              <div className="col-md-3">
                <strong>Order Date</strong>
                <p>{new Date(order.orderDate).toLocaleDateString()}</p>
              </div>

              <div className="col-md-3">
                <strong>Status</strong>
                <p className="fw-bold text-primary">
                  {order.orderStatus}
                </p>
              </div>

              <div className="col-md-3">
                <strong>Total</strong>
                <p>₹{order.totalPrice}</p>
              </div>
            </div>
          </div>

          {/* Status Tracker */}
          {order.orderStatus !== "CANCELLED" && (
            <div className="row justify-content-center mt-4">
              {STATUS_FLOW.map((status, index) => {
                const isCompleted =
                  STATUS_FLOW.indexOf(order.orderStatus) >= index;

                return (
                  <div key={status} className="col text-center">
                    <i
                      className={`fa-solid ${
                        isCompleted
                          ? "fa-circle-check text-success"
                          : "fa-circle text-secondary"
                      } fa-2x`}
                    ></i>
                    <div className="small fw-bold mt-2">{status}</div>
                  </div>
                );
              })}
            </div>
          )}

          {/* Cancelled */}
          {order.orderStatus === "CANCELLED" && (
            <div className="alert alert-danger text-center mt-4 fw-bold">
              <i className="fa-solid fa-ban"></i> Order Cancelled
            </div>
          )}

          {/* Order Items */}
          <div className="card shadow mt-4">
            <div className="card-header fw-bold">
              Ordered Items
            </div>
            <div className="card-body">
              {order.items.map((item) => (
                <div
                  key={item.productId}
                  className="d-flex justify-content-between border-bottom py-2"
                >
                  <div>
                    <strong>{item.productName}</strong>
                    <div className="text-muted">
                      Qty: {item.quantity}
                    </div>
                  </div>
                  <div>₹{item.subTotal}</div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default OrderTracking;
