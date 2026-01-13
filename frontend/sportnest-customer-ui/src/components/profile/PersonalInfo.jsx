import { useState, useEffect } from "react";
import { updateCustomerProfile } from "../../services/customerService";

const PersonalInfo = ({ customer, setCustomer }) => {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    userName: "",
    phoneNumber: ""
  });

  const [loading, setLoading] = useState(false);

  // Load customer data into form
  useEffect(() => {
    if (customer) {
      setForm({
        firstName: customer.firstName || "",
        lastName: customer.lastName || "",
        userName: customer.userName || "",
        phoneNumber: customer.phoneNumber || ""
      });
    }
  }, [customer]);

  const handleChange = (e) => {
    setForm({...form,[e.target.name]: e.target.value});
  };

  const submit = async (e) => {
    e.preventDefault();
    
    setLoading(true);

    try {
      const res = await updateCustomerProfile(form);
      setCustomer(res);
      alert("Profile updated successfully");
    } catch (error) {
      console.error("Update failed:", error);
      alert("Failed to update profile");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <h3 className="mb-3">Personal Information</h3>

      <form onSubmit={submit}>
        <input
          type="text"
          name="firstName"
          className="form-control mb-2"
          value={form.firstName}
          onChange={handleChange}
          placeholder="First Name"
          required
        />

        <input
          type="text"
          name="lastName"
          className="form-control mb-2"
          value={form.lastName}
          onChange={handleChange}
          placeholder="Last Name"
          required
        />

        <input
          type="text"
          name="username"
          className="form-control mb-2"
          value={form.userName}
          readOnly
          disabled
        />

        <input
          type="text"
          name="phoneNumber"
          className="form-control mb-3"
          value={form.phoneNumber}
          onChange={handleChange}
          placeholder="Phone Number"
        />

        <button className="btn btn-success" disabled={loading}>
          {loading ? "Updating..." : "Update"}
        </button>
      </form>
    </>
  );
};

export default PersonalInfo;
