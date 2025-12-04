package com.example.WheelWise.entities;

import java.math.BigDecimal;
import java.time.Year;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Image> images=new ArrayList<>();

    @Column(name = "company_name")
    private String companyName;
    private String numberPlate;
    private String model;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    private int capacity;
    private BigDecimal pricePerDay;
    private Year manufacturingYear;
    private Byte rating;
    
    private String location; // New field for vehicle location'
    @ElementCollection
    private List<String> imagePaths=new ArrayList<>();

    // Default constructor
    public Vehicle() {}

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setType(com.example.WheelWise.entities.Vehicle.VehicleType type) {
        this.type = type;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    // Parameterized constructor
    public Vehicle(String companyName,List<Image> images, String numberPlate, String model, VehicleType type, int capacity,
                   BigDecimal pricePerDay, Year manufacturingYear, Byte rating, String location) {
        this.companyName = companyName;
        this.numberPlate = numberPlate;
        this.image=images;
        this.model = model;
        this.type = type;
        this.capacity = capacity;
        this.pricePerDay = pricePerDay;
        this.manufacturingYear = manufacturingYear;
        this.rating = rating;
        this.location = location;
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

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
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

    // Enum for Vehicle Type
    public enum VehicleType {
        FORD, AUDI, FERRARI, SEDAN
    }
}
