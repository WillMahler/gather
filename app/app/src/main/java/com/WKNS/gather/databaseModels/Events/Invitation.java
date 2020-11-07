package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;

public class Invitation {
    @Exclude private String invitationID;

    private String firstName;
    private String lastName;
    private boolean didDenied; //false pending request, true denied, when accepted user moved to attendee

    public Invitation() { }

    /**
     * An invitation to an event. By default a user is pending.
     * @param firstName
     * @param lastName
     */
    public Invitation(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.didDenied = false;
    }
    //Setters
    public void setID(String invitationID) { this.invitationID = invitationID; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDidDenied(boolean didDenied) { this.didDenied = didDenied; }

    //Getters
    public String getInvitationID(){ return invitationID; }
    public String getFirstName(){ return firstName; }
    public String getLastName(){ return lastName; }
    public boolean isDidDenied(){ return didDenied; }

}
