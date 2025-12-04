package com.example.WheelWise.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.WheelWise.entities.Booking;
import com.example.WheelWise.entities.Feedback;
import com.example.WheelWise.repositories.BookingRepository;
import com.example.WheelWise.repositories.FeedbackRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository repository;
    @Autowired
    private BookingRepository bookingRepository;

    public Feedback saveFeedback(Feedback feedback) {
       
        return repository.save(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return repository.findAll();
    }

    public List<Feedback> getFeedbackByVehicleId(Long vehicleId) {
        return repository.findByVehicleId(vehicleId);
    }

    public ResponseEntity<Map<String, String>> saveFeedback(Feedback feedback, Long bookingId) {
        // Fetch the Booking entity from the database
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);

        // If the booking is not found, return a JSON response with a message
        if (bookingOptional.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Booking not found with ID: " + bookingId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 404 Not Found
        }

        // Set the Booking object in the Feedback entity
        feedback.setBooking(bookingOptional.get());

        // Save the Feedback entity
        repository.save(feedback);

        // Return success response
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Feedback saved successfully!"+bookingId);
        return ResponseEntity.ok(successResponse); // 200 OK
    }
    public Double getAverageRatingForVehicle(Long vehicleId) {
        // Replace with your actual repository or query
        return repository.findAverageRatingByVehicleId(vehicleId);
    }
    public int getCustomerCountForVehicle(Long vehicleId) {
        return repository.countByVehicleId(vehicleId);
    }


}
