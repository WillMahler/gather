package com.WKNS.gather.databaseModels;
import android.net.Uri;

// Represents a user in the Users collection
public class User {
    private String userID, firstName, lastName, email;
    private Uri photoURL;

    // Constructors
    public User() {}

    public User(String userID, String firstName, String lastName, String email, Uri photoURL) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photoURL = photoURL;
    }

    // Getters
    public String getUserID() { return userID; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getEmail() { return email; }

    public Uri getPhotoURL() { return photoURL; }

}
