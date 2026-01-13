import { useState, useEffect } from "react";
import { getCustomerProfile } from "../services/customerService";

import PersonalInfo from "../components/profile/PersonalInfo";
import AddressComponent from "../components/profile/AddressComponent";
import ChangePassword from "../components/profile/ChangePassword";
import EmailChange from "../components/profile/EmailChange";

const ProfilePage = () => {
  
  const [activeTab, setActiveTab] = useState("PERSONAL");
  const [customer, setCustomer] = useState(null);

  useEffect(() => {
    getCustomerProfile()
      .then(res => setCustomer(res.data))
      .catch(err => console.error(err));
  }, []);

  const logout = () => {
    localStorage.removeItem("token");
    window.location.href = "/shop/login";
  };

  return (
    <div className="container mt-5 mb-5">
      <div className="row">

        {/* Sidebar */}
        <div className="col-md-3">
          <div className="list-group">
            <button
              className={`list-group-item ${activeTab === "PERSONAL" && "active"}`}
              onClick={() => setActiveTab("PERSONAL")}
            >
              Personal Info
            </button>

            <button
              className={`list-group-item ${activeTab === "ADDRESS" && "active"}`}
              onClick={() => setActiveTab("ADDRESS")}
            >
              Address Info
            </button>

            <button
              className={`list-group-item ${activeTab === "PASSWORD" && "active"}`}
              onClick={() => setActiveTab("PASSWORD")}
            >
              Change Password
            </button>

            <button
              className={`list-group-item ${activeTab === "EMAILCHANGE" && "active"}`}
              onClick={() => setActiveTab("EMAILCHANGE")}
            >
              Email Change
            </button>

            <button className="list-group-item text-danger" onClick={logout}>
              Logout
            </button>
          </div>
        </div>

        {/* Content */}
        <div className="col-md-9">
          {!customer && <p>Loading...</p>}

          {activeTab === "PERSONAL" && customer && (
            <PersonalInfo customer={customer} setCustomer={setCustomer} />
          )}

          {activeTab === "ADDRESS" && <AddressComponent />}

          {activeTab === "PASSWORD" && <ChangePassword />}

          {activeTab === "EMAILCHANGE" && <EmailChange />}
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
