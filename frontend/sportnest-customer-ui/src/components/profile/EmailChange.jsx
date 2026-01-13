import { useContext, useState } from "react";
import api from "../../services/customer-helper";
import { AuthContext } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";

const EmailChange = () => {
  
  const {logout} = useContext(AuthContext);
  const navigate = useNavigate();

  const [newEmail, setNewEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [otpSent, setOtpSent] = useState(false);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const handleSendOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      const res = await api.post("/customer/email/change",
        { newEmail },
      );
      setOtpSent(true);
      setMessage(res.data);
    } catch (err) {
      console.error(err);
      setMessage(err.response?.data || "Failed to send OTP");
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      const res = await api.post("/customer/email/verify",{ newEmail, otp });
      setMessage("Email updated successfully to " + res.data.userName);
      setOtpSent(false);
      setOtp("");
      setNewEmail("");
      logout();
      navigate("/shop/login", { replace: true });
    } 
    catch (err) {
      console.error(err);
      setMessage(err.response?.data || "OTP verification failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="email-change-container">
      <h3>Change Email</h3>
      {!otpSent ? (
        <form onSubmit={handleSendOtp}>
          <input
            type="email"
            value={newEmail}
            onChange={(e) => setNewEmail(e.target.value)}
            placeholder="Enter new email"
            required
            className="form-control mb-2"
          />
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? "Sending OTP..." : "Send OTP"}
          </button>
        </form>
      ) : (
        <form onSubmit={handleVerifyOtp}>
          <input
            type="email"
            value={newEmail}
            onChange={(e) => setNewEmail(e.target.value)}
            placeholder="Enter new email"
            required
            className="form-control mb-2"
          />
          <input
            type="text"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            placeholder="Enter OTP"
            required
            className="form-control mb-2"
          />
          <button type="submit" className="btn btn-success" disabled={loading}>
            {loading ? "Verifying OTP..." : "Verify & Update Email"}
          </button>
        </form>
      )}

      {message && <p className="mt-2">{message}</p>}
    </div>
  );
};

export default EmailChange;
