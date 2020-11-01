package com.WKNS.gather.databaseModels;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;
import com.google.type.DateTime;

import java.util.Map;

public class Event {
    @Exclude private String eventID;
    private String title;
    private String description;
    private DateTime time;
    private Uri photo;

    @Exclude private Map<String, Budget> budgetMap;

    public Event(){

    }

    /**
     *
     * @param title         The title of the event
     * @param description   The description of the event
     * @param time          The time when the event happens
     * @param photo         The photo used for the event
     */
    public Event(String title, String description, DateTime time, Uri photo){
        this.title = title;
        this.description = description;
        this.time = time;
        this.photo = photo;
    }

    public void addBudget(String key, Budget budget){

    }

    //Setters
    public void setKey(String eventID){
        this.eventID = eventID;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setTime(DateTime time){
        this.time = time;
    }

    //Getters
    public String getEventID(){
        return this.eventID;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getTime() {
        return time;
    }

    //Disabled for now because database doesn't store images, other method for storing messages exists
//    public Uri getPhoto() {
//        return photo;
//    }
}
