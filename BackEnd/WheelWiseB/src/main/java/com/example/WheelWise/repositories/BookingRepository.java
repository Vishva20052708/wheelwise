//package com.example.WheelWise.repositories;
//
//import com.example.WheelWise.entities.Booking;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface BookingRepository extends JpaRepository<Booking, Long> {}
//
//













package com.example.WheelWise.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.WheelWise.entities.Booking;
import com.example.WheelWise.entities.Vehicle;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.id = :bookingId AND b.user.id = :userId")
    Optional<Booking> findByUserIdAndBookingId(@Param("userId") Long userId, @Param("bookingId") Long bookingId);
   // List<Booking> findByVehicle(Vehicle vehicle); // Fetch bookings for a specific vehicle
}

