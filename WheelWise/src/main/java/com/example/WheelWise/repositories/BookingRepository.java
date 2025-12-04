package com.example.WheelWise.repositories;

import com.example.WheelWise.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {}
