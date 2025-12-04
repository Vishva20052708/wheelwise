import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './RegisterPage.css';
import ill from '../../assets/illustration.png';

const RegisterPage = () => {
  const [input, setInput] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    contact: '',
  });

  const [errors, setErrors] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    contact: '',
  });

  const [passwordErrors, setPasswordErrors] = useState([]);

  const handleChange = (e) => {
    const { name, value } = e.target;

   
    setInput((prev) => ({ ...prev, [name]: value }));

   
    setErrors((prevErrors) => ({ ...prevErrors, [name]: '' }));

  
    if (name === 'password') {
      const errors = validatePassword(value);
      setPasswordErrors(errors);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    let valid = true;
    const newErrors = {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      contact: '',
    };

    if (!input.firstName.trim()) {
      newErrors.firstName = 'First Name is required.';
      valid = false;
    }

    if (!input.lastName.trim()) {
      newErrors.lastName = 'Last Name is required.';
      valid = false;
    }

    if (!validateEmail(input.email)) {
      newErrors.email = 'Please enter a valid email address.';
      valid = false;
    }

    if (!input.password.trim()) {
      newErrors.password = 'Password is required.';
      valid = false;
    } else {
      const validationResult = validatePassword(input.password);
      if (validationResult.length > 0) {
        newErrors.password = 'Password must meet the requirements.';
        valid = false;
      }
    }

    if (!validateContact(input.contact)) {
      newErrors.contact = 'Please enter a valid 10-digit contact number.';
      valid = false;
    }

    setErrors(newErrors);

    if (valid) {
      const formattedInput = {
        firstname: input.firstName,
        lastname: input.lastName,
        email: input.email,
        password: input.password,
        contactNo: input.contact,
      };

      try {
        const response = await fetch('http://localhost:8080/signup', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(formattedInput),
        });

        if (!response.ok) {
          const contentType = response.headers.get('content-type');
          let errorMessage;

          if (contentType && contentType.includes('application/json')) {
            const errorData = await response.json();
            errorMessage = errorData.message || 'Something went wrong';
          } else {
            errorMessage = await response.text();
          }

          throw new Error(errorMessage);
        }

        const data = await response.json();
        console.log('Registration successful:', data);
        alert('Registration successful!');

        setInput({
          firstName: '',
          lastName: '',
          email: '',
          password: '',
          contact: '',
        });
        setErrors({
          firstName: '',
          lastName: '',
          email: '',
          password: '',
          contact: '',
        });
        setPasswordErrors([]);
      } catch (error) {
        console.error('Error during registration:', error.message);
        alert('Registration failed: ' + error.message);
      }
    }
  };

  const validateEmail = (email) => /\S+@\S+\.\S+/.test(email);

  const validatePassword = (password) => {
    const errors = [];
    if (password.length < 6) errors.push('must be at least 6 characters.');
    if (!/[A-Z]/.test(password)) errors.push('must include at least one uppercase ');
    if (!/[a-z]/.test(password)) errors.push('must include at least one lowercase ');
    if (!/\d/.test(password)) errors.push('must include at least one number');
    if (!/[!@#$%^&*()]/.test(password)) errors.push('must include at least one special character');
    return errors;
  };

  const validateContact = (contact) => /^\d{10}$/.test(contact);

  return (
    <div className="register-page">
      <div className="image-container">
        <img src={ill} alt="Register" className="register-image" />
      </div>
      <div className="form-container">
        <form onSubmit={handleSubmit} className="register-form" noValidate autoComplete="off">
          <h2>Join Us</h2>

          <label>First Name</label>
          <input
            type="text"
            name="firstName"
            value={input.firstName}
            onChange={handleChange}
            required
            noValidate
          />
          {errors.firstName && <span className="error">{errors.firstName}</span>}

          <label>Last Name</label>
          <input
            type="text"
            name="lastName"
            value={input.lastName}
            onChange={handleChange}
            required
            noValidate
          />
          {errors.lastName && <span className="error">{errors.lastName}</span>}

          <label>Email</label>
          <input
            type="email"
            name="email"
            value={input.email}
            onChange={handleChange}
            required
            noValidate
          />
          {errors.email && <span className="error">{errors.email}</span>}

          <label>Contact Number</label>
          <input
            type="text"
            name="contact"
            value={input.contact}
            onChange={handleChange}
            required
            maxLength="10"
            noValidate
          />
          {errors.contact && <span className="error">{errors.contact}</span>}

          <div className="password-field tooltip-container">
  <label>Password</label>
  <input
    type="password"
    name="password"
    value={input.password}
    onChange={handleChange}
    required
    noValidate
    style={{ marginTop: '10px' }}
  />
  {passwordErrors.length > 0 && (
    <div className="tooltip">
      <div className="tooltip-arrow"></div>
      <ul>
        {passwordErrors.map((rule, index) => (
          <li key={index}>{rule}</li>
        ))}
      </ul>
    </div>
  )}
  {errors.password && <span className="error">{errors.password}</span>}
</div>

<button type="submit">Register</button>

<div className="footer-links">
  <Link to="/login" className="footer-link">Already have an account?</Link>
</div>


        </form>
      </div>
    </div>
  );
};

export default RegisterPage;
