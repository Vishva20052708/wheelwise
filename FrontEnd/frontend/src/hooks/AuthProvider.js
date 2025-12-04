import React, { createContext, useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('user');
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const navigate = useNavigate();

  // Helper function to update user in state and localStorage
  const updateUserState = (updatedData) => {
    const updatedUser = {
      ...user,        // Retain other user properties not updated
      ...updatedData, // Merge updated fields
    };
  
    console.log('Updated user state before saving:', updatedUser); // Debug log
  
    // Update the user state
    setUser(updatedUser);
  
    // Update the user data in localStorage
    localStorage.setItem('user', JSON.stringify(updatedUser));
  
    console.log('User saved to localStorage:', localStorage.getItem('user')); // Debug log
  };

  // Register function
  const register = async (userData) => {
    try {
      const response = await fetch('http://localhost:8080/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Registration failed');
      }

      console.log('Registration successful');
      alert('Registration successful! Please log in.');
      navigate('/login');
    } catch (error) {
      console.error('Error during registration:', error.message);
      alert(`Registration failed: ${error.message}`);
    }
  };

  // Login function
  const login = async (credentials) => {
    try {
      const response = await fetch('http://localhost:8080/signin', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Invalid email or password');
      }

      const data = await response.json();
      console.log('Login successful:', data);

      const userData = {
        ...data,
        token: data.token, // Include token for authenticated requests
      };

      updateUserState(userData);
      navigate('/');
    } catch (error) {
      console.error('Error during login:', error.message);
      throw error;
    }
  };

  // Logout function
  const logout = async () => {
    try {
      const response = await fetch('http://localhost:8080/signout', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: user.email }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Logout failed');
      }

      console.log('Logout successful');
      setUser(null);
      localStorage.removeItem('user');
      navigate('/login');
    } catch (error) {
      console.error('Error during logout:', error.message);
      alert(`Logout failed: ${error.message}`);
    }
  };

  // Profile update function
  const updateProfile = async (updatedData) => {
    try {
      const formData = new FormData();
  
      // Append all fields to FormData
      Object.entries(updatedData).forEach(([key, value]) => {
        if (value !== undefined && value !== null && key !== 'profilePhoto') {
          formData.append(key, value);
        }
      });
  
      // If profile photo exists, append it
      if (updatedData.profilePhoto && updatedData.profilePhoto instanceof File) {
        formData.append('profilePhoto', updatedData.profilePhoto);
      }
  
      const response = await fetch('http://localhost:8080/api/user/update-profile', {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${user.token}`,
        },
        body: formData,
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Profile update failed');
      }
  
      const data = await response.json();
      console.log('Profile updated successfully:', data);
  
      // Assuming the response has a `user` object with the updated fields
      const updatedUser = data.user; // data.user contains the updated user object
  
      // Update the user object in state and localStorage
      updateUserState({
        firstName: updatedUser.firstname ,
        lastName: updatedUser.lastname ,
        contactNo: updatedUser.contactNo ,
        address: updatedUser.address ,
        licenseUrl: updatedUser.licenseUrl ,
        profilePhotoUrl: updatedUser.profilePhotoUrl || user.profilePhotoUrl,
      });
  
      alert('Profile updated successfully!');
    } catch (error) {
      console.error('Error updating profile:', error.message);
      alert(`Profile update failed: ${error.message}`);
    }
  };
  

  return (
    <AuthContext.Provider value={{ user, setUser, register, login, logout, updateProfile }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
