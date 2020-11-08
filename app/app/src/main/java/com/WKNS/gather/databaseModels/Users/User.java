package com.WKNS.gather.databaseModels.Users;

// Represents a user in the Users collection
public class User {
    private String userID, email, firstName, lastName, profileImg;

    // Constructors
    public User() {}

    public User(String userID, String email, String firstName, String lastName, String profileImg) {
        this.userID = userID;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImg = profileImg;
    }

    // Getters + Setters
    public String getUserID() { return this.userID; }
    public void setUserID(String userID) { this.email = userID; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getProfileImage() { return profileImg; }
    public void setProfileImage(String profileImage) { this.profileImg = profileImage; }
}
