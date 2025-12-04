

package com.example.WheelWise.services;

import com.example.WheelWise.entities.Booking;
import com.example.WheelWise.entities.User;
import com.example.WheelWise.entities.Vehicle;
import com.example.WheelWise.repositories.BookingRepository;
import com.example.WheelWise.repositories.UserRepository;
import com.example.WheelWise.repositories.VehicleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private UserRepository userRepository;
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

//   
    public Booking createBooking(Long vehicleId, Integer userId, LocalDateTime startDate, LocalDateTime endDate) {
        // Fetch Vehicle from the database using the provided vehicle ID
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle ID"));

        // Fetch User from the database using the provided user ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // Calculate the total price based on pricePerDay and booking duration
        long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        if (days <= 0) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        BigDecimal totalPrice = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(days));

        // Create a new Booking entity
        Booking booking = new Booking();
        booking.setVehicle(vehicle);
        booking.setCustomer(user);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);// Default status
        booking.setCreatedAt(LocalDateTime.now());
        booking.setModifiedAt(LocalDateTime.now());

        // Save the booking to the database
        return bookingRepository.save(booking);
    }
    
    public Booking updateBooking(Long id, Long vehicleId, Integer userId, LocalDateTime startDate, LocalDateTime endDate, Booking.BookingStatus status) {
        // Fetch Vehicle from the database using the provided vehicle ID
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle ID"));

        // Fetch User from the database using the provided user ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // Calculate the total price based on pricePerDay and booking duration
        long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        if (days <= 0) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        BigDecimal totalPrice = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(days));

        // Fetch the existing booking from the database using the provided booking ID
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id " + id));

        // Update the booking details with the new data
        existingBooking.setVehicle(vehicle);
        existingBooking.setCustomer(user);
        existingBooking.setStartDate(startDate);
        existingBooking.setEndDate(endDate);
        existingBooking.setTotalPrice(totalPrice);
        existingBooking.setStatus(status); // Set the status provided in the request
        existingBooking.setModifiedAt(LocalDateTime.now());  // Set current time as modified time

        // Save the updated booking to the database
        return bookingRepository.save(existingBooking);
    }


    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

}

