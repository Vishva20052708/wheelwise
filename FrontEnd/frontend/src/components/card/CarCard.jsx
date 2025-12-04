import React from "react";
import { useNavigate } from "react-router-dom";
import "./card.css";

const CarCard = ({
  id,
  model,
  companyName,
  fuelType,
  transmissionType,
  location,
  pricePerDay,
  rating,
  type,
  manufacturingYear,
  capacity,
  imagePath , 
  startTime = null,
  endTime = null,
}) => {
  const navigate = useNavigate();

  const handleCardClick = () => {
    navigate(`/car/${id}`, {
      state: {
        id,
        model,
        companyName,
        fuelType,
        transmissionType,
        location,
        pricePerDay,
        rating,
        type,
        manufacturingYear,
        capacity,
        startTime,
        endTime,
        imagePath,
      },
    });
  };

  return (
    <div className="card" onClick={handleCardClick}>
      <img
        src={`http://localhost:8080/images?imagePath=${imagePath}`} // Use the provided URL with the image path
        alt={model}
        className="card-image"
      />
      <div className="card-content">
        <div className="card-rating">
          <span className="rating-value">{rating} ★</span>
          <span className="rating-trips">| {location}</span>
        </div>
        <div>
          <h3 className="card-title" style={{ display: "inline" }}>
            {companyName} {model}
          </h3>
          <span style={{ display: "inline", marginLeft: "10px" }}>{type}</span>
        </div>
        <br />
        <div className="card-details">
          <span>{transmissionType}</span> • <span>{fuelType}</span> • <span>{capacity} seats</span>
        </div>
        <div className="card-price">
          <span>₹{pricePerDay}/day</span>
          <span className="price-exclude"> inclusive tax</span>
        </div>
        <hr />
      </div>
    </div>
  );
};

export default CarCard;
