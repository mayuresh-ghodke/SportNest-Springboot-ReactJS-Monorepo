import { Link } from "react-router-dom";
import "../../styles/Footer.css";

const Footer = () => {
  return (
    <>
      <footer className="footer-main">
        <div className="container py-5">
          <div className="row">

            {/* BRAND + ABOUT */}
            <div className="col-md-4 mb-4">
              <h4 className="footer-brand">
                SPORT<span>NEST</span>
              </h4>
              <p className="footer-text">
                Your one-stop destination for premium sports equipment, apparel,
                and accessories. Built for athletes, by athletes.
              </p>
            </div>

            {/* SHOP LINKS */}
            <div className="col-md-2 mb-4">
              <h6 className="footer-title">Shop</h6>
              <Link to="/shop/catalog">All Products</Link>
              <Link to="/shop/categories">Categories</Link>
              <Link to="/shop/cart">Cart</Link>
            </div>

            {/* CUSTOMER */}
            <div className="col-md-3 mb-4">
              <h6 className="footer-title">Customer Care</h6>
              <Link to="/shop/profile">My Account</Link>
              <Link to="/shop/orders">My Orders</Link>
            </div>

            {/* CONTACT + SOCIAL */}
            <div className="col-md-3 mb-4">
              <h6 className="footer-title">Connect With Us</h6>

              <p className="footer-text mb-1">Pune, India</p>
              <p className="footer-text mb-1">+91 XXXXX XXXXX</p>
              <p className="footer-text">support@XXXXX.com</p>

              <div className="social-icons mt-2">
                <i className="fab fa-facebook-f"></i>
                <i className="fab fa-instagram"></i>
                <i className="fab fa-twitter"></i>
              </div>
            </div>

          </div>
        </div>
      </footer>

      {/* BOTTOM BAR */}
      <div className="footer-bottom">
        {new Date().getFullYear()} SPORTNEST • All Rights Reserved
      </div>
    </>
  );
};

export default Footer;
