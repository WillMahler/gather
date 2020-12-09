package com.WKNS.gather.databaseModels.Users;

import com.google.firebase.firestore.Exclude;
import java.util.Date;

// Represents an event associated with User in User's Private sub-collection
public class UserEvent {
    @Exclude private String eventID;
    private String title;
    private String description;
    private String ownerID;
    private String location;

    private String ownerFirstName;
    private String ownerLastName;

    private String profileImg;
    private Date date;
    private int status; // User's invite status; 0 if invited, 1 if accepted, 2 if declined
    private boolean published;

    // Constructors
    public UserEvent() {}

    public UserEvent(String title, String description, String ownerFirstName, String ownerLastName, Date date, int status, boolean published, String profileImg, String location) {
        this.title = title;
        this.description = description;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.date = date;
        this.status = status;
        this.published = published;
        this.profileImg = profileImg;
        this.location = location;
    }
    //Setters
    public void setEventID(String eventID) { this.eventID = eventID; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setOwnerFirstName(String ownerFirstName) { this.ownerFirstName = ownerFirstName; }
    public void setOwnerLastName(String ownerLastName) { this.ownerLastName = ownerLastName; }
    public void setDate(Date date) {this.date = date; }
    public void setStatus(int status) { this.status = status; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }
    public void setPublished(boolean published) {
        this.published = published;
    }
    public void setOwnerID(String ownerID) { this.ownerID = ownerID; }
    public void setLocation(String location) { this.location = location; }

    // Getters
    @Exclude public String getEventID() { return eventID; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getOwnerFirstName() { return ownerFirstName; }
    public String getOwnerLastName() { return ownerLastName; }
    public Date getDate() { return date; }
    public int getStatus() { return status; }
    public String getProfileImg() { return this.profileImg; }
    public boolean isPublished() {
        return published;
    }
    public String getOwnerID() { return ownerID; }
    public String getLocation() { return location; }





}
