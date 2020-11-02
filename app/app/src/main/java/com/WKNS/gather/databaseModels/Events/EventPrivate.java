package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventPrivate {
    @Exclude private String eventPrivateId;

    private String organizerID;
    private Map<String, String> rolesMap; // userid to role
    private List<String> permissions;

    public EventPrivate(){ }

    public EventPrivate(String organizerID){
        this.organizerID = organizerID;
        this.rolesMap = new HashMap<String, String>();
        this.permissions = new ArrayList<String>();
    }

    //Setters
    public void setEventPrivateId(String eventPrivateId) { this.eventPrivateId = eventPrivateId; }
    public void setOrganizerID(String organizerID){ this.organizerID = organizerID; }
    public void setRolesMap(Map<String, String> rolesMap){ this.rolesMap = rolesMap; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }

    //Getters
    public String getEventPrivateId() { return this.eventPrivateId; }
    public String getOrganizerID(){ return this.organizerID; }
    public Map<String,String> getRolesMap(){ return this.rolesMap; }
    public List<String> getPermissions() { return this.permissions; }
}
