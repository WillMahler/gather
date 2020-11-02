package com.WKNS.gather.databaseModels.Users;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

// Represents a user's private data
public class UserPrivate {
    @Exclude private String documentID;
    @Exclude private Map<String, Friend> friends;
    @Exclude private Map<String, FriendRequest> outgoingRequests;
    @Exclude private Map<String, FriendRequest> incomingRequests;
    @Exclude private Map<String, UserEvent> events;

    // Constructor
    public UserPrivate() {
        this.friends = new HashMap<String, Friend>();
        this.outgoingRequests = new HashMap<String, FriendRequest>();
        this.incomingRequests = new HashMap<String, FriendRequest>();
        this.events = new HashMap<String, UserEvent>();
    }

    // Getters + Setters
    public String getDocumentID() { return documentID; }

    public Map<String, Friend> getFriends() { return friends; }

    public Map<String, FriendRequest> getOutgoingRequests() { return outgoingRequests; }

    public Map<String, FriendRequest> getIncomingRequests() { return incomingRequests; }

    public Map<String, UserEvent> getEvents() { return events; }
}
