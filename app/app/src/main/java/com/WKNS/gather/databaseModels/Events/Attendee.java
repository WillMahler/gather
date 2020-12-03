package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;

public class Attendee {
    @Exclude private String attendeeID;

    private String firstName;
    private String lastName;
    private String photoURL;
    private String email;

    public Attendee() {}

    public Attendee(String firstName, String lastName, String photoURL, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoURL = photoURL;
        this.email = email;
    }

    //Setters
    public void setID(String attendeeID) { this.attendeeID = attendeeID; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhotoURL(String photoURL) { this.photoURL = photoURL; }
    public void setEmail(String email) { this.email = email; }

    //Getters
    public String getAttendeeID(){ return attendeeID; }
    public String getFirstName(){ return firstName; }
    public String getLastName(){ return lastName; }
    public String getPhotoURL() { return photoURL; }
    public String getEmail() { return email; }
}
