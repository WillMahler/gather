package com.WKNS.gather.databaseModels.Users;

import android.graphics.Bitmap;

// Represents a user in the Users collection
public class User {
    private String userID, email, phoneNum, firstName, lastName, profileImg, bio;
    private Bitmap profileBitmap;

    // Constructors
    public User() {}

    public User(String userID, String email, String phoneNum, String firstName, String lastName, String profileImg, String bio) {
        this.userID = userID;
        this.email = email;
        this.phoneNum = phoneNum;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImg = profileImg;
        this.bio = bio;
        this.profileBitmap = null;
    }

    // Getters + Setters
    public String getUserID() { return this.userID; }
    public void setUserID(String userID) { this.email = userID; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNum() { return this.phoneNum; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getProfileImage() { return profileImg; }
    public void setProfileImage(String profileImage) { this.profileImg = profileImage; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Bitmap getProfileBitmap() { return profileBitmap; }
    public void setProfileBitmap(Bitmap profileBitmap) { this.profileBitmap = profileBitmap; }
}