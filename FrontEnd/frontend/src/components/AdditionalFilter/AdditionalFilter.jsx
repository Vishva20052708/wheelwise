import React from "react";
import { FaTimesCircle } from "react-icons/fa";
import './AdditionalFilter.css';

const AdditionalFilter = ({
  searchBrand,
  setSearchBrand,
  priceRange,
  handlePriceChange,
  brands,
  selectedBrands,
  handleCheckboxChange,
  transmissions,
  selectedTransmission,
  fuels,
  selectedFuel,
  handleClearFilters,
}) => {
  // Split the brands array into two halves (7 on left, 7 on right)
  const leftBrands = brands.slice(0, 7);
  const rightBrands = brands.slice(7, 14); // Ensure no overflow

  return (
    <div className="additional-filters">
      <div className="price-range-container">
        <label>Price Range / Day</label>
        <select
          value={priceRange.min}
          onChange={(e) => handlePriceChange("min", e.target.value)}
          style={{ marginRight: '10px' }} // Add margin to separate the dropdowns
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

      {/* Brands Section */}
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

      {/* Transmission Section */}
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

      {/* Fuel Section */}
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
  );
};

export default AdditionalFilter;
