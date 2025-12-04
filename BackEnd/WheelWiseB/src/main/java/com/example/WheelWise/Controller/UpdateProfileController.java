package com.example.WheelWise.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.WheelWise.entities.User;
import com.example.WheelWise.services.UserService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UpdateProfileController {

    @Autowired
    private UserService userService;

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUserProfile(
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("licenseUrl") String licenseUrl,
            @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto) {
        try {
            // Update user profile with optional photo
            boolean success = userService.updateProfile(email, firstName, lastName, phoneNumber, address, licenseUrl, profilePhoto);

            if (success) {
                // Fetch updated user details
                Optional<User> updatedUser = userService.findByEmail(email);
                
                if (updatedUser.isPresent()) {
                    return ResponseEntity.ok(Map.of(
                        "message", "Profile updated successfully",
                        "user", updatedUser.get()
                    ));
                } else {
                    return ResponseEntity.status(404).body(Map.of("error", "User not found after update."));
                }
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "User not found."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to update profile: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/profile-photo")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        byte[] image = user.getProfilePhoto();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Set appropriate content type
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

}