import React, { useState } from 'react';
import { useAuth } from '../../hooks/AuthProvider';
import { Link, useNavigate } from 'react-router-dom';
import './LoginPage.css';
import ill from '../../assets/illustration.png';

const LoginPage = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [input, setInput] = useState({ email: '', password: '' });
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setInput((prev) => ({ ...prev, [name]: value }));
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await login(input);
      alert('Login successful!');
      setInput({ email: '', password: '' });
      setError('');
      navigate('/');
    } catch (error) {
      console.error('Login error:', error.message);
      setError(error.message);
    }
  };

  return (
    <div className="login-page">
      <div className="image-container">
        <img src={ill} alt="Login" className="login-image" />
      </div>
      <div className="form-container">
        <form onSubmit={handleSubmit} className="login-form" autoComplete="off">
          <h2>Welcome Back</h2>
          <label>Email</label>
          <input
            type="email"
            name="email"
            value={input.email}
            onChange={handleChange}
            required
          />
          <label>Password</label>
          <input
            type="password"
            name="password"
            value={input.password}
            onChange={handleChange}
            required
          />
          {error && <span className="error">{error}</span>}
          <button type="submit">Login</button>
          <div className="footer-links">
            <div className="footer-left">
              <Link to="/forgot-password" className="footer-link">Forgot Password</Link>
            </div>
            <div className="footer-right">
              <Link to="/register" className="footer-link">Don't have an Account?</Link>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
