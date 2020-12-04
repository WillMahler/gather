package com.WKNS.gather.databaseModels.Users;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;
import java.util.Date;

// Represents an event associated with User in User's Private sub-collection
public class UserEvent {
    @Exclude private String eventID;
    private String title;
    private String description;

    private String ownerFirstName;
    private String ownerLastName;

    //@Exclude private String photo; //TODO: find out how photos encoded/stored, can't be URI in firestore
    private Date date;
    private int status; // User's invite status; 0 if invited, 1 if accepted, 2 if declines

    // Constructors
    public UserEvent() {}

    public UserEvent(String title, String description, String ownerFirstName, String ownerLastName, Date date, int status) {
        this.title = title;
        this.description = description;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.date = date;
        this.status = status;
    }
    //Setters
    public void setEventID(String eventID) { this.eventID = eventID; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setOwnerFirstName(String ownerFirstName) { this.ownerFirstName = ownerFirstName; }
    public void setOwnerLastName(String ownerLastName) { this.ownerLastName = ownerLastName; }
    public void setDate(Date date) {this.date = date; }
    public void setStatus(int status) { this.status = status; }

    // Getters
    @Exclude public String getEventID() { return eventID; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getOwnerFirstName() { return ownerFirstName; }
    public String getOwnerLastName() { return ownerLastName; }
    public Date getDate() { return date; }
    public int getStatus() { return status; }
}
