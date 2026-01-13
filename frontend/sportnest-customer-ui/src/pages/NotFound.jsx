import { Link } from "react-router-dom";

const NotFound = () => {
  return (
    <div className="container text-center py-5">
      <h1 className="display-4 text-danger">404</h1>
      <h3 className="mb-3">Page Not Found</h3>

      <p className="text-muted mb-4">
        The page you are looking for doesn’t exist or was moved.
      </p>

      <Link to="/shop/home" className="btn btn-primary">
        Home
      </Link>
    </div>
  );
};

export default NotFound;
