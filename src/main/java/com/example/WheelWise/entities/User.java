package com.example.WheelWise.entities;

import java.time.LocalDateTime;
import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "user_id")
	    private Integer userId;

		@Override
		public String toString() {
			return "User [userId=" + userId + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
					+ ", password=" + password + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt
					+ ", isLoggedIn=" + isLoggedIn + ", contactNo=" + contactNo + ", profilePhoto="
					+ Arrays.toString(profilePhoto) + ", licenseUrl=" + licenseUrl + "]";
		}

		@Column(name = "firstname", nullable = false, length = 100)
	    private String firstname;
	    
	    @Column(name = "lastname", nullable = false, length = 100)
	    private String lastname;
	    

	    @Column(name = "email", nullable = false, unique = true, length = 100)
	    private String email;

	    public User() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Column(name = "password", nullable = false)
	    private String password;

	    private LocalDateTime createdAt;  // New field for timestamp of registration
	    private LocalDateTime modifiedAt; // New field for timestamp of last modification
	    private boolean isLoggedIn = false; // New field to track login status
	    @Column(name = "address", length = 255)
	    private String address; // New field for user address
	    public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public LocalDateTime getModifiedAt() {
			return modifiedAt;
		}

		public void setModifiedAt(LocalDateTime modifiedAt) {
			this.modifiedAt = modifiedAt;
		}

		public boolean isLoggedIn() {
			return isLoggedIn;
		}

		public void setLoggedIn(boolean isLoggedIn) {
			this.isLoggedIn = isLoggedIn;
		}

		@Column(name = "contact_no", length = 10)
	    private String contactNo;
	    public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		
		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getContactNo() {
			return contactNo;
		}

		public void setContactNo(String contactNo) {
			this.contactNo = contactNo;
		}

		@Column(name = "profile_photo", columnDefinition = "BLOB")
		private byte[] profilePhoto; // Field to store image data in binary format

		// Getter and Setter
		public byte[] getProfilePhoto() {
		    return profilePhoto;
		}

		public void setProfilePhoto(byte[] profilePhoto) {
		    this.profilePhoto = profilePhoto;
		}
		   @Column(name = "license_url", length = 500)
		    private String licenseUrl; // New field for license document URL

		public String getLicenseUrl() {
			return licenseUrl;
		}

		public void setLicenseUrl(String licenseUrl) {
			this.licenseUrl = licenseUrl;
		}

		public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }
	
	    
}
