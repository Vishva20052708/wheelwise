import React, { useState } from "react";
import { FaMapMarkerAlt } from "react-icons/fa";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { DateTimePicker } from "@mui/x-date-pickers";
import "./Filter.css";

const Filter = ({
  location,
  setLocation,
  startTime,
  setStartTime,
  endTime,
  setEndTime,
  handleSearch,
}) => {
  const [showLocationModal, setShowLocationModal] = useState(false);

  const handleLocationSelect = (selectedLocation) => {
    setLocation(selectedLocation);
    setShowLocationModal(false); // Close the modal
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDateFns}>
      <div className="filter-container">
      <div className="filter-header">
  {/* Location Selector */}
  <div className="location-selector">
    <button
      onClick={() => setShowLocationModal(true)}
      className="location-button"
    >
      {location || "Select Location"}
      <FaMapMarkerAlt className="location-icon" />
    </button>
  </div>

  {/* Start Time Selector */}
  <div className="date-time-picker">
    <DateTimePicker
      label="Start Time"
      value={startTime}
      onChange={(newValue) => setStartTime(newValue)}
      renderInput={(params) => (
        <input
          {...params}
          style={{
            padding: "15px", // Increased padding
            border: "1px solid #ccc",
            borderRadius: "18px",
            backgroundColor: "#f8f9fa",
            fontSize: "16px",
            width: "300px",
            outline: "none",
            color: startTime ? "#000" : "#adb5bd",
            boxShadow: "none",
            marginRight: "20px", // Add spacing between inputs
          }}
        />
      )}
    />
  </div>

  {/* End Time Selector */}
  <div className="date-time-picker">
    <DateTimePicker
      label="End Time"
      value={endTime}
      onChange={(newValue) => setEndTime(newValue)}
      minDateTime={startTime}
      renderInput={(params) => (
        <input
          {...params}
          style={{
            padding: "15px",
            border: "1px solid #ccc",
            borderRadius: "8px",
            backgroundColor: "#f8f9fa",
            fontSize: "16px",
            width: "300px",
            outline: "none",
            color: endTime ? "#000" : "#adb5bd",
            boxShadow: "none",
            marginRight: "20px", // Add spacing between inputs
          }}
        />
      )}
    />
  </div>

  {/* Search Button */}
  <button onClick={handleSearch} className="search-button">
    Search
  </button>
</div>


        {/* Location Modal */}
        {showLocationModal && (
          <div
            className="location-modal"
            onClick={() => setShowLocationModal(false)} // Close modal when clicking outside
          >
            <div
              className="modal-content"
              onClick={(e) => e.stopPropagation()} // Prevent click events inside the modal from propagating
            >
              <h3>Select City</h3>
              <ul className="location-list">
                {[
                  "Hyderabad",
                  "Bangalore",
                  "Mumbai",
                  "Delhi",
                  "Chennai",
                  "Kolkata",
                  "Pune",
                  "Ahmedabad",
                  "Jaipur",
                  "Lucknow",
                ].map((city) => (
                  <li
                    key={city}
                    onClick={() => handleLocationSelect(city)}
                    className={`location-item ${
                      location === city ? "selected" : ""
                    }`}
                  >
                    {city}
                  </li>
                ))}
              </ul>
            </div>
          </div>
        )}
      </div>
    </LocalizationProvider>
  );
};

export default Filter;
