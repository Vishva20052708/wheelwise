package com.example.WheelWise.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.WheelWise.entities.User;
import com.example.WheelWise.repositories.UserRepository;
@Service
public class UserService {

		@Autowired
	    private UserRepository userRepository;
	   
	    // Method to create a new user
	   
	    public User registerUser(User user) {
	        return userRepository.save(user);
	    }

	    // Method to retrieve all users
	    public List<User> getAllUsers() {
	        return userRepository.findAll();
	    }

	    // Method to retrieve a user by ID
	    public User getUserById(Integer userId) {
	        return userRepository.findById(userId).orElse(new User());
	    }
	    public Optional<User> findByEmail(String email) {
	        return userRepository.findByEmail(email);
	    }
	    public boolean updateUserPassword(String email, String newPassword) {
	        Optional<User> foundUser = userRepository.findByEmail(email);
	        if (foundUser.isPresent()) {
	            User user = foundUser.get();
	            user.setPassword(newPassword);  // Save the password directly without encoding
	            userRepository.save(user);
	            return true;
	        }
	        return false;  // Return false if user is not found
	    }
	    public boolean updateProfile(String email, String firstName, String lastName, String phoneNumber, 
			                String address, String licenseUrl, MultipartFile profilePhoto) {
			Optional<User> userOptional = userRepository.findByEmail(email);
			
			if (userOptional.isPresent()) {
			User user = userOptional.get();
			
			// Update fields
			user.setFirstname(firstName);
			user.setLastname(lastName);
			user.setContactNo(phoneNumber);
			user.setAddress(address);
			user.setLicenseUrl(licenseUrl);
			
			// Update profile photo if provided
			if (profilePhoto != null && !profilePhoto.isEmpty()) {
			try {
			   user.setProfilePhoto(profilePhoto.getBytes());
			} catch (IOException e) {
			   throw new RuntimeException("Failed to upload profile photo", e);
			}
			}
			
			userRepository.save(user);
			return true;
			}
			return false;
	    	}
}
