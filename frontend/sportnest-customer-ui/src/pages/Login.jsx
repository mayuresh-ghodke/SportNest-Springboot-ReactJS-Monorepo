import { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { AuthContext } from "../context/AuthContext";
import "../styles/Login.css";

const Login = () => {

  const { setToken } = useContext(AuthContext);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      setLoading(true);
      const res = await axios.post(
        "http://localhost:8020/shop/api/auth/login",
        { username: email, password },
        { headers: { "Content-Type": "application/json" } }
      );

      setToken(res.data.token);
      navigate("/shop/home");

    } catch (err) {
      setError("Invalid email or password");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-glass">
        <h2>Welcome Back</h2>
        <p className="subtitle">Login to continue shopping</p>

        {error && <div className="alert alert-danger">{error}</div>}

        <form onSubmit={handleLogin}>
          <input
            className="form-control mb-3"
            type="email"
            placeholder="Email address"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            className="form-control mb-3"
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button
            type="submit"
            className="btn" id="loginSubmitBtn"
            disabled={loading}
          >
            {loading ? "Logging in..." : "Login"}
          </button>

          <div className="text-center mt-3">
            <Link to="/shop/forgot-password">Forgot Password ?</Link>
          </div>

          <div className="text-center mt-3">
            <span>Don’t have an account ?</span><Link to="/shop/signup"> Sign up</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
