package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;
import com.google.type.DateTime;

import java.util.Map;


public class Event {
    @Exclude private String eventID;
    private String title;
    private String description;

    private String ownerID;
    private String ownerFirstName;
    private String ownerLastName;

    //private String photo; TODO: find out how photos encoded/stored, can't be URI in firestore
    private DateTime time;
    //private boolean published; TODO: discuss it more

    //private Uri photo;

    @Exclude private Map<String, Budget> budgetMap;
    @Exclude private Map<String, Attendee> attendeeMap;
    @Exclude private Map<String, Invitation> invitationMap;
    @Exclude private Map<String, Task> taskMap;

    public Event(){ }

    /**
     * @param title         The title of the event
     * @param description   The description of the event
     * @param time          The time when the event happens
     */
    public Event(String title, String description, String ownerFirstName, String ownerLastName, DateTime time){
        this.title = title;
        this.description = description;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.time = time;
    }

    //Setters
    public void setID(String eventID){
        this.eventID = eventID;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description) { this.description = description; }
    public void setOwnerFirstName(String ownerFirstName){ this.ownerLastName = ownerFirstName; };
    public void setOwnerLastName(String ownerLastName){ this.ownerLastName = ownerLastName; }
    public void setTime(DateTime time){
        this.time = time;
    }
    //public void setPublished(boolean published) { this.published = published; }

    //Getters
    public String getEventID(){
        return this.eventID;
    }
    public String getTitle(){
        return this.title;
    }
    public String getDescription() {
        return this.description;
    }
    public String getOwnerFirstName(){ return this.ownerFirstName;}
    public String getOwnerLastName(){ return this.ownerLastName;}
    public DateTime getTime() { return this.time; }
    //public boolean isPublished() { return this.published;}


}
