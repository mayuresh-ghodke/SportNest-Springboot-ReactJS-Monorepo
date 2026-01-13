
import Dropdown from 'react-bootstrap/Dropdown';
import { useNavigate } from 'react-router-dom';

function ProfileDropdown({ user, onLogout }) {
    
    const navigate = useNavigate();

    return (
        <Dropdown align="end">
            <Dropdown.Toggle variant="outline-light" id="dropdown-basic">
                {user?.firstName && user?.lastName
                    ? `${user.customer.firstName} ${user.customer.lastName}`
                    : "My Account"}
            </Dropdown.Toggle>


            <Dropdown.Menu>
                <Dropdown.Item onClick={() => navigate('/shop/profile')}>
                    My Profile
                </Dropdown.Item>

                <Dropdown.Item onClick={() => navigate('/shop/orders')}>
                    My Orders
                </Dropdown.Item>

                <Dropdown.Item onClick={() => navigate('/shop/track-order')}>
                    Track Order
                </Dropdown.Item>

                <Dropdown.Divider />

                <Dropdown.Item onClick={onLogout} className="text-danger">
                    Logout
                </Dropdown.Item>
            </Dropdown.Menu>
        </Dropdown>
    );
}

export default ProfileDropdown;
