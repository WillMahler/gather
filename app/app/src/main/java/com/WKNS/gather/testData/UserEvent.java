package com.WKNS.gather.testData;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Date;

public class UserEvent {
    @Exclude
    private String eventID;
    private String title;
    private String description;

    private String ownerID;
    private String ownerFirstName;
    private String ownerLastName;

    private Date date;
    private String location;
    private boolean published;
    private String photoURL;

    public UserEvent(String eventTitle, String hostFirstName, Date eventTime) {
        title = eventTitle;
        ownerFirstName = hostFirstName;
        date = eventTime;
    }

    public UserEvent(String eventTitle, String hostFirstName, Date eventTime, String imgURI) {
        title = eventTitle;
        ownerFirstName = hostFirstName;
        date = eventTime;
        photoURL = imgURI;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public Date getTime() {
        return date;
    }

    public boolean isPublished() {
        return published;
    }
    public Date getDate() { return date; }


    public static ArrayList<UserEvent> testEvents() {
        ArrayList<UserEvent> testData = new ArrayList<>();

        testData.add(new UserEvent("Borat Watchparty", "Azamat", new Date(2020, 11, 13, 20, 30), "https://miro.medium.com/max/1200/1*ZwsuiM48pU22ugmPQq_5vA.jpeg"));
        testData.add(new UserEvent("Wing Night", "James", new Date(2020, 11, 17, 19, 15)));
        testData.add(new UserEvent("Stranger Things Marathon", "Elle", new Date(2020, 11, 29, 11, 30), "https://miro.medium.com/max/1200/1*ZwsuiM48pU22ugmPQq_5vA.jpeg"));
        testData.add(new UserEvent("Camping Trip", "Bear", new Date(2020, 11, 30, 8, 0), "https://miro.medium.com/max/1200/1*ZwsuiM48pU22ugmPQq_5vA.jpeg"));
        testData.add(new UserEvent("UFC 300", "Dana", new Date(2020, 12, 02, 21, 0)));
        testData.add(new UserEvent("Baseball Game", "Roberto", new Date(2020, 12, 4, 14, 45)));
        testData.add(new UserEvent("Housewarming Party", "Jessica", new Date(2020, 12, 17, 12, 30), "https://miro.medium.com/max/1200/1*ZwsuiM48pU22ugmPQq_5vA.jpeg"));
        testData.add(new UserEvent("Christmas Party!", "Santa", new Date(2020, 12, 24, 21, 0)));
        testData.add(new UserEvent("Hangover Hangout", "Mike", new Date(2020, 12, 15, 9, 30)));
        testData.add(new UserEvent("Cottage Trip", "Colton", new Date(2021, 1, 3, 17, 15), "https://miro.medium.com/max/1200/1*ZwsuiM48pU22ugmPQq_5vA.jpeg"));
        testData.add(new UserEvent("Skydiving", "Usain", new Date(2021, 3, 24, 13, 30)));
        testData.add(new UserEvent("Bungee Jumping", "Sarah", new Date(2021, 5, 31, 15, 0), "https://miro.medium.com/max/1200/1*ZwsuiM48pU22ugmPQq_5vA.jpeg"));

        return testData;
    }
}
