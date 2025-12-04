import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './hooks/AuthProvider';
import Header from './components/Header/Header';
import HomePage from './pages/HomePage/HomePage';
import Booking from './pages/Booking/Booking';
import LoginPage from './pages/LoginPage/LoginPage';
import RegisterPage from './pages/RegisterPage/RegisterPage';
import ForgotPasswordPage from './pages/ForgotPasswordPage/ForgotPasswordPage';
import ProtectedRoute from './components/ProtectedRoute';
import ProfilePage from './pages/ProfilePage/ProfilePage';
import CarDescriptionPage from './pages/CarDescriptionPage/CarDescriptionPage';
import '@fortawesome/fontawesome-free/css/all.min.css';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Header />
       
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/forgot-password" element={<ForgotPasswordPage />} />
            <Route path="/home" element={<ProtectedRoute element={<HomePage />} />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/confirmation" element={<Booking />} />
            <Route path="/car/:id" element={<CarDescriptionPage />} />
          </Routes>
        
      </AuthProvider>
    </Router>
  );
}

export default App;
