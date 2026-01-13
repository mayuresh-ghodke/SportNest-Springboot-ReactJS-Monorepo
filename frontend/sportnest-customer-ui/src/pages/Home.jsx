import { Link } from "react-router-dom";
import "../styles/Home.css";

const Home = () => {
  return (
    <div className="bg-light min-vh-100">

      <section className="hero-section position-relative overflow-hidden">
        <img
          src="/images/football-bg.jpg"
          alt="SportNest Banner"
          className="hero-img w-100"
          fetchpriority="high"
        />
        <div className="hero-overlay d-flex align-items-center">
          <div className="container text-white">
            <div className="col-lg-6">
              <h1 className="display-3 fw-bold mb-3">
                Gear Up for <span className="text-primary">Your Game</span>
              </h1>
              <p className="lead mb-4">
                Premium sports equipment from trusted brands.
                <br />
                Built for champions, beginners & professionals.
              </p>
              <Link to="/shop/catalog" className="btn  btn-lg px-5 py-3 fw-bold shadow-lg"
              style={{backgroundColor:"#ffcc00", color:"#3700B3"}}>
                Shop Now
              </Link>
            </div>
          </div>
        </div>
      </section>

      <section className="container py-5 mt-5">
        <div className="row align-items-center">
          <div className="col-md-7 mb-4 mb-md-0">
            <div className="pe-lg-5">
              <img
                src="/images/bg-football.jpg"
                alt="Football Equipment"
                className="img-fluid rounded-4 shadow-lg"
                loading="lazy"
              />
            </div>
          </div>
          <div className="col-md-5">
            <h2 className="fw-bold mb-4">Explore High-Performance Sports Gear</h2>
            <p className="text-muted fs-5 mb-4">
              At <strong>SportNest</strong>, we deliver reliable, durable,
              and performance-oriented sports equipment designed to help
              athletes push their limits safely and confidently.
            </p>
            <img
              src="/images/cricket.jpg"
              alt="Cricket Equipment"
              className="img-fluid rounded-4 shadow-sm"
              loading="lazy"
            />
          </div>
        </div>
      </section>

      <div className="bg-light py-5">
        <div className="container">
          <div className="text-center">
            <h1 className="font-weight-bold mb-4">Why Choose Us?</h1>
            <hr className="w-25 mx-auto mb-4" />
          </div>

          <div className="row text-center">
            {[
              {
                img: "free-shipping.jpg",
                title: "Free Shipping",
                items: [
                  "Enjoy Free Shipping on All Orders – No minimum required!",
                  "Shop for essentials or special treats without any additional cost.",
                  "We deliver your items straight to your door with no extra charges.",
                ],
              },
              {
                img: "secure-payments.jpg",
                title: "Secure Payments",
                items: [
                  "Shop with Peace of Mind – Secure Payments Guaranteed!",
                  "Advanced encryption technologies keep your payment information safe.",
                  "A range of trusted payment options to suit your preference.",
                  "Your privacy and safety are always our priority.",
                ],
              },
              {
                img: "order-tracking.jpg",
                title: "Order Tracking",
                items: [
                  "Track Your Orders with Ease!",
                  "Receive a unique tracking number once your order is placed.",
                  "Monitor the journey of your package from our warehouse to your doorstep.",
                  "Get real-time updates and estimated delivery times directly on our website.",
                ],
              },
            ].map((feature, index) => (
              <div className="col-12 col-md-4 mb-4" key={index}>
                <div className="feature-box p-4 bg-white shadow-sm rounded">
                  <img
                    src={`/images/${feature.img}`}
                    className="img-fluid mb-3 rounded"
                    alt={feature.title}
                  />
                  <h4 className="font-weight-bold mb-3">{feature.title}</h4>
                  <ul className="list-unstyled text-left">
                    {feature.items.map((item, i) => (
                      <li key={i}>{item}</li>
                    ))}
                  </ul>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* ================= FEATURES SECTION ================= */}
      <div className="container my-5">
        <hr className="my-4" />

        <div className="row">
          <div className="col-md-12 text-center">
            <h1 className="font-weight-bold mb-3">
              Unleash Your Potential with High-Quality Sports Equipment
            </h1>
            <p className="lead mb-5">
              At <strong>SportNest</strong>, we believe in equipping athletes
              with top-notch gear that empowers peak performance, safety, and
              comfort.
            </p>
          </div>
        </div>

        <div className="row text-center">
          <div className="col-md-4 mb-4">
            <div className="feature">
              <h4 className="font-weight-bold">Durable</h4>
              <p>Built for endurance and intense training.</p>
            </div>

            <div className="feature mt-4">
              <h4 className="font-weight-bold">Performance Enhancing</h4>
              <p>Designed to improve agility, strength, and speed.</p>
            </div>

            <div className="feature mt-4">
              <h4 className="font-weight-bold">Premium Quality</h4>
              <p>Meets international quality standards.</p>
            </div>
          </div>

          <div className="col-md-4 mb-4">
            <img
              src="/images/middle-home.jpg"
              className="img-fluid rounded shadow-sm"
              alt="Sports Equipment"
            />
          </div>

          <div className="col-md-4 mb-4">
            <div className="feature">
              <h4 className="font-weight-bold">Versatile Equipment</h4>
              <p>Multi-sport gear for all fitness needs.</p>
            </div>

            <div className="feature mt-4">
              <h4 className="font-weight-bold">Safety</h4>
              <p>Reduce injury risk with protective designs.</p>
            </div>

            <div className="feature mt-4">
              <h4 className="font-weight-bold">Comfortable Designs</h4>
              <p>Ergonomic and breathable for long sessions.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
