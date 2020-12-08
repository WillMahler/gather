package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;

public class Attendee {
    @Exclude private String attendeeID;

    private String firstName;
    private String lastName;
    private String email;
    private String profileImg;
    private int status;     // '0' == invited, '1' == accepted, '2' = denied;
    public Attendee() { }

    public Attendee(String firstName, String lastName, String email, String photo, int status){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profileImg = photo;
        this.status = status;
    }

    //Setters
    public void setID(String attendeeID) { this.attendeeID = attendeeID; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoto(String photo) { this.profileImg = photo; }
    public void setStatus(int status) { this.status = status; }

    //Getters
    public String getAttendeeID(){ return attendeeID; }
    public String getFirstName(){ return firstName; }
    public String getLastName(){ return lastName; }
    public String getEmail() { return email; }
    public String getPhoto() { return profileImg; }
    public int getStatus() { return status; }
}
