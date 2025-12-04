package com.example.WheelWise.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.WheelWise.entities.Vehicle;
import com.example.WheelWise.services.VehicleService;

@RestController
public class VehicleController {
	
	@Autowired
	private  VehicleService vehicleService;
	
	@RequestMapping("/vehicles")
	@GetMapping
	   public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
	@GetMapping("/available")
	public ResponseEntity<List<Vehicle>> getAvailableVehicles(
	    @RequestParam(required = false) String location,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
	) {
	    List<Vehicle> availableVehicles = vehicleService.findAvailableVehicles(location, startTime, endTime);
	    return ResponseEntity.ok(availableVehicles);
	}

	@PostMapping("/addVehicle")
	public ResponseEntity<String> createVehicle(@RequestParam("vehicleData") String vehicleData,
												@RequestParam("image") MultipartFile[] images) {
		// Parse vehicleData to create a Vehicle object
		ObjectMapper objectMapper = new ObjectMapper();
		Vehicle vehicle;
		try {
			vehicle = objectMapper.readValue(vehicleData, Vehicle.class);
		} catch (IOException e) {
			// Handle parsing error
			return ResponseEntity.badRequest().body("Invalid vehicle data");
		}

		// Save the image to the file system


		for (MultipartFile image : images) {
			String imagePath = saveImageToFile(image);
			vehicle.getImagePaths().add(imagePath);

			// Create Image object and associate it with the vehicle
			Image vehicleImage = new Image();
			vehicleImage.setImagePath(imagePath);
			vehicleImage.setVehicle(vehicle);
			vehicle.getImages().add(vehicleImage);
		}

		// Save the vehicle and image to the database
		vehicleService.saveVehicle(vehicle);

		return ResponseEntity.ok("Vehicle Added");
	}


	private String saveImageToFile(MultipartFile image) {
		// Set the desired path to store the images
		String uploadDir = "WheelWise\\WheelWise\\ImageDir"; // Adjust the path as needed

		// Create the directory if it doesn't exist
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				// Handle exception, e.g., log the error and return an error response
				e.printStackTrace();
				return null;
			}
		}

		// Create the full file path
		Path imagePath = Paths.get(uploadDir, image.getOriginalFilename());

		try {
			Files.write(imagePath, image.getBytes());
			return imagePath.toString();
		} catch (IOException e) {
			// Handle exception, e.g., log the error and return an error response
			e.printStackTrace();
			return null;
		}
	}

}

