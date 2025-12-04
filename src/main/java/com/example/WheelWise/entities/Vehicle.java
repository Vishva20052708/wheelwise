package com.example.WheelWise.entities;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;
    private String numberPlate;
    private String model; 
    private String type;
    private int capacity;
    private BigDecimal pricePerDay;
    private Year manufacturingYear;
    private Byte rating;
    
    private String location; // New field for vehicle location
    private String transmissionType; 
    private String fuelType;
    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Image> images=new ArrayList<>();
    @ElementCollection
    private List<String> imagePaths=new ArrayList<>();
    public List<String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

	public String getTransmissionType() {
		return transmissionType;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public void setType(String type) {
		this.type = type;
	}

	  @Column(length = 500) // Optional: specify the maximum length for the description
	    private String description; // New field for description

	    // Getters and Setters
	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }
    // Default constructor
    public Vehicle() {}

   public Vehicle(String companyName, String numberPlate, String model, String type, int capacity,
            BigDecimal pricePerDay, Year manufacturingYear, Byte rating, String location,
            String transmissionType, String fuelType, String description) {
 this.companyName = companyName;
 this.numberPlate = numberPlate;
 this.model = model;
 this.type = type;
 this.capacity = capacity;
 this.pricePerDay = pricePerDay;
 this.manufacturingYear = manufacturingYear;
 this.rating = rating;
 this.location = location;
 this.transmissionType = transmissionType;
 this.fuelType = fuelType;
 this.description = description;
}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

  
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Year getManufacturingYear() {
        return manufacturingYear;
    }

    public void setManufacturingYear(Year manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }

    public Byte getRating() {
        return rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

   
}
