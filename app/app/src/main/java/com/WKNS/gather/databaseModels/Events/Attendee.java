package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;

public class Attendee {
    @Exclude private String attendeeID;

    private String firstName;
    private String lastName;
    //private Uri photo;
    public Attendee() { }

    public Attendee(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    //Setters
    public void setID(String attendeeID) { this.attendeeID = attendeeID; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    //Getters
    public String getAttendeeID(){ return attendeeID; }
    public String getFirstName(){ return firstName; }
    public String getLastName(){ return lastName; }
}
