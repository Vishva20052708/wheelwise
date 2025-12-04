import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/AuthProvider';
import logo from '../../assets/logo.png';
import { FaUserCircle } from 'react-icons/fa'; 
import './Header.css';

const Header = () => {
  const { user, logout } = useAuth();
  const [loading, setLoading] = useState(false);
  const [dropdownVisible, setDropdownVisible] = useState(false); 
  const dropdownRef = useRef(null); 
  const navigate = useNavigate();

  
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setDropdownVisible(false); 
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleLogout = async () => {
    try {
      setLoading(true);
      const response = await fetch('http://localhost:8080/signout', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: user.email,
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Logout failed');
      }

      const data = await response.json();
      console.log('Logout successful:', data.message);
      await logout();
      alert(data.message);
      navigate('/login');
    } catch (error) {
      console.error('Logout failed:', error.message);
      alert(`Logout failed: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  const toggleDropdown = () => setDropdownVisible((prev) => !prev);

 
  const handleDropdownClick = (path) => {
    navigate(path);
    setDropdownVisible(false);
  };

  return (
    <header className="header">
      <Link to="/" className="logo">
        <img src={logo} alt="Wheel Wise Logo" className="logo-img" />
      </Link>
      <nav>
        <Link to="/" className="nav-link">Home</Link>
        {user ? (
          <div className="user-menu">
            <FaUserCircle
              className="profile-icon"
              onClick={toggleDropdown}
              title="Profile Menu"
            />
            <span className="username">{user.username}</span>
            {dropdownVisible && (
              <div className="dropdown-menu" ref={dropdownRef}>
                <button
                  className="dropdown-item"
                  onClick={() => handleDropdownClick('/profile')}
                >
                  Profile
                </button>
                <button
                  className="dropdown-item"
                  onClick={handleLogout}
                  disabled={loading}
                >
                  {loading ? 'Logging out...' : 'Logout'}
                </button>
              </div>
            )}
          </div>
        ) : (
          <>
            <Link to="/login" className="nav-link">Login</Link>
            <Link to="/register" className="nav-link">Register</Link>
          </>
        )}
      </nav>
    </header>
  );
};

export default Header;
