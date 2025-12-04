package com.example.WheelWise.Controller;

import com.example.WheelWise.entities.Booking;
import com.example.WheelWise.entities.BookingRequest;
import com.example.WheelWise.entities.User;
import com.example.WheelWise.entities.Vehicle;
import com.example.WheelWise.repositories.BookingRepository;
import com.example.WheelWise.repositories.UserRepository;
import com.example.WheelWise.repositories.VehicleRepository;
import com.example.WheelWise.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/{userId}/booking/{bookingId}")
    public ResponseEntity<?> getBookingByUserIdAndBookingId(@PathVariable Long userId, @PathVariable Long bookingId) {
        // Use the custom query method to find the booking
        Optional<Booking> bookingOptional = bookingRepository.findByUserIdAndBookingId(userId, bookingId);

        // If the booking is not found, return an error response
        if (bookingOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("message", "Booking with ID " + bookingId + " does not belong to user ID " + userId + "."));
        }

        // Return the booking details if found
        return ResponseEntity.ok(bookingOptional.get());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBookingsByUserId(@PathVariable Long userId) {
        try {
            // Fetch all bookings for the given userId
            List<Booking> bookings = bookingRepository.findByUserId(userId);

            if (bookings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(Map.of("message", "No bookings found for user ID " + userId + "."));
            }

            // Return the list of bookings
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("message", "An error occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/update")
    public Booking createBooking(@RequestBody BookingRequest bookingRequest) {
        return bookingService.createBooking(
                bookingRequest.getVehicleId(),
                bookingRequest.getUserId(),
                bookingRequest.getStartDate(),
                bookingRequest.getEndDate()
        );
    }

    @PutMapping("/{id}/update")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        // Fetch the existing Booking entity from the database
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));

        // Update the fields of the existing booking with the new values from the request body
        if (booking.getVehicle() != null) {
            Vehicle vehicle = vehicleRepository.findById(booking.getVehicle().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle not found."));
            existingBooking.setVehicle(vehicle);
        }

        if (booking.getCustomer() != null) {
            User user = userRepository.findById(booking.getCustomer().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found."));
            existingBooking.setCustomer(user);
        }

        if (booking.getStartDate() != null) {
            existingBooking.setStartDate(booking.getStartDate());
        }

        if (booking.getEndDate() != null) {
            existingBooking.setEndDate(booking.getEndDate());
        }

        if (booking.getTotalPrice() != null) {
            existingBooking.setTotalPrice(booking.getTotalPrice());
        }

        if (booking.getStatus() != null) {
            existingBooking.setStatus(booking.getStatus());
        }

        // Update the modification timestamp
        existingBooking.setModifiedAt(LocalDateTime.now());

        // Save the updated booking to the database
        return bookingRepository.save(existingBooking);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        // Check if the booking exists
        Optional<Booking> bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isEmpty()) {
            // If booking is not found, return a not found message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "Booking with ID " + id + " not found."));
        }

        // Get the booking object
        Booking booking = bookingOptional.get();

        // Update the booking status to "CANCELLED"
        booking.setStatus("CANCELLED");

        // Save the updated booking back to the database
        bookingRepository.save(booking);

        // Return a success message in JSON format
        return ResponseEntity.ok().body(Map.of("message", "Booking cancelled successfully."));
    }


}
