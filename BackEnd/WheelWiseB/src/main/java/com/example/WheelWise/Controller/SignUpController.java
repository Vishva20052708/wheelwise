package com.example.WheelWise.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
public class SignUpController {

	@Autowired
	UserService userService;
	
	@RequestMapping("/signup")
	@ResponseBody
	public Map<String, String> signup() {
	    Map<String, String> response = new HashMap<>();
	    response.put("message", "Sign Up page..");
	    return response;
	}

 
	@PostMapping("/signup")
	public ResponseEntity<Map<String, String>> signup(@RequestBody User user) {
	    Map<String, String> response = new HashMap<>();

	    if (userService.findByEmail(user.getEmail()).isPresent()) {
	        response.put("message", "Email already exists");
	        return ResponseEntity.badRequest().body(response);
	    }

	    user.setCreatedAt(LocalDateTime.now());
	    user.setModifiedAt(LocalDateTime.now());
	    userService.registerUser(user);

	    response.put("message", "User registered successfully");
	    return ResponseEntity.ok(response);
	}

	@PostMapping("/signout")
    public ResponseEntity<Map<String, String>> signout(@RequestBody User user) {
		 Map<String, String> response = new HashMap<>();
        User foundUser = userService.findByEmail(user.getEmail())
                .orElse(new User());

        if (foundUser == null) {
        	response.put("message", "Invalid email");
	        return ResponseEntity.badRequest().body(response);
        }

        foundUser.setLoggedIn(false);  // Update isLoggedIn to false
        userService.registerUser(foundUser);  // Persist changes in the database
        response.put("message", "User signed out successfully");
	    return ResponseEntity.ok(response);
       
    }

}
