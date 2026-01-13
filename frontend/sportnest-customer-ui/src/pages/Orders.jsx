import { useEffect, useState } from "react";
import { getMyOrders, cancelOrder} from "../services/orderService";
import { toast } from "react-toastify";
import {
  createPaymentOrder,
  verifyPayment,
  openRazorpay
} from "../services/paymentService";
import { useNavigate } from "react-router-dom";


const Orders = () => {

  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Fetch orders
  const fetchOrders = async () => {
    try {
      const data = await getMyOrders();
      setOrders(data);
    } catch (err) {
      toast.error("Failed to load orders");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  // Cancel order
  const handleCancel = async (orderId) => {
    if (!window.confirm("Are you sure you want to cancel this order?")) return;

    try {
      await cancelOrder(orderId);
      toast.success("Order cancelled successfully");
      fetchOrders();
    } catch (err) {
      toast.error("Unable to cancel order");
    }
  };

  if (loading) {
    return <h4 className="text-center mt-5">Loading orders...</h4>;
  }

  const handlePayNow = async (order) => {
  try {
    const payment = await createPaymentOrder(
      order.totalPrice,
      order.orderId
    );

    openRazorpay({
      payment,
      onSuccess: async (response) => {
        await verifyPayment(response);
        toast.success("Payment successful");
        fetchOrders(); // refresh list
      },
      onFailure: () => {
        toast.error("Payment failed");
      }
    });

  } catch {
    toast.error("Unable to initiate payment");
  }
};



  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3>Your Orders</h3>
        <span className="badge bg-dark p-2">
          Total Orders: {orders.length}
        </span>
      </div>

      {orders.length === 0 ? (
        <div className="text-center">
          <h5>No Orders Found</h5>
          <img
            src="/images/empty_orders.jpeg"
            alt="No Orders"
            style={{ maxWidth: "300px" }}
          />
        </div>
      ) : (
        <table className="table table-bordered table-hover">
          <thead className="table-dark">
            <tr>
              <th>Order Date</th>
              <th>Delivery Date</th>
              <th>Order Status</th>
              <th>Quantity</th>
              <th>Total Price</th>
              <th>Payment Status</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {orders.map((order) => (
              <tr key={order.orderId}>
                <td>
                  {new Date(order.orderDate).toLocaleDateString("en-IN")}
                </td>

                <td>
                  {order.deliveryDate
                    ? new Date(order.deliveryDate).toLocaleDateString("en-IN")
                    : "Pending"}
                </td>

                <td>
                  <span
                    className={`badge ${
                      order.orderStatus === "PENDING"
                        ? "bg-warning text-dark"
                        : order.orderStatus === "CONFIRMED"
                        ? "bg-info text-dark"
                        : order.orderStatus === "SHIPPED"
                        ? "bg-primary"
                        : order.orderStatus === "DELIVERED"
                        ? "bg-success"
                        : order.orderStatus === "CANCELLED"
                        ? "bg-danger"
                        : "bg-secondary"
                    }`}
                  >
                    {order.orderStatus}
                  </span>
                </td>

                <td>{order.quantity}</td>

                <td className="fw-bold text-success">
                  ₹ {order.totalPrice.toLocaleString("en-IN")}
                </td>

                <td>
                    {order.paymentStatus === "PAID" ? (
                        <span className="badge bg-success">PAID</span>
                    ) : order.paymentStatus === "FAILED" ? (
                        <span className="badge bg-danger">FAILED</span>
                    ) : (
                        <>
                            <span className="badge bg-warning text-dark">YET TO PAY</span>
                            <button className="btn btn-sm btn-outline-success"
                                    onClick={() => handlePayNow(order)}>
                                    Pay Now
                            </button>
                        </>
                    )}
                </td>

                <td>
                  <>
                    {order.orderStatus !== "CANCELLED" &&
                    order.orderStatus !== "DELIVERED" &&
                    order.orderStatus !== "ASSIGNED" && (
                      <button
                        className="btn btn-outline-danger btn-sm"
                        onClick={() => handleCancel(order.orderId)}
                      >
                        Cancel
                      </button>
                    )}
                    <button
                      className="btn btn-info btn-sm ms-1"
                      onClick={() => navigate(`/shop/orders/${order.orderId}`)}
                    >
                      View Order
                    </button>
                  </>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default Orders;
