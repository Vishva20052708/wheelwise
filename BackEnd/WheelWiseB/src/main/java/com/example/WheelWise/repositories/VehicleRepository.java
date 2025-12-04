package com.example.WheelWise.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.WheelWise.entities.Vehicle;
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	@Query("SELECT v FROM Vehicle v")
	List<Vehicle> findAllAvailableVehicles();
	Vehicle findByCompanyNameAndNumberPlate(String companyName, String numberPlate);
}