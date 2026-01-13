import { useState } from "react";
import { verifyOtp } from "../../services/forgotPasswordService";
import { useLocation, useNavigate } from "react-router-dom";

const VerifyOtp = () => {
  const [otp, setOtp] = useState("");
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email;

  const handleVerify = async (e) => {
    e.preventDefault();

    try {
      await verifyOtp(email, otp);
      alert("OTP verified successfully");
      navigate("/shop/reset-password", { state: { email } });
    } catch (err) {
      alert(err.response?.data?.message || "Invalid OTP");
    }
  };

  return (
    <div className="container mt-5 col-md-4">
      <h3 className="text-center">Verify OTP</h3>

      <form onSubmit={handleVerify}>
        <input
          type="text"
          className="form-control mb-3"
          placeholder="Enter OTP"
          value={otp}
          onChange={(e) => setOtp(e.target.value)}
          required
        />

        <button className="btn btn-success w-100">
          Verify OTP
        </button>
      </form>
    </div>
  );
};

export default VerifyOtp;
