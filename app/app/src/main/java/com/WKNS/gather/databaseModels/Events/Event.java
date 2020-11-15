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

    //private String photo; TODO: find out how photos encoded/stored, can't be URI in firestore
    private Date date;
    //private boolean published; TODO: discuss it more

    //private Uri photo;

    @Exclude private Map<String, Budget> budgetMap;
    @Exclude private Map<String, Attendee> attendeeMap;
    @Exclude private Map<String, Invitation> invitationMap;
    @Exclude private Map<String, Task> taskMap;

    public Event(){ }


    public Event(String title, String description, String ownerID, String ownerFirstName, String ownerLastName, Date date){
        this.title = title;
        this.description = description;
        this.ownerID = ownerID;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.date = date;
    }

    //Setters
    public void setID(String eventID){
        this.eventID = eventID;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description) { this.description = description; }
    public void setOwnerID(String ownerID) { this.ownerID = ownerID; }
    public void setOwnerFirstName(String ownerFirstName){ this.ownerFirstName = ownerFirstName; };
    public void setOwnerLastName(String ownerLastName){ this.ownerLastName = ownerLastName; }
    public void setDate(Date date){ this.date = date; }
    //public void setPublished(boolean published) { this.published = published; }

    //Getters
    public String eventID(){ return this.eventID; } // This can't be called get because of firebase
    public String getTitle(){
        return this.title;
    }
    public String getDescription() {
        return this.description;
    }
    public String getOwnerID() { return ownerID; }
    public String getOwnerFirstName(){ return ownerFirstName;}
    public String getOwnerLastName(){ return ownerLastName;}
    public Date getDate() { return this.date; }
    //public boolean isPublished() { return this.published;}


}
