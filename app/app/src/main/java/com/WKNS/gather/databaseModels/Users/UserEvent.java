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
    private Date time;
    private int status; // User's invite status; 0 if invited, -1 if denied, 1 if accepted

    // Constructors
    public UserEvent() {}

    public UserEvent(String title, String description, String ownerFirstName, String ownerLastName, Date time, int status) {
        this.title = title;
        this.description = description;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.time = time;
        this.status = status;
    }
    //Setters
    public void eventID(String eventID) { this.eventID = eventID; } //This can't be named setEventID otherwise value is added to firebase
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setOwnerFirstName(String ownerFirstName) { this.ownerFirstName = ownerFirstName; }
    public void setOwnerLastName(String ownerLastName) { this.ownerLastName = ownerLastName; }
    public void setTime(Date time) { this.time = time; }
    public void setStatus(int status) { this.status = status; }

    // Getters
    public String eventID() { return eventID; }     //This can't be named getEventID otherwise value is added to firebase
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getOwnerFirstName() { return ownerFirstName; }
    public String getOwnerLastName() { return ownerLastName; }
    public Date getTime() { return time; }
    public int getStatus() { return status; }
}
