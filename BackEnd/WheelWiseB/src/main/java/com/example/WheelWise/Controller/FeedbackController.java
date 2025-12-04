package com.example.WheelWise.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.WheelWise.entities.Feedback;
import com.example.WheelWise.services.FeedbackService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService service;

    // POST API to store feedback
    @PostMapping
    public ResponseEntity<Map<String, String>> saveFeedback(@RequestBody Map<String, Object> feedbackData) {
        // Extract the BOOKING_ID from the request payload
        Long bookingId = ((Number) feedbackData.get("BOOKING_ID")).longValue();

        // Create a Feedback entity and populate fields
        Feedback feedback = new Feedback();
        feedback.setRating((Integer) feedbackData.get("rating"));
        feedback.setExperience((String) feedbackData.get("experience"));
        feedback.setBookingEasy((Boolean) feedbackData.get("bookingEasy"));
        feedback.setCarCondition((Boolean) feedbackData.get("carCondition"));
        feedback.setSupportHelpful((Boolean) feedbackData.get("supportHelpful"));
        feedback.setRecommend((Boolean) feedbackData.get("recommend"));
        
        // Add the new comfort and timeliness fields
        feedback.setComfort((String) feedbackData.get("comfort"));
        feedback.setTimeliness((String) feedbackData.get("timeliness"));

        // Save feedback with the booking
         return  service.saveFeedback(feedback, bookingId);       
    }


    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<Feedback>> getFeedbackByVehicleId(@PathVariable Long vehicleId) {
        List<Feedback> feedbackList = service.getFeedbackByVehicleId(vehicleId);
        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/vehicle/{vehicleId}/average-rating")
    public ResponseEntity<Map<String, Object>> getAverageRatingForVehicle(@PathVariable Long vehicleId) {
        Double averageRating = service.getAverageRatingForVehicle(vehicleId);
        if (averageRating == null) {
            averageRating = 0.0;
        }
        int customerCount = service.getCustomerCountForVehicle(vehicleId);
        Map<String, Object> response = new HashMap<>();
        response.put("averageRating", Math.round(averageRating * 10.0) / 10.0); // Rounded to one decimal
        response.put("customerCount", customerCount); // Number of customers who rated
        return ResponseEntity.ok(response);
    }

}
