package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventPrivate {
    @Exclude private String eventPrivateId;

    private Map<String, String> rolesMap; // userid to role
    private List<String> permissions;

    public EventPrivate(){ }

    public EventPrivate(Map<String, String> rolesMap, List<String> permissions){
        this.rolesMap = rolesMap;
        this.permissions = permissions;
    }

    //Setters
    public void setEventPrivateId(String eventPrivateId) { this.eventPrivateId = eventPrivateId; }
    public void setRolesMap(Map<String, String> rolesMap){ this.rolesMap = rolesMap; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }

    //Getters
    public String getEventPrivateId() { return this.eventPrivateId; }
    public Map<String,String> getRolesMap(){ return this.rolesMap; }
    public List<String> getPermissions() { return this.permissions; }
}
