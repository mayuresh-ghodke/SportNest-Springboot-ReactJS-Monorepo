import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { getAddress, saveOrUpdateAddress } from "../services/addressService";

const AddressForm = ({ onAddressSaved }) => {

  const emptyAddress = () => ({
    street: "",
    city: "",
    pincode: "",
    state: "",
    country: ""
  });

  const [address, setAddress] = useState(null);
  const [editable, setEditable] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadAddress();
  }, []);

  const loadAddress = async () => {
    try {
      const data = await getAddress(); 
      if (data) {
        setAddress(data);
        setEditable(false);
      } else {
        setAddress(emptyAddress());
        setEditable(true);
      }
    } catch (err) {
      setAddress(emptyAddress());
      setEditable(true);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setAddress(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const saveAddress = async () => {
    try {
      setSaving(true);

      if (Object.values(address).some(v => !v)) {
        toast.error("Please fill all address fields");
        return;
      }

      const savedAddress = await saveOrUpdateAddress(address);

      toast.success("Address saved successfully");
      setAddress(savedAddress);
      setEditable(false);

      if (onAddressSaved) {
        onAddressSaved(savedAddress);
      }

    } catch (err) {
      toast.error("Failed to save address");
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <p>Loading address...</p>;
  }

  if (!address) {
    return null;
  }

  return (
    <div className="card mb-4">
      <div className="card-body">

        <h5 className="mb-3">Delivery Address</h5>

        <div className="row">

          <div className="col-md-12 mb-2">
            <input
              type="text"
              className="form-control"
              placeholder="Street Address"
              name="street"
              value={address.street}
              onChange={handleChange}
              disabled={!editable}
            />
          </div>

          <div className="col-md-6 mb-2">
            <input
              type="text"
              className="form-control"
              placeholder="City"
              name="city"
              value={address.city}
              onChange={handleChange}
              disabled={!editable}
            />
          </div>

          <div className="col-md-6 mb-2">
            <input
              type="text"
              className="form-control"
              placeholder="Pincode"
              name="pincode"
              value={address.pincode}
              onChange={handleChange}
              disabled={!editable}
            />
          </div>

          <div className="col-md-6 mb-2">
            <input
              type="text"
              className="form-control"
              placeholder="State"
              name="state"
              value={address.state}
              onChange={handleChange}
              disabled={!editable}
            />
          </div>

          <div className="col-md-6 mb-3">
            <input
              type="text"
              className="form-control"
              placeholder="Country"
              name="country"
              value={address.country}
              onChange={handleChange}
              disabled={!editable}
            />
          </div>

        </div>

        {!editable ? (
          <button
            className="btn btn-outline-primary"
            onClick={() => setEditable(true)}
          >
            Edit Address
          </button>
        ) : (
          <button
            className="btn btn-primary"
            onClick={saveAddress}
            disabled={saving}
          >
            {saving ? "Saving..." : "Save Address"}
          </button>
        )}

      </div>
    </div>
  );
};

export default AddressForm;
