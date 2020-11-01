package com.WKNS.gather.databaseModels;
import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.util.Map;

// Represents a user in the Users collection
public class User {
    @Exclude private String userID;
    private String firstName, lastName, email;
    private Uri photoURL;

    // Constructors
    public User() {}

    public User(String firstName, String lastName, String email, Uri photoURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photoURL = photoURL;
    }

    // Getters + Setters
    public String setUserID() { return userID; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Uri getPhotoURL() { return photoURL; }
    public void setPhotoURL(Uri photoURL) { this.photoURL = photoURL; }
}
