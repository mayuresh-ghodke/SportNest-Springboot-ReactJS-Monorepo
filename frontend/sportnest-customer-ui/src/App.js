import { BrowserRouter } from "react-router-dom";
import { ToastContainer } from "react-toastify";

import TopBar from "./components/layout/TopBar";
import MainTop from "./components/layout/MainTop";
import Navbar from "./components/layout/Navbar";
import Footer from "./components/layout/Footer";

import CustomerRoutes from "./routes/CustomerRoutes";

function App() {
  return (
    <>
     <BrowserRouter>
      <ToastContainer
          position="top-right"
          autoClose={2500}
          hideProgressBar={false}
          newestOnTop
          closeOnClick
          pauseOnHover
        />
      {/* <TopBar /> */}
      <MainTop />
      <Navbar cartCount={0} />

      {/* Dynamic Page Content */}
      <main className="container-fluid p-0 m-0">
        <CustomerRoutes />
      </main>

      <Footer />
    </BrowserRouter>
    </>
  );
}

export default App;
