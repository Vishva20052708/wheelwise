package com.example.WheelWise.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.WheelWise.entities.User;
import com.example.WheelWise.services.UserService;

@RestController
public class LoginController {
    
    @Autowired
    UserService userService;
    
    @RequestMapping("/signin")
    @ResponseBody
    public Map<String, String> login() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login page..");
        return response;
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signin(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        // Retrieve user by email
        Optional<User> optionalUser = userService.findByEmail(user.getEmail());

        // Check if user exists and password is correct
        if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(user.getPassword())) {
            response.put("message", "Invalid email or password");
            return ResponseEntity.badRequest().body(response);
        }

        // Get the user object from Optional
        User foundUser = optionalUser.get();

        // Set login status and save changes
        foundUser.setLoggedIn(true);  // Set the 'isLoggedIn' field to true
        userService.registerUser(foundUser);  // Persist the login status

        // Return the full user details upon successful sign-in
        response.put("message", "Login successful");
        response.put("email", foundUser.getEmail()); 
        response.put("firstName", foundUser.getFirstname()); 
        response.put("lastName", foundUser.getLastname()); 
        response.put("contactNo", foundUser.getContactNo()); 
        response.put("address", foundUser.getAddress());  // Add address to the response
        response.put("LicenseUrl", foundUser.getLicenseUrl());  // Add licenseUrl to the response
     //  response.put("profilePhotoUrl", foundUser.getProfilePhotoUrl()); // Add profilePhotoUrl
        response.put("UserId", foundUser.getUserId()); 
        return ResponseEntity.ok(response);
    }
}