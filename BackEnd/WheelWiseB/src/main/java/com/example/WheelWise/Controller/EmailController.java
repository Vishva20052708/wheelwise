package com.example.WheelWise.Controller;

import com.example.WheelWise.entities.Booking;
import com.example.WheelWise.entities.User;
import com.example.WheelWise.entities.Vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import com.example.WheelWise.repositories.*;
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendEmail(@RequestParam Long bookingId) {
        Map<String, String> response = new HashMap<>();
        
        try {
            // Fetch the booking details
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new Exception("Booking not found"));
            Vehicle vehicle = booking.getVehicle();
            User user = booking.getCustomer();

            // Prepare email content
            String subject = "Booking Confirmation: " + vehicle.getModel();
            String message = String.format(
                "Booking Details:\n\n" +
                "Car Model: %s\n" +
                "Number Plate: %s\n" +
                "Price Per Day: Rs%.2f\n" +
                "Total Fare: Rs%.2f\n" +
                "Location: %s\n" +
                "Start Date: %s\n" +
                "End Date: %s\n" +
                "Booking Status: %s\n\n" +
                "Thank you for choosing our service.",
                vehicle.getModel(),
                vehicle.getNumberPlate(),
                vehicle.getPricePerDay(),
                booking.getTotalPrice(),
                vehicle.getLocation(),
                booking.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                booking.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                booking.getStatus()
            );

            // Send email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            mailSender.send(mailMessage);

            // Prepare success response
            response.put("status", "success");
            response.put("message", "Email sent successfully to " + user.getEmail());
        } catch (Exception e) {
            // Prepare error response
            response.put("status", "error");
            response.put("message", "Error while sending email: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}
