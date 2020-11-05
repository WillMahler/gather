package com.WKNS.gather.testData;

import java.util.ArrayList;
import java.util.Date;

public class Event {
    private String title;
    private String description;

    private String ownerFirstName;
    private String ownerLastName;

    private Date time;
    private boolean published;

    //private Uri photo;

    public Event(String eventTitle, String hostFirstName, Date eventTime) {
        title = eventTitle;
        ownerFirstName = hostFirstName;
        time = eventTime;
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
        return time;
    }

    public boolean isPublished() {
        return published;
    }


    public static ArrayList<Event> testEvents() {
        ArrayList<Event> testData = new ArrayList<>();

        testData.add(new Event("Borat Watchparty", "Azamat", new Date(2020, 11, 13, 20, 30)));
        testData.add(new Event("Wing Night", "James", new Date(2020, 11, 17, 19, 15)));
        testData.add(new Event("Stranger Things Marathon", "Elle", new Date(2020, 11, 29, 11, 30)));
        testData.add(new Event("Camping Trip", "Bear", new Date(2020, 11, 30, 8, 0)));
        testData.add(new Event("UFC 300", "Dana", new Date(2020, 12, 02, 21, 0)));
        testData.add(new Event("Baseball Game", "Roberto", new Date(2020, 12, 4, 14, 45)));
        testData.add(new Event("Housewarming Party", "Jessica", new Date(2020, 12, 17, 12, 30)));
        testData.add(new Event("Christmas Party!", "Santa", new Date(2020, 12, 24, 21, 0)));
        testData.add(new Event("Hangover Hangout", "Mike", new Date(2020, 12, 15, 9, 30)));
        testData.add(new Event("Cottage Trip", "Colton", new Date(2021, 1, 3, 17, 15)));
        testData.add(new Event("Skydiving", "Usain", new Date(2021, 3, 24, 13, 30)));
        testData.add(new Event("Bungee Jumping", "Sarah", new Date(2021, 5, 31, 15, 0)));

        return testData;
    }
}
