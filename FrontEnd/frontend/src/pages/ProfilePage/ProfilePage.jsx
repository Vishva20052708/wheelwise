import React, { useState, useEffect } from 'react';
import { useAuth } from '../../hooks/AuthProvider';
import './ProfilePage.css';

const ProfilePage = () => {
  const { user, setUser, updateProfile } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [editData, setEditData] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    email: user?.email || '',
    contactNo: user?.contactNo || '',
    address: user?.address || '',
    licenseUrl: user?.licenseUrl || '',
    profilePhoto: null,
  });

  const [profilePhotoUrl, setProfilePhotoUrl] = useState('');
  const [loading, setLoading] = useState(false);

  // Fetch profile photo URL on mount or when user changes
  useEffect(() => {
    if (user?.UserId) {
      const photoUrl = `http://localhost:8080/api/user/${user.UserId}/profile-photo`;
      setProfilePhotoUrl(photoUrl); // Updates the profile photo URL whenever the user data changes
    }
  }, [user]); // Ensure this effect runs whenever user changes
  
  // Handle profile photo change with preview
  const handlePhotoChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setEditData((prev) => ({ ...prev, profilePhoto: file }));
      const reader = new FileReader();
      reader.onloadend = () => {
        setProfilePhotoUrl(reader.result);  // Preview image
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSave = async () => {
    setLoading(true);
    try {
      // Prepare updated data for profile
      const updatedData = {
        firstName: editData.firstName,
        lastName: editData.lastName,
        email: user.email, // Include email as per the backend requirement
        phoneNumber: editData.contactNo, // Map contactNo to phoneNumber
        address: editData.address,
        licenseUrl: editData.licenseUrl,
        profilePhoto: editData.profilePhoto, // Handle profile photo separately
      };
  
      // Call the updateProfile function from AuthContext
      await updateProfile(updatedData);
      
      // Successfully updated
      setIsEditing(false);
    } catch (error) {
      console.error('Error updating profile:', error.message);
      alert(`Profile update failed: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleEditClick = () => setIsEditing(true);

  const handleCancel = () => {
    setIsEditing(false);
    setEditData({
      firstName: user?.firstName || '',
      lastName: user?.lastName || '',
      email: user?.email || '',
      contactNo: user?.contactNo || '',
      address: user?.address || '',
      licenseUrl: user?.licenseUrl || '',
      profilePhoto: null,
    });
    setProfilePhotoUrl(`http://localhost:8080/api/user/${user?.UserId}/profile-photo`);
  };

  return (
    <div className="profile-page">
      <h2>Profile Page</h2>
      {isEditing ? (
        <div className="edit-form">
          {/* Profile photo container */}
          <div className="profile-photo-container">
            {profilePhotoUrl ? (
              <img
                src={profilePhotoUrl}
                alt="Profile"
                className="profile-photo"
              />
            ) : (
              <div>No photo available</div>
            )}
            <input type="file" accept="image/*" onChange={handlePhotoChange} />
          </div>

          {/* First Name */}
          <label>
            <span>First Name:</span>
            <input
              type="text"
              name="firstName"
              value={editData.firstName}
              onChange={(e) => setEditData({ ...editData, firstName: e.target.value })}
            />
          </label>

          {/* Last Name */}
          <label>
            <span>Last Name:</span>
            <input
              type="text"
              name="lastName"
              value={editData.lastName}
              onChange={(e) => setEditData({ ...editData, lastName: e.target.value })}
            />
          </label>

          {/* Email (Read-only, as it's not editable) */}
          <label>
            <span>Email:</span>
            <input
              type="email"
              name="email"
              value={editData.email}
              readOnly
              disabled
            />
          </label>

          {/* Contact Number */}
          <label>
            <span>Contact Number:</span>
            <input
              type="tel"
              name="contactNo"
              value={editData.contactNo}
              onChange={(e) => setEditData({ ...editData, contactNo: e.target.value })}
            />
          </label>

          {/* Address */}
          <label>
            <span>Address:</span>
            <textarea
              name="address"
              value={editData.address}
              onChange={(e) => setEditData({ ...editData, address: e.target.value })}
            />
          </label>

          {/* License URL */}
          <label>
            <span>License URL:</span>
            <input
              type="url"
              name="licenseUrl"
              value={editData.licenseUrl}
              onChange={(e) => setEditData({ ...editData, licenseUrl: e.target.value })}
            />
          </label>

          {/* Save and Cancel buttons */}
          <div className="button-container">
            <button onClick={handleSave} disabled={loading}>
              {loading ? 'Saving...' : 'Save'}
            </button>
            <button onClick={handleCancel} className="cancel">Cancel</button>
          </div>
        </div>
      ) : (
        <div className="profile-details">
          {user ? (
            <>
              {/* Profile Photo */}
              <div className="profile-photo-container">
                {profilePhotoUrl ? (
                  <img
                    src={profilePhotoUrl}
                    alt="Profile"
                    className="profile-photo"
                  />
                ) : (
                  'No photo uploaded'
                )}
              </div>

              <p><strong>First Name:</strong> {user.firstName}</p>
              <p><strong>Last Name:</strong> {user.lastName}</p>
              <p><strong>Email:</strong> {user.email}</p>
              <p><strong>Contact Number:</strong> {user.contactNo}</p>
              <p><strong>Address:</strong> {user.address}</p>
              <p><strong>License URL:</strong> {user.licenseUrl}</p>
              <button onClick={handleEditClick} className="edit-button">Edit</button>
            </>
          ) : (
            <p>Loading...</p>
          )}
        </div>
      )}
    </div>
  );
};

export default ProfilePage;
