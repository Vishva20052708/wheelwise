package com.example.WheelWise.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.WheelWise.entities.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Correct query for calculating the average rating of a vehicle
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.booking.vehicle.id = :vehicleId")
    Double findAverageRatingByVehicleId(@Param("vehicleId") Long vehicleId);

    // Correct query for counting distinct bookings associated with a vehicle
    @Query("SELECT COUNT(DISTINCT f.booking.id) FROM Feedback f WHERE f.booking.vehicle.id = :vehicleId")
    int countByVehicleId(@Param("vehicleId") Long vehicleId);
    
    @Query("SELECT f FROM Feedback f WHERE f.booking.vehicle.id = :vehicleId")
    List<Feedback> findByVehicleId(@Param("vehicleId") Long vehicleId);

}