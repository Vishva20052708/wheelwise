import React, { useState, useEffect } from "react";
import { FaTimesCircle } from "react-icons/fa";
import Filter from '../../components/Filter/Filter';
import CarCard from '../../components/card/CarCard';
import './HomePage.css';

const HomePage = () => {
  const [location, setLocation] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [searchBrand, setSearchBrand] = useState("");
  const [priceRange, setPriceRange] = useState({ min: "", max: "" });
  const [selectedBrands, setSelectedBrands] = useState([]);
  const [selectedTransmission, setSelectedTransmission] = useState([]);
  const [selectedFuel, setSelectedFuel] = useState([]);
  const [cars, setCars] = useState([]); // State to store car data

  const brands = [
    "Toyota", "Honda", "Ford", "Chevrolet", "Nissan", "Hyundai", "Kia",
    "Volkswagen", "BMW", "Buick", "Ram", "Mercedes", "Audi", "Lincoln"
  ];
  const transmissions = ["Manual", "Automatic"];
  const fuels = ["Petrol", "Diesel", "Electric"];

  useEffect(() => {
    const currentDateTime = new Date()
      .toLocaleString("sv-SE", { timeZone: "UTC" })
      .slice(0, 16);
    setStartTime(currentDateTime);
    setEndTime(currentDateTime);
  }, []);

  const handleStartTimeChange = (newValue) => {
    setStartTime(newValue);
    const newEndTime = new Date(new Date(newValue).getTime() + 30 * 60000)
      .toLocaleString("sv-SE", { timeZone: "UTC" })
      .slice(0, 16);
    setEndTime(newEndTime);
  };

  const handleCheckboxChange = (type, value) => {
    const updateState = (prev) =>
      prev.includes(value) ? prev.filter((item) => item !== value) : [...prev, value];

    if (type === "brand") setSelectedBrands(updateState);
    else if (type === "transmission") setSelectedTransmission(updateState);
    else if (type === "fuel") setSelectedFuel(updateState);
  };

  const handleClearFilters = () => {
    setSearchBrand("");
    setPriceRange({ min: "", max: "" });
    setSelectedBrands([]);
    setSelectedTransmission([]);
    setSelectedFuel([]);
  };

  const handlePriceChange = (type, value) => {
    setPriceRange((prev) => ({
      ...prev,
      [type]: value,
    }));
  };

  const handleSearch = async () => {
    const queryParams = new URLSearchParams();
    if (location) queryParams.append("location", location);
    if (priceRange.min) queryParams.append("minPrice", priceRange.min);
    if (priceRange.max) queryParams.append("maxPrice", priceRange.max);
    if (selectedBrands.length > 0) queryParams.append("companyName", selectedBrands.join(","));
    if (selectedTransmission.length > 0) queryParams.append("transmissionType", selectedTransmission.join(","));
    if (selectedFuel.length > 0) queryParams.append("fuelTypes", selectedFuel.join(","));
    if (startTime) queryParams.append("startTime", new Date(startTime).toISOString());
    if (endTime) queryParams.append("endTime", new Date(endTime).toISOString());

    const apiUrl = `http://localhost:8080/available?${queryParams.toString()}`;

    try {
      const response = await fetch(apiUrl, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const data = await response.json();
      setCars(data); // Set fetched car data to state
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  // Split brands for layout
  const leftBrands = brands.slice(0, 7);
  const rightBrands = brands.slice(7, 14);

  return (
    <div className="home-page">
      {/* Filters Section */}
      <div className="filters-section">
        <Filter
          location={location}
          setLocation={setLocation}
          startTime={startTime}
          setStartTime={setStartTime}
          endTime={endTime}
          setEndTime={setEndTime}
          handleStartTimeChange={handleStartTimeChange}
          handleSearch={handleSearch}
        />

        <div className="additional-filters">
          <div className="price-range-container">
            <label>Price Range / Day</label>
            <select
              value={priceRange.min}
              onChange={(e) => handlePriceChange("min", e.target.value)}
              style={{ marginRight: '10px' }}
            >
              <option value="">Min</option>
              <option value="1000">Rs 1000</option>
              <option value="2000">Rs 2000</option>
              <option value="3000">Rs 3000</option>
              <option value="5000">Rs 5000</option>
            </select>
            <select
              value={priceRange.max}
              onChange={(e) => handlePriceChange("max", e.target.value)}
            >
              <option value="">Max</option>
              <option value="5000">Rs 5000</option>
              <option value="7000">Rs 7000</option>
              <option value="10000">Rs 10000</option>
            </select>
          </div>

          <div className="brands-container">
            <div className="brands-column">
              <label>Brands</label>
              {leftBrands.map((brand) => (
                <div key={brand}>
                  <input
                    type="checkbox"
                    checked={selectedBrands.includes(brand)}
                    onChange={() => handleCheckboxChange("brand", brand)}
                  />
                  {brand}
                </div>
              ))}
            </div>
            <div className="brands-column">
              {rightBrands.map((brand) => (
                <div key={brand}>
                  <input
                    type="checkbox"
                    checked={selectedBrands.includes(brand)}
                    onChange={() => handleCheckboxChange("brand", brand)}
                  />
                  {brand}
                </div>
              ))}
            </div>
          </div>

          <div>
            <label>Transmission</label>
            {transmissions.map((type) => (
              <div key={type}>
                <input
                  type="checkbox"
                  checked={selectedTransmission.includes(type)}
                  onChange={() => handleCheckboxChange("transmission", type)}
                />
                {type}
              </div>
            ))}
          </div>

          <div>
            <label>Fuel</label>
            {fuels.map((type) => (
              <div key={type}>
                <input
                  type="checkbox"
                  checked={selectedFuel.includes(type)}
                  onChange={() => handleCheckboxChange("fuel", type)}
                />
                {type}
              </div>
            ))}
          </div>

          <button className="clear-filters" onClick={handleClearFilters}>
            <FaTimesCircle /> Clear Filters
          </button>
        </div>
      </div>

      {/* Car List */}
      <div className="car-list">
        {cars.map((car) => (
          <CarCard
            key={car.id}
            model={car.model}
            companyName={car.companyName}
            fuelType={car.fuelType}
            transmissionType={car.transmissionType}
            location={car.location}
            pricePerDay={car.pricePerDay}
            rating={car.rating}
            type={car.type}
            manufacturingYear={car.manufacturingYear}
            capacity={car.capacity}
            imagePath={car.imagePaths[0]}
          />
        ))}
      </div>
    </div>
  );
};

export default HomePage;
