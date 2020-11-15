package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;
import com.google.type.DateTime;

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
    private String location;
    private boolean published;

    //private Uri photo;

    @Exclude private Map<String, Budget> budgetMap;
    @Exclude private Map<String, Attendee> attendeeMap;
    @Exclude private Map<String, Invitation> invitationMap;
    @Exclude private Map<String, Task> taskMap;

    public Event(){ }

    public Event(String title, String description, String ownerID, String ownerFirstName,
                 String ownerLastName, Date date, String location, boolean published) {
        this.title = title;
        this.description = description;
        this.ownerID = ownerID;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.date = date;
        this.location = location;
        this.published = published;
    }

    public void addBudget(String id, Budget budget){
        budgetMap.put(id, budget);
    }
    public void addAttendee(String id, Attendee attendee) { attendeeMap.put(id, attendee); }

    //Setters
    public void setID(String eventID){
        this.eventID = eventID;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description) { this.description = description; }
    public void setOwnerID(String ownerID) { this.ownerID = ownerID; }
    public void setOwnerFirstName(String ownerFirstName){ this.ownerLastName = ownerFirstName; };
    public void setOwnerLastName(String ownerLastName){ this.ownerLastName = ownerLastName; }
    public void setTime(Date date){ this.date = date; }
    public void setLocation(String location) { this.location = location; }
    public void setPublished(boolean published) { this.published = published; }

    //Getters
    @Exclude
    public String getEventID(){ return this.eventID; }

    public String getTitle(){
        return this.title;
    }
    public String getDescription() {
        return this.description;
    }
    public String getOwnerID() { return this.ownerID; }
    public String getOwnerFirstName(){ return this.ownerFirstName;}
    public String getOwnerLastName(){ return this.ownerLastName;}
    public Date getDate() { return this.date; }
    public String getLocation() {return this.location; }
    public boolean isPublished() { return this.published;}


}
