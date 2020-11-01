package com.WKNS.gather.databaseModels;
import android.net.Uri;

// Represents a friend in the User's Private sub-collection
public class Friend {
    private String firstName, lastName, userID;
    private Uri photoURL;

    // Constructors
    public Friend() {}

    public Friend(String userID, String firstName, String lastName, Uri photoURL) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoURL = photoURL;
    }

    // Getters
    public String getUserID() { return userID; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public Uri getPhotoURL() { return photoURL; }

}
