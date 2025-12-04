package com.example.WheelWise.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.WheelWise.entities.Image;
import com.example.WheelWise.entities.Vehicle;
import com.example.WheelWise.services.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class VehicleController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;

    // Endpoint to get all vehicles
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    // Endpoint to get available vehicles based on filters and requested time range
    @GetMapping("/available")
    public ResponseEntity<List<Vehicle>> getAvailableVehicles(
            @RequestParam(required = false) String location, // location remains a single value
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) List<String> type, // Accept multiple types
            @RequestParam(required = false) List<String> companyName, // Accept multiple companies
            @RequestParam(required = false) List<String> fuelType, // Accept multiple fuel types
            @RequestParam(required = false) List<String> transmissionType, // Accept multiple transmission types
            @RequestParam(required = false) List<Integer> numofseats, // Accept multiple seat numbers
            @RequestParam(required = false) List<Double> minPrice, // Accept multiple min prices
            @RequestParam(required = false) List<Double> maxPrice // Accept multiple max prices
    ) {
        List<Vehicle> availableVehicles = vehicleService.findAvailableVehiclesWithFilters(
                location, startTime, endTime, type, companyName, fuelType, transmissionType, numofseats, minPrice, maxPrice
        );
        return ResponseEntity.ok(availableVehicles);
    }

    @PostMapping("/addVehicle")
    public ResponseEntity<String> createVehicle(@RequestParam("vehicleData") String vehicleData,
                                                @RequestParam("image") MultipartFile[] images) {
        ObjectMapper objectMapper = new ObjectMapper();
        Vehicle vehicle;

        try {
            // Deserialize vehicleData to a Vehicle object
            vehicle = objectMapper.readValue(vehicleData, Vehicle.class);
        } catch (IOException e) {
            logger.error("Error parsing vehicle data: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid vehicle data");
        }

        // Retrieve the vehicle from the database based on companyName and numberPlate
        String companyName = vehicle.getCompanyName();
        String numberPlate = vehicle.getNumberPlate();
        Vehicle existingVehicle = vehicleService.findByCompanyNameAndNumberPlate(companyName, numberPlate);

        if (existingVehicle == null) {
            logger.error("Vehicle not found in database for companyName: {} and numberPlate: {}", companyName, numberPlate);
            return ResponseEntity.badRequest().body("Vehicle not found in database");
        }

        // Add image data to the retrieved vehicle
        for (MultipartFile image : images) {
            String imagePath = saveImageToFile(image); // Save image and get the file path
            if (imagePath != null) {
                // Add the image path to the vehicle's image paths
                existingVehicle.getImagePaths().add(imagePath);

                // Create an Image object and associate it with the existing vehicle
                Image vehicleImage = new Image();
                vehicleImage.setImagePath(imagePath);
                vehicleImage.setVehicle(existingVehicle); // This links the image to the vehicle
                existingVehicle.getImages().add(vehicleImage);
            } else {
                logger.error("Failed to save image: {}", image.getOriginalFilename());
            }
        }

        // Save the updated vehicle with images
        vehicleService.saveVehicle(existingVehicle);

        // Retrieve the vehicleId
        vehicleService.saveVehicle(vehicle);
        return ResponseEntity.ok("Vehicle Added");
    }

    private String saveImageToFile(MultipartFile image) {
        // Define the upload directory (adjust this to your server's path)
        String uploadDir = "WheelWise-3/Wheelwise/imagedir";
        Path uploadPath = Paths.get(uploadDir);

        // Ensure the directory exists or create it
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                logger.error("Failed to create directory: {}", e.getMessage());
                return null;
            }
        }

        // Generate a unique filename using a timestamp
        String originalFilename = image.getOriginalFilename();
        String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
        Path imagePath = uploadPath.resolve(uniqueFilename);

        // Save the file
        try {
            Files.write(imagePath, image.getBytes());
            return imagePath.toString(); // Return the full path as a string
        } catch (IOException e) {
            logger.error("Failed to save image: {}", e.getMessage());
            return null;
        }
    }


    @GetMapping("/images")
    public ResponseEntity<byte[]> getImageByPath(@RequestParam("imagePath") String imagePath) {
        try {
            logger.info("Received request to fetch image at path: {}", imagePath);

            Path path = Paths.get(imagePath);

            if (!Files.exists(path)) {
                logger.error("Image not found at path: {}", imagePath);
                return ResponseEntity.notFound().build();
            }

            byte[] imageData = Files.readAllBytes(path);
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(imageData.length);

            logger.info("Successfully retrieved image at path: {}", imagePath);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageData);

        } catch (IOException e) {            logger.error("Error retrieving image at path {}: {}", imagePath, e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}