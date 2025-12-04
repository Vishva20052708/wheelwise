

import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom"; 
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DateTimePicker } from "@mui/x-date-pickers/DateTimePicker";
import { TextField } from "@mui/material";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { useAuth } from "../../hooks/AuthProvider"; 
import { Dialog, DialogActions, DialogContent, DialogTitle, Button } from "@mui/material"; 
import "./CarDescriptionPage.css";

const CarDescriptionPage = () => {
    const { state } = useLocation();
    const [startTime, setStartTime] = useState(null); 
    const [endTime, setEndTime] = useState(null); 
    const { user } = useAuth(); 
    const [openDialog, setOpenDialog] = useState(false); 
    const [dialogMessage, setDialogMessage] = useState(""); 
    const [startTimeValid, setStartTimeValid] = useState(true); 
    const [endTimeValid, setEndTimeValid] = useState(true); 
    const navigate = useNavigate(); 
  
    if (!state) {
      return <p>No car details available</p>;
    }
  
    const {
      model = "",
      companyName = "",
      fuelType = "",
      transmissionType = "",
      capacity = "",
      location = "",
      pricePerDay = 0,
      type = "",
       imagePath = ""
    } = state;
  
    const handleBookingClick = () => {
        if (!startTime || !endTime) {
          setStartTimeValid(!!startTime);
          setEndTimeValid(!!endTime);
          return;
        }
      
        if (user) {
          const isProfileComplete =
            user.firstName && user.lastName && user.email && user.contactNo && user.address && user.licenseUrl;
      
          if (isProfileComplete) {
            navigate('/confirmation', {
              state: {
                carDetails: { model, companyName, fuelType, transmissionType, capacity, location, pricePerDay, type },
                bookingDetails: { startTime, endTime },
                userDetails: { 
                  firstName: user.firstName, 
                  lastName: user.lastName, 
                  email: user.email, 
                  licenseUrl: user.licenseUrl, 
                  address: user.address, 
                  contactNo: user.contactNo 
                },
              },
            });
          } else {
            setDialogMessage("Please complete your profile.");
            setOpenDialog(true);
          }
        } else {
          setDialogMessage("Please sign in to continue.");
          setOpenDialog(true);
        }
      };
      
  
    const handleCloseDialog = () => {
      setOpenDialog(false);
    };
  
    return (
      <div className="car-description-page">
        {/* Car Description */}
        <div className="top-section">
        <div className="car-overview">
  <img
    src={`http://localhost:8080/images?imagePath=${imagePath}`}
    alt={`${companyName} ${model}`}
    className="car-image"
  />
  <div className="car-detailss">
    <h2>{`${companyName} ${model}`}</h2>
    <p>
      <strong>Location:</strong> {location}
    </p>
    <p>
      <strong>Price per Day: </strong> Rs .{pricePerDay}
    </p>
    <br></br>
    <div>
      <span
        style={{
          display: "inline-flex",
          alignItems: "center",
          gap: "8px",
          marginRight: "16px",
        }}
      >
        
        <i className="fas fa-users" style={{ fontSize: "20px", color: "#555" }}></i>
        <p style={{ margin: "0" }}>{capacity ? `${capacity} seats` : " "}</p>
      </span>
      <span
        style={{
          display: "inline-flex",
          alignItems: "center",
          gap: "8px",
          marginRight: "16px",
        }}
      >
        <i className="fas fa-cogs" style={{ fontSize: "20px", color: "#555" }}></i>
        <p style={{ margin: "0" }}>{transmissionType}</p>
      </span>
      <span
        style={{
          display: "inline-flex",
          alignItems: "center",
          gap: "8px",
        }}
      >
        <i className="fas fa-gas-pump" style={{ fontSize: "20px", color: "#555" }}></i>
        <p style={{ margin: "0" }}>{fuelType}</p>
      </span>
     
    </div>
    
  </div>
 
</div>

  
          <div className="booking-form">
            <h3>SCHEDULE</h3>
            <form>
              <div className="date-time-picker-container" style={{ display: "flex", gap: "30px" }}>
                <div className="date-time-picker">
                  <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <DateTimePicker
                      label="Start Time"
                      value={startTime}
                      onChange={(newValue) => setStartTime(newValue)} 
                      renderInput={(params) => (
                        <TextField
                          {...params}
                          style={{
                            padding: "15px",
                            border: "1px solid #ccc",
                            borderRadius: "18px",
                            backgroundColor: "#f8f9fa",
                            fontSize: "16px",
                            width: "300px",
                            outline: "none",
                            color: startTime ? "#000" : "#adb5bd",
                            boxShadow: "none",
                            borderColor: startTimeValid ? "#ccc" : "red", 
                          }}
                        />
                      )}
                    />
                  </LocalizationProvider>
                </div>
  
                <div className="date-time-picker">
                  <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <DateTimePicker
                      label="End Time"
                      value={endTime}
                      onChange={(newValue) => setEndTime(newValue)} 
                      renderInput={(params) => (
                        <TextField
                          {...params}
                          style={{
                            padding: "15px",
                            border: "1px solid #ccc",
                            borderRadius: "18px",
                            backgroundColor: "#f8f9fa",
                            fontSize: "16px",
                            width: "300px",
                            outline: "none",
                            color: endTime ? "#000" : "#adb5bd",
                            boxShadow: "none",
                            borderColor: endTimeValid ? "#ccc" : "red", 
                          }}
                        />
                      )}
                    />
                  </LocalizationProvider>
                </div>
              </div>
  
              <button
                type="button"
                className="proceed-btn"
                style={{ marginTop: "40px" }}
                onClick={handleBookingClick}
              >
                Proceed
              </button>
            </form>
          </div>
        </div>
  
        {/* Important Points */}
        <div className="important-points">
          <h3>IMPORTANT POINTS TO REMEMBER</h3>
          <table>
            <tbody>
              <tr>
                <td>CHANGE IN PRICING PLAN:</td>
                <td>The pricing plan cannot be changed after the booking is made.</td>
              </tr>
              <tr>
                <td>FUEL:</td>
                <td>
                  In case you return the car at a lower fuel level than what was
                  received, a flat refueling service charge will apply.
                </td>
              </tr>
              <tr>
                <td>TOLLS, PARKING, INTER-STATE TAXES:</td>
                <td>To be paid by you.</td>
              </tr>
              <tr>
                <td>ID VERIFICATION:</td>
                <td>
                  Keep original or DigiLocker of driving license and ID proof for
                  verification. Missing documents will result in a late
                  cancellation fee.
                </td>
              </tr>
              <tr>
                <td>PRE-HANDOVER INSPECTION:</td>
                <td>
                  Please inspect the car thoroughly before approving the
                  checklist.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
  
        {/* Dialog for Profile Completion or Sign In */}
        <Dialog open={openDialog} onClose={handleCloseDialog}>
          <DialogTitle>{dialogMessage}</DialogTitle>
          <DialogContent>
            {user ? (
              <p>Would you like to update your profile details?</p>
            ) : (
              <p>Please sign in to proceed with the booking.</p>
            )}
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Cancel</Button>
            <Button
              onClick={() => {
                handleCloseDialog();
                if (user) {
                  // Navigate to update profile page or similar action
                  navigate("/profile");  // Example page to update profile
                } else {
                  navigate("/login"); // Navigate to login page if not logged in
                }
              }}
              color="primary"
            >
              Yes
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    );
  };
  

export default CarDescriptionPage;



