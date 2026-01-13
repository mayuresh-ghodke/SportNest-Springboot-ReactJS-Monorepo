import { useState } from "react";
import { resetPassword } from "../../services/forgotPasswordService";
import { useLocation, useNavigate } from "react-router-dom";

const ResetPassword = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email;

  const [form, setForm] = useState({
    newPassword: "",
    confirmPassword: ""
  });

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (form.newPassword !== form.confirmPassword) {
      alert("Passwords do not match");
      return;
    }

    try {
      await resetPassword({
        email,
        newPassword: form.newPassword,
        confirmPassword: form.confirmPassword
      });

      alert("Password reset successful");
      navigate("/shop/login");
    } catch (err) {
      alert(err.response?.data?.message || "Error resetting password");
    }
  };

  return (
    <div className="container mt-5 col-md-4">
      <h3 className="text-center">Reset Password</h3>

      <form onSubmit={handleSubmit}>
        <input
          type="password"
          className="form-control mb-3"
          placeholder="New Password"
          value={form.newPassword}
          onChange={(e) =>
            setForm({ ...form, newPassword: e.target.value })
          }
          required
        />

        <input
          type="password"
          className="form-control mb-3"
          placeholder="Confirm Password"
          value={form.confirmPassword}
          onChange={(e) =>
            setForm({ ...form, confirmPassword: e.target.value })
          }
          required
        />

        <button className="btn btn-dark w-100">
          Reset Password
        </button>
      </form>
    </div>
  );
};

export default ResetPassword;
