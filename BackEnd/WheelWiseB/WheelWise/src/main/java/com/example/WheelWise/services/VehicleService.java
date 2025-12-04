package com.example.WheelWise.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.WheelWise.entities.Vehicle;
import com.example.WheelWise.repositories.VehicleRepository;

@Service
public class VehicleService {

	@Autowired
	 private  VehicleRepository vehicleRepository;

	
	 public List<Vehicle> getAllVehicles() {
	        return vehicleRepository.findAll();
	    }
	 public List<Vehicle> findAvailableVehicles(String location, LocalDateTime startTime, LocalDateTime endTime) {
		    if (location == null && startTime == null && endTime == null) {
		        // Fetch all available vehicles
		        return vehicleRepository.findAllAvailableVehicles();
		    } else if (location != null && startTime == null && endTime == null) {
		        // Fetch vehicles available at the specified location
		        return vehicleRepository.findAvailableVehiclesByLocation(location);
		    } else {
		        // Fetch vehicles by location and/or time range as applicable
		        return vehicleRepository.findAvailableVehicles(location, startTime, endTime);
		    }
		}

}
