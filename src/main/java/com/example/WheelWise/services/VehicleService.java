package com.example.WheelWise.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.WheelWise.entities.Vehicle;
import com.example.WheelWise.repositories.VehicleRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    public List<Vehicle> findAvailableVehiclesWithFilters(
            String location,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String type,
            String companyName,
            String fuelType,
            String transmissionType,
            Integer numofseats,
            Double minPrice,
            Double maxPrice
    ) {
        // Start by retrieving all available vehicles
        List<Vehicle> vehicles = vehicleRepository.findAllAvailableVehicles();

        // Apply filters based on selected parameters
        return vehicles.stream()
                .filter(vehicle -> (location == null || location.isEmpty() || vehicle.getLocation().equalsIgnoreCase(location)))
                .filter(vehicle -> (type == null || type.isEmpty() || vehicle.getType().equalsIgnoreCase(type)))
                .filter(vehicle -> (companyName == null || companyName.isEmpty() || vehicle.getCompanyName().equalsIgnoreCase(companyName)))
                .filter(vehicle -> (fuelType == null || fuelType.isEmpty() || vehicle.getFuelType().equalsIgnoreCase(fuelType)))
                .filter(vehicle -> (transmissionType == null || transmissionType.isEmpty() || vehicle.getTransmissionType().equalsIgnoreCase(transmissionType)))
                .filter(vehicle -> (numofseats == null || vehicle.getCapacity() == numofseats))
                .filter(vehicle -> (startTime == null || endTime == null || isVehicleAvailable(vehicle, startTime, endTime)))
                .filter(vehicle -> (minPrice == null || isPriceInRange(vehicle.getPricePerDay(), minPrice, true)))  // Filter by min price
                .filter(vehicle -> (maxPrice == null || isPriceInRange(vehicle.getPricePerDay(), maxPrice, false)))  // Filter by max price
                .collect(Collectors.toList());
    }

    private boolean isPriceInRange(BigDecimal pricePerDay, Double price, boolean isMinPrice) {
        if (price == null) {
            return true; // No price filter applied
        }
        
        // Convert Double to BigDecimal for comparison
        BigDecimal priceToCompare = BigDecimal.valueOf(price);
        
        if (isMinPrice) {
            // Check if pricePerDay >= minPrice
            return pricePerDay.compareTo(priceToCompare) >= 0;
        } else {
            // Check if pricePerDay <= maxPrice
            return pricePerDay.compareTo(priceToCompare) <= 0;
        }
    }
    private boolean isVehicleAvailable(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime) {
        // Implement the availability logic to check if the vehicle is available between startTime and endTime
        return true; // Placeholder for actual availability logic
    }


    public Vehicle findByCompanyNameAndNumberPlate(String companyName, String numberPlate) {
        return vehicleRepository.findByCompanyNameAndNumberPlate(companyName, numberPlate);
    }


    public void saveVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }
    
    
    
    public List<Vehicle> findAvailableVehiclesWithFilters(
            String location,
            LocalDateTime startTime,
            LocalDateTime endTime,
            List<String> type, // Accept multiple types
            List<String> companyName, // Accept multiple companies
            List<String> fuelType, // Accept multiple fuel types
            List<String> transmissionType, // Accept multiple transmission types
            List<Integer> numofseats, // Accept multiple seat numbers
            List<Double> minPrice, // Accept multiple min prices
            List<Double> maxPrice // Accept multiple max prices
    ) {
        // Assuming you have a repository or data source where vehicles are stored
        List<Vehicle> vehicles = vehicleRepository.findAll(); // Or your method of fetching vehicles
        
        return vehicles.stream()
                .filter(vehicle -> (startTime == null || endTime == null) || isVehicleAvailable(vehicle, startTime, endTime)) // Check availability if both are provided
                .filter(vehicle -> location == null || vehicle.getLocation().equalsIgnoreCase(location)) // Filter by location
                .filter(vehicle -> type == null || type.isEmpty() || type.contains(vehicle.getType())) // Filter by multiple types
                .filter(vehicle -> companyName == null || companyName.isEmpty() || companyName.contains(vehicle.getCompanyName())) // Filter by multiple companies
                .filter(vehicle -> fuelType == null || fuelType.isEmpty() || fuelType.contains(vehicle.getFuelType())) // Filter by multiple fuel types
                .filter(vehicle -> transmissionType == null || transmissionType.isEmpty() || transmissionType.contains(vehicle.getTransmissionType())) // Filter by multiple transmission types
                .filter(vehicle -> numofseats == null || numofseats.isEmpty() || numofseats.contains(vehicle.getCapacity())) // Filter by multiple seat counts
                .filter(vehicle -> minPrice == null || minPrice.isEmpty() || minPrice.stream().anyMatch(price -> vehicle.getPricePerDay().compareTo(BigDecimal.valueOf(price)) >= 0)) // Filter by multiple min prices
                .filter(vehicle -> maxPrice == null || maxPrice.isEmpty() || maxPrice.stream().anyMatch(price -> vehicle.getPricePerDay().compareTo(BigDecimal.valueOf(price)) <= 0)) // Filter by multiple max prices
                .collect(Collectors.toList());
    }
}