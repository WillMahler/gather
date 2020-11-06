package com.WKNS.gather.databaseModels.Users;

import android.net.Uri;

// Represents an outgoing/incoming friend request in User's Private sub-collection
public class FriendRequest {
    private String userID, firstName, lastName;
    private Uri photoURL;

    // Constructors
    public FriendRequest() {}

    public FriendRequest(String userID, String firstName, String lastName, Uri photoURL) {
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
