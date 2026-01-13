import { useState } from "react";
import { changeCustomerPassword } from "../../services/customerService";
import { AuthContext } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";

const ChangePassword = () => {

  const {logout} = useContext(AuthContext);

  const navigate = useNavigate();

  const [form, setForm] = useState({});

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const submit = (e) => {
    e.preventDefault();
    changeCustomerPassword(form)
      .then(() => {
        alert("Password changed successfully. Please login again.");
        logout();
        navigate("/shop/login", { replace: true });
      })
      .catch(() => alert("Failed"));
  };

  return (
    <>
      <h3>Change Password</h3>

      <form onSubmit={submit}>
        <input name="oldPassword" type="password" className="form-control mb-2"
         onChange={handleChange} 
         placeholder="Enter Current Password"
         />
        <input name="newPassword" type="password" className="form-control mb-2" 
        onChange={handleChange} 
          placeholder="Enter New Password"
        />
        <input name="repeatNewPassword" type="text" 
        className="form-control mb-2" onChange={handleChange} 
          placeholder="Re-Enter New Password"
        />

        <button className="btn btn-success">Save</button>
      </form>
    </>
  );
};

export default ChangePassword;
