import { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { registerCustomer } from "../services/customerService";
import "../styles/Register.css";
import { Link } from "react-router-dom";

const Register = () => {

  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    username: "",
    password: "",
    confirmPassword: ""
  });

  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const validate = () => {
    let newErrors = {};
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!form.firstName.trim()) newErrors.firstName = "First name is required";
    if (!form.lastName.trim()) newErrors.lastName = "Last name is required";
    if (!/^\d{10}$/.test(form.phoneNumber))
      newErrors.phoneNumber = "Enter valid 10-digit mobile number";
    if (!emailRegex.test(form.username))
      newErrors.username = "Enter valid email address";
    if (form.password.length < 8)
      newErrors.password = "Minimum 8 characters required";
    if (form.password !== form.confirmPassword)
      newErrors.confirmPassword = "Passwords do not match";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {

    e.preventDefault();
    setSuccess("");
    setError("");

    if (!validate()) return;

    try {
      setLoading(true);
      const res = await registerCustomer(form);
      setSuccess(res.message || "Registration successful. Please login to continue shopping.!");
      setForm({
        firstName: "",
        lastName: "",
        phoneNumber: "",
        username: "",
        password: "",
        confirmPassword: ""
      });
    } 
    catch (err) {
      setError(err.response?.data?.message || "Registration failed");
    } 
    finally {
      setLoading(false);
    }
  };

  return (
    <div className="register-page">
      <div className="register-glass">
        <h2>Create Account</h2>
        <p className="subtitle">Your one-stop destination for premium sports equipment, apparel, and accessories. Built for athletes, by athletes.</p>

        {success && <div className="alert alert-success">{success}</div>}
        {error && <div className="alert alert-danger">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="row g-3">
            <div className="col-md-6">
              <input
                type="text"
                name="firstName"
                placeholder="First Name"
                className="form-control"
                value={form.firstName}
                onChange={handleChange}
              />
              <small className="text-danger">{errors.firstName}</small>
            </div>

            <div className="col-md-6">
              <input
                type="text"
                name="lastName"
                placeholder="Last Name"
                className="form-control"
                value={form.lastName}
                onChange={handleChange}
              />
              <small className="text-danger">{errors.lastName}</small>
            </div>

            <div className="col-md-6">
              <input
                type="text"
                name="phoneNumber"
                placeholder="Mobile Number"
                className="form-control"
                value={form.phoneNumber}
                onChange={handleChange}
              />
              <small className="text-danger">{errors.phoneNumber}</small>
            </div>

            <div className="col-md-6">
              <input
                type="email"
                name="username"
                placeholder="Email Address"
                className="form-control"
                value={form.username}
                onChange={handleChange}
              />
              <small className="text-danger">{errors.username}</small>
            </div>

            <div className="col-md-6">
              <input
                type="password"
                name="password"
                placeholder="Password"
                className="form-control"
                value={form.password}
                onChange={handleChange}
              />
              <small className="text-danger">{errors.password}</small>
            </div>

            <div className="col-md-6">
              <input
                type="password"
                name="confirmPassword"
                placeholder="Confirm Password"
                className="form-control"
                value={form.confirmPassword}
                onChange={handleChange}
              />
              <small className="text-danger">{errors.confirmPassword}</small>
            </div>
          </div>

          <button
            type="submit"
            className="btn signupBtn mt-4"
            disabled={loading}
          >
            {loading ? "Creating Account..." : "Create Account"}
          </button>

          <div className="text-center mt-3">
            <span>Already have an account?</span><Link to="/shop/login"> Login now</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register;
