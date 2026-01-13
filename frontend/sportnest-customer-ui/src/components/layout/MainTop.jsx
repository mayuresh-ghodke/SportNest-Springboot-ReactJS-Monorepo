import { Link } from "react-router-dom";
import "../../styles/MainTop.css";

const MainTop = () => {
  return (
    <div className="main-top">
      <div className="container">
        <div className="row align-items-center">
          <div className="col-md-6 d-flex align-items-center gap-3">
            <Link to="/shop/home" className="top-logo">
              <img
                src="/images/mainlogo.jpg"
                alt="SportNest"
              />
              <span>SPORT<span>NEST</span></span>
            </Link>

            <small className="tagline">
              Get in the game with us...
            </small>
          </div>
          <div className="col-md-6 text-end">
            <Link to="/shop/wishlist" className="wishlist-btn">
              <i className="fas fa-heart"></i>
              <span>Wishlist</span>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainTop;
