import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { viewOrderInformation } from "../services/orderService";
import { toast } from "react-toastify";

const OrderInformation = () => {

  const { orderId } = useParams();
  const [orderInfo, setOrderInfo] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrderInfo();
  }, [orderId]);

  const fetchOrderInfo = async () => {
    try {
      const data = await viewOrderInformation(orderId);
      setOrderInfo(data);
    } catch {
      toast.error("Failed to load order information");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <h4 className="text-center mt-5">Loading order details...</h4>;
  }

  if (!orderInfo) {
    return <h5 className="text-center text-danger">Order not found</h5>;
  }

  const {
    orderResponseDto: order,
    customerResponseDto: customer,
    addressResponseDto
  } = orderInfo;

  return (
    <div className="container mt-4 mb-5">

      {/* ================= ORDER INFO ================= */}
      <div className="card mb-4 shadow-sm">
        <div className="card-header bg-dark text-white">
          <h5 className="mb-0">Order Information</h5>
        </div>

        <div className="card-body">
          <div className="row mb-3">
            <div className="col-md-3">
              <strong>Order ID</strong>
              <div>{order.orderId}</div>
            </div>

            <div className="col-md-3">
              <strong>Status</strong>
              <div>
                <span className="badge bg-info">
                  {order.orderStatus}
                </span>
              </div>
            </div>

            <div className="col-md-3">
              <strong>Order Date</strong>
              <div>
                {new Date(order.orderDate).toLocaleDateString("en-IN")}
              </div>
            </div>

            <div className="col-md-3">
              <strong>Delivery Date</strong>
              <div>
                {order.deliveryDate
                  ? new Date(order.deliveryDate).toLocaleDateString("en-IN")
                  : "Pending"}
              </div>
            </div>
          </div>

          <hr />

          <div className="row">
            <div className="col-md-3">
              <strong>Total Amount</strong>
              <div className="fw-bold text-success">
                ₹ {order.totalPrice}
              </div>
            </div>

            <div className="col-md-3">
              <strong>Payment Status</strong>
              <div>
                <span
                  className={`badge ${
                    order.paymentStatus === "PAID"
                      ? "bg-success"
                      : "bg-warning text-dark"
                  }`}
                >
                  {order.paymentStatus}
                </span>
              </div>
            </div>

            <div className="col-md-3">
              <strong>Payment Method</strong>
              <div>{order.paymentMethod || "RAZORPAY"}</div>
            </div>

            <div className="col-md-3">
              <strong>Delivery Person</strong>
              <div>{order.deliveryPersonName || "Not Assigned"}</div>
            </div>
          </div>
        </div>
      </div>

      {/* ================= PRODUCTS ================= */}
      <div className="card mb-4 shadow-sm">
        <div className="card-header bg-success text-white">
          <h5 className="mb-0">Ordered Products</h5>
        </div>

        <ul className="list-group list-group-flush">
          {order.items.map(item => (
            <li key={item.productId} className="list-group-item">
              <div className="row align-items-center">
                <div className="col-md-6 fw-semibold">
                  {item.productName}
                </div>
                <div className="col-md-6">
                  Qty: {item.quantity}
                </div>
                {/* <div className="col-md-2">
                  ₹ {item.price}
                </div>
                <div className="col-md-2 fw-bold text-success">
                  ₹ {item.subTotal}
                </div> */}
              </div>
            </li>
          ))}
        </ul>
      </div>

      {/* ================= CUSTOMER + ADDRESS ================= */}
      <div className="row">
        <div className="col-md-6 mb-3">
          <div className="card shadow-sm h-100">
            <div className="card-header bg-secondary text-white">
              <h6 className="mb-0">Customer Information</h6>
            </div>
            <div className="card-body">
              <p className="mb-1">
                <strong>Name:</strong> {customer.firstName} {customer.lastName}
              </p>
              <p className="mb-1">
                <strong>Username:</strong> {customer.userName}
              </p>
              <p className="mb-0">
                <strong>Phone:</strong> {customer.phoneNumber}
              </p>
            </div>
          </div>
        </div>

        <div className="col-md-6 mb-3">
          <div className="card shadow-sm h-100">
            <div className="card-header bg-info text-white">
              <h6 className="mb-0">Delivery Address</h6>
            </div>
            <div className="card-body">
              <p className="mb-0">
                {addressResponseDto.addressLine}<br />
                {addressResponseDto.city} - {addressResponseDto.pincode}
              </p>
            </div>
          </div>
        </div>
      </div>

    </div>
  );
};

export default OrderInformation;
