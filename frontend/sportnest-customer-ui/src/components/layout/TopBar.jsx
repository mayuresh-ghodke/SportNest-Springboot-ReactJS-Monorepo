import { Link } from "react-router-dom";

const TopBar = () => {
  return (
    <div className="bg-light">
      <div className="container-fluid" style={{ height: "32px"}}>
        <div className="row p-2">
          {/* Left */}
          <div className="col-md-6 d-flex align-items-center">
            <Link className="nav-link font-weight-bold mx-2 nav-hover" to="/shop/about">ABOUT</Link>
            <span>|</span>
            <Link className="nav-link font-weight-bold mx-2 nav-hover" to="/shop/contact">CONTACT</Link>
            <span>|</span>
            <Link className="nav-link font-weight-bold mx-2 nav-hover" to="/shop/gallery">GALLERY</Link>
          </div>

          {/* Right */}
          <div className="col-md-6 d-flex justify-content-end align-items-center">
            <i className="fas fa-location-dot mx-1"></i> XYZ Lane, Pune
            <span className="mx-2">|</span>
            <i className="fas fa-phone mx-1"></i> +91 7559201990
            <span className="mx-2">|</span>
            <i className="fas fa-envelope mx-1"></i> ghodkemayuresh86@gmail.com
          </div>
        </div>
      </div>
    </div>
  );
};

export default TopBar;
