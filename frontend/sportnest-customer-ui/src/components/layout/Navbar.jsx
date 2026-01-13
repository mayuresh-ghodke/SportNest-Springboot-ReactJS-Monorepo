import { Link, NavLink } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../context/AuthContext";
import ProfileDropdown from "../common/ProfileDropdown";
import { getCustomerProfile } from "../../services/customerService";
import "../../styles/Navbar.css";

const Navbar = ({ cartCount = 0 }) => {

  const { isAuthenticated, logout } = useContext(AuthContext);
  const [user, setUser] = useState(null);

  useEffect(() => {
    if (isAuthenticated) {
      const fetchProfile = async () => {
        try {
          const data = await getCustomerProfile();
          setUser(data);
        } catch (err) {
          // console.error("Failed to fetch profile");
        }
      };
      fetchProfile();
    }
  }, [isAuthenticated]);

  return (
    <nav className="navbar navbar-expand-lg custom-navbar sticky-top">
      <div className="container">

        {/* BRAND */}
        <Link
          className="navbar-brand brand-logo"
          to="/shop/home"
          style={{ color: "white" }}
        >
          SPORT<span>NEST</span>
        </Link>

        {/* MOBILE TOGGLER */}
        <button
          className="navbar-toggler shadow-none"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#mainNavbar"
        >
          <span className="navbar-toggler-icon text-white"></span>
        </button>

        <div className="collapse navbar-collapse" id="mainNavbar">

          <ul className="navbar-nav mx-auto">
            <li className="nav-item">
              <NavLink className="nav-link" to="/shop/home">
                Home
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link" to="/shop/catalog">
                Shop
              </NavLink>
            </li>
          </ul>

          {/* RIGHT SECTION */}
          <ul className="navbar-nav align-items-center gap-3">

            {/* CART - ALWAYS VISIBLE */}
            <li className="nav-item">
              <Link className="cart-icon" to="/shop/cart">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="22"
                  height="22"
                  fill="currentColor"
                  viewBox="0 0 16 16"
                >
                  <path d="M8 1a2.5 2.5 0 0 1 2.5 2.5V4h-5v-.5A2.5 2.5 0 0 1 8 1m3.5 3v-.5a3.5 3.5 0 1 0-7 0V4H1v10a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V4zM2 5h12v9a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1z" />
                </svg>

                {cartCount > 0 && (
                  <span className="cart-badge">{cartCount}</span>
                )}
              </Link>
            </li>

            {/* AUTH SECTION */}
            {!isAuthenticated ? (
              <div className="auth-buttons d-flex gap-2">
                <Link className="btn" id="loginBtn" to="/shop/login">
                  Login
                </Link>
                <Link className="btn" id="signUpBtn" to="/shop/signup">
                  Sign Up
                </Link>
              </div>
            ) : (
              <li className="nav-item">
                <ProfileDropdown user={user} onLogout={logout} />
              </li>
            )}

          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
