import { Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "../routes/ProtectedRoute";

import Home from "../pages/Home";
import ProductList from "../pages/ProductList";
import Cart from "../pages/Cart";
import Orders from "../pages/Orders";
import Login from "../pages/Login";
import ShoppingCatalog from "../pages/ShoppingCatalog";
import CategoriesAndSubCategories from "../components/CategoriesAndSubCategories";
import Checkout from "../pages/Checkout";
import OrderInformation from "../pages/OrderInformation";
import ProfilePage from "../pages/ProfilePage";
import Wishlist from "../pages/Wishlist";
import Register from "../pages/Register";
import OrderTracking from "../pages/OrderTracking";
import VerifyOtp from "../components/forgotPassword/VerifyOtp";
import ResetPassword from "../components/forgotPassword/ResetPassword";
import ForgotPassword from "../components/forgotPassword/ForgotPassword";
import ProductDetail from "../pages/ProductDetail";
import NotFound from "../pages/NotFound";

const CustomerRoutes = () => (
  <Routes>

    <Route path="/" element={<Navigate to="/shop/home" replace />} />
    {/* PUBLIC */}
    <Route path="/shop/home" element={<Home />} />
    <Route path="/shop/products" element={<ProductList />} />
    <Route path="/shop/product/:id" element={<ProductDetail />} />
    <Route path="/shop/categories" element={<CategoriesAndSubCategories/>} />
    <Route path="/shop/catalog" element={<ShoppingCatalog/>} />
    
    <Route path="/shop/login" element={<Login />} />
    <Route path="/shop/signup" element={<Register />} />
    <Route path="/shop/forgot-password" element={<ForgotPassword/>} />
    <Route path="/shop/verify-otp" element={<VerifyOtp />} />
    <Route path="/shop/reset-password" element={<ResetPassword />} />

    <Route path="*" element={<NotFound />} />

    {/* Protected only to logged in customers */}
    <Route path="/shop/cart" element={<ProtectedRoute><Cart /></ProtectedRoute> } />

    <Route path="/shop/orders" element={<ProtectedRoute><Orders /></ProtectedRoute>} />

    <Route path="/shop/track-order" element={<OrderTracking />} />

    <Route path="/shop/orders/:orderId" element={<ProtectedRoute><OrderInformation /></ProtectedRoute>} />

    <Route path="/shop/wishlist" element={<ProtectedRoute><Wishlist /></ProtectedRoute>} />

    <Route path="/shop/profile" element={<ProtectedRoute><ProfilePage /></ProtectedRoute>} />

    <Route path="/shop/checkout" element={<ProtectedRoute><Checkout /></ProtectedRoute>} />

  </Routes>
);

export default CustomerRoutes;
