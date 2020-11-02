package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;
import com.google.type.DateTime;

import java.util.Map;


public class Event {
    @Exclude private String eventID;
    private String title;
    private String description;
    private DateTime time;

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
    public Event(String title, String description, DateTime time){
        this.title = title;
        this.description = description;
        this.time = time;
    }

    public void addBudget(String key, Budget budget){

    }

    //Setters
    public void setID(String eventID){
        this.eventID = eventID;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description) { this.description = description; }
    public void setTime(DateTime time){
        this.time = time;
    }

    //Getters
    public String getID(){
        return this.eventID;
    }
    public String getTitle(){
        return this.title;
    }
    public String getDescription() {
        return description;
    }
    public DateTime getTime() { return time; }


}
