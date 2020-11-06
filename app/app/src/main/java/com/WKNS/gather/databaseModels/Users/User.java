package com.WKNS.gather.databaseModels.Users;
import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.util.Map;

// Represents a user in the Users collection
public class User {
    @Exclude private String userID; // Matches UserID from Firebase Auth
    private String firstName, lastName;

    // Constructors
    public User() {}

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters + Setters
    public String getUserID() { return this.userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
