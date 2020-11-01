package com.WKNS.gather.databaseModels;

import android.net.Uri;

import com.google.type.Date;
import com.google.type.DateTime;

// Represents an event associated with User in User's Private sub-collection
public class UserEvent {
    private String eventID;
    private DateTime date;
    private String description;
    private Uri photoURL;
    private short status; // User's invite status; 0 if invited, -1 if denied, 1 if accepted

    // Constructors
    public UserEvent() {}

    public UserEvent(String eventID, DateTime date, String description,
                     Uri photoURL, Short status) {
        this.eventID = eventID;
        this.date = date;
        this.description = description;
        this.photoURL = photoURL;
        this.status = status;
    }

    // Getters
    public String getEventID() { return eventID; }

    public DateTime getDate() { return date; }

    public String getDescription() { return description; }

    public Uri getPhotoURL() { return photoURL; }

    public short getStatus() { return status; }
}
