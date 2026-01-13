import api from "./customer-helper";


export const createPaymentOrder = async (amount, shoppingOrderId) => {
  const res = await api.post("/payment/create-order",{
      amount,
      currency: "INR",
      shoppingOrderId
    }
  );
  return res.data; // razorpay order
};


export const verifyPayment = async (paymentResponse) => {
  await api.post("/payment/update-order",{
      razorpayPaymentId: paymentResponse.razorpay_payment_id,
      razorpayOrderId: paymentResponse.razorpay_order_id,
      razorpaySignature: paymentResponse.razorpay_signature
    }
  );
};


export const openRazorpay = ({ payment, onSuccess, onFailure }) => {
  if (!window.Razorpay) {
    throw new Error("Razorpay SDK not loaded");
  }

  const options = {
    key: "rzp_test_9NlQwnOUBr8xWJ",//rzp_test_... is Razorpay’s Key ID, a public 
    // identifier used by the frontend Checkout SDK to associate a payment with a 
    // merchant account
    amount: payment.amount * 100,
    currency: payment.currency,
    name: "SPORTNEST",
    description: "Order Payment",
    order_id: payment.orderId,
    handler: onSuccess,
    theme: { color: "#000000" }
  };

  const rzp = new window.Razorpay(options);

  rzp.on("payment.failed", onFailure);
  rzp.open();
};
