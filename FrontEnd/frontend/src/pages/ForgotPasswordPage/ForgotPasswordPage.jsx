import React, { useState } from 'react';
import './ForgotPasswordPage.css';

const ForgotPasswordPage = () => {
  const [step, setStep] = useState(1); 
  const [email, setEmail] = useState('');
  const [otp, setOtp] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  
  const handleEmailSubmit = async (e) => {
    e.preventDefault();
  

    setError('');
    setMessage('');
  
    try {
      const formData = new URLSearchParams();
      formData.append('email', email);
  
      const response = await fetch('http://localhost:8080/send-otp', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData.toString(),
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to send OTP');
      }
  
      const data = await response.json();
      setMessage(data.message || 'OTP sent to your email');
      setStep(2); 
    } catch (err) {
      console.error('Error sending OTP:', err.message);
      setError(err.message);
    }
  };
  
  
 
  const handleOtpSubmit = async (e) => {
    e.preventDefault();
  
    setError('');
    setMessage('');
  
    if (newPassword !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
  
    try {
      const url = new URL('http://localhost:8080/set-password');
      url.searchParams.append('email', email);
      url.searchParams.append('otp', otp);
      url.searchParams.append('newPassword', newPassword);
      url.searchParams.append('confirmPassword', confirmPassword);
  
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to reset password');
      }
  
      const data = await response.json();
      alert(data.message || 'Password reset successful');
  
      
      setStep(1);
      setEmail('');
      setOtp('');
      setNewPassword('');
      setConfirmPassword('');
    } catch (err) {
      console.error('Error resetting password:', err.message);
      setError(err.message);
    }
  };
  

  return (
    <div className="forgot-password-container">
      <div className="forgot-password-form-container">
        {step === 1 ? (
          <form onSubmit={handleEmailSubmit} className="email-form">
            <h2 className="forgot-password-heading">Forgot Password</h2>
            <label className="forgot-password-label">Email</label>
            <input
              type="email"
              name="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              placeholder="Enter your email"
              className="forgot-password-input"
            />
  

            {error && <span className="error">{error}</span>}
            {message && <span className="message">{message}</span>}
            <button type="submit" className="forgot-password-btn forgot-password-submit-btn">
              Send OTP
            </button>
          </form>
        ) : (
          <form onSubmit={handleOtpSubmit} className="otp-password-form">
            <h2 className="forgot-password-heading">Reset Your Password</h2>
            <label className="forgot-password-label">Enter OTP</label>
            <input
              type="text"
              name="otp"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              required
              placeholder="Enter OTP"
              className="forgot-password-input"
            />
            <label className="forgot-password-label">New Password</label>
            <input
              type="password"
              name="newPassword"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              required
              placeholder="Enter new password"
              className="forgot-password-input"
            />
            <label className="forgot-password-label">Confirm Password</label>
            <input
              type="password"
              name="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
              placeholder="Confirm new password"
              className="forgot-password-input"
            />
            {error && <span className="error">{error}</span>}
            {message && <span className="message">{message}</span>}
            <button type="submit" className="forgot-password-btn forgot-password-submit-btn">
              Reset Password
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

export default ForgotPasswordPage;
