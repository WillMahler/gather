package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.Map;


public class Event {
    @Exclude private String eventID;
    private String title;
    private String description;

    private String ownerID;
    private String ownerFirstName;
    private String ownerLastName;

    private Date date;
    private String time;
    private String location;
    private boolean published;
    private String profileImg;

    @Exclude private Map<String, Attendee> attendeeMap;

    public Event(){ }

    public Event(String title, String description, String ownerID, String ownerFirstName,
                 String ownerLastName, Date date, String time, String location, boolean published, String profileImg) {
        this.title = title;
        this.description = description;
        this.ownerID = ownerID;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.date = date;
        this.time = time;
        this.location = location;
        this.published = published;
        this.profileImg = profileImg;
    }

    // Setters
    public void setID(String eventID) { this.eventID = eventID; }
    public void setTitle(String title){ this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setOwnerID(String ownerID) { this.ownerID = ownerID; }
    public void setOwnerFirstName(String ownerFirstName) { this.ownerFirstName = ownerFirstName; }
    public void setOwnerLastName(String ownerLastName){ this.ownerLastName = ownerLastName; }
    public void setDate(Date date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setLocation(String location) { this.location = location; }
    public void setPublished(boolean published) { this.published = published; }
    public void setProfileImg(String url) { this.profileImg = url; }

    public void addAttendee(String id, Attendee attendee) { attendeeMap.put(id, attendee); }
  
    // Getters
    @Exclude public String getEventID(){ return this.eventID; }
    public String getTitle(){ return this.title; }
    public String getDescription() { return this.description; }
    public String getOwnerID() { return this.ownerID; }
    public String getOwnerFirstName(){ return this.ownerFirstName; }
    public String getOwnerLastName(){ return this.ownerLastName; }
    public Date getDate() { return this.date; }
    public String getTime() { return this.time; }
    public String getLocation() {return this.location; }
    public boolean isPublished() { return this.published; }
    public String getProfileImg() { return this.profileImg; }

}
