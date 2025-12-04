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

	@Query("SELECT v FROM Vehicle v WHERE v.id NOT IN (SELECT b.vehicle.id FROM Booking b WHERE b.status = 'CONFIRMED')")
	List<Vehicle> findAllAvailableVehicles();
	@Query("SELECT v FROM Vehicle v WHERE v.location = :location AND v.id NOT IN (SELECT b.vehicle.id FROM Booking b WHERE b.status = 'CONFIRMED')")
	List<Vehicle> findAvailableVehiclesByLocation(@Param("location") String location);
	@Query("SELECT v FROM Vehicle v WHERE (:location IS NULL OR v.location = :location) " +
		       "AND v.id NOT IN (SELECT b.vehicle.id FROM Booking b WHERE b.startDate < :endTime AND b.endDate > :startTime AND b.status = 'CONFIRMED')")
		List<Vehicle> findAvailableVehicles(@Param("location") String location,
		                                    @Param("startTime") LocalDateTime startTime,
		                                    @Param("endTime") LocalDateTime endTime);

}
