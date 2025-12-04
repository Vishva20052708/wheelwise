import React from "react";
import { useLocation } from "react-router-dom";
import "./Booking.css";

const ConfirmationPage = () => {
  const { state } = useLocation();

  if (!state) {
    return <p>No booking details available.</p>;
  }

  const {
    carDetails = {},
    bookingDetails = {},
    userDetails = {},
  } = state || {};

 
  const durationInMs = new Date(bookingDetails.endTime) - new Date(bookingDetails.startTime);
  const durationInDays = Math.ceil(durationInMs / (1000 * 60 * 60 * 24)); 
  const totalFare = durationInDays * carDetails.pricePerDay;
  const gstCost = totalFare * 0.18;

  return (
    <div className="confirmation-page">
      <h2 className="confirmation-title">Booking Confirmation</h2>

      <div className="confirmation-container">
        {/* Left Section: Car Details */}
        <div className="left-section">
          <div className="car-details">
            <h3>Car Details</h3>
            <div className="car-details-container">
              <img
                src="https://via.placeholder.com/400x400"
                alt={carDetails.model || "Car"}
                className="car-image"
              />
              <div className="car-info">
                <div className="icon-text">
                  
                  <span><strong>Model:</strong> {carDetails.model || "Elantra"}</span>
                </div>
                <div className="icon-text">
                 
                  <span><strong>Company:</strong> {carDetails.companyName || "Hyundai"}</span>
                </div>
                <div className="icon-text">
                  
                  <span><strong>Fuel Type:</strong> {carDetails.fuelType || "Electric"}</span>
                </div>
                <div className="icon-text">
                 
                  <span><strong>Transmission:</strong> {carDetails.transmissionType || "Automatic"}</span>
                </div>
                <div className="icon-text">
                  
                  <span><strong>Capacity:</strong> {carDetails.capacity ? `${carDetails.capacity} seats` : "5 seats"}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Right Section */}
        <div className="right-section">
          {/* Fare Details */}
          <div className="fare-details">
            <h3>Fare Details</h3>
            <p><strong>Price Per Day:</strong> ₹{carDetails.pricePerDay || "N/A"}</p>
        
            <p><strong>Duration:</strong> {durationInDays} {durationInDays > 1 ? "days" : "day"}</p>
            <p><strong>Total Fare:</strong> ₹{totalFare || "N/A"}</p>
            <p><strong>GST (Included):</strong> ₹{gstCost.toFixed(2) || "N/A"}</p>
            <p><strong>Total with GST:</strong> ₹{(totalFare + gstCost).toFixed(2) || "N/A"}</p>
          </div>

          {/* Booking Details */}
          <div className="booking-details">
            <h3>Booking Details</h3>
            <p><strong>Name:</strong> {userDetails.firstName} {userDetails.lastName}</p>
            <p><strong>Email:</strong> {userDetails.email}</p>
            <p><strong>Address:</strong> {userDetails.address}</p>
            <p><strong>Contact Number:</strong> {userDetails.contactNo}</p>
            <p>
              <strong>License Number:</strong>{" "}
              {userDetails.licenseUrl ? (
                <a href={userDetails.licenseUrl} target="_blank" rel="noopener noreferrer">
                  View License
                </a>
              ) : (
                "Not Provided"
              )}
            </p>
            <p><strong>Pickup:</strong> {bookingDetails.startTime ? new Date(bookingDetails.startTime).toLocaleString() : "N/A"}</p>
            <p><strong>Dropoff:</strong> {bookingDetails.endTime ? new Date(bookingDetails.endTime).toLocaleString() : "N/A"}</p>
            <p><strong>Duration:</strong> {durationInDays} {durationInDays > 1 ? "days" : "day"}</p>
          </div>

          {/* Confirm Booking Button */}
          <div className="confirm-button-container">
            <button
              className="confirm-booking-button"
              onClick={() => alert("Booking Confirmed!")}
            >
              Confirm Booking
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ConfirmationPage;