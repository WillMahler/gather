package com.WKNS.gather.testData;

import java.util.ArrayList;
import java.util.Date;

public class Notification {

    public enum Type {
        FRIEND_REQUEST,
        EVENT_INVITE
    }

    private Type type;

    private String mRequesterFirstName;
    private String mRequesterLastName;

    private String mEventTitle;
    private Date mTime;
    private String mHostName;

    public Notification(String firstName, String lastName) {
        type = Type.FRIEND_REQUEST;
        mRequesterFirstName = firstName;
        mRequesterLastName = lastName;
    }

    public Notification(String title, String hostName, Date time) {
        type = Type.EVENT_INVITE;
        mEventTitle = title;
        mHostName = hostName;
        mTime = time;
    }

    public Type getType() {
        return type;
    }

    public String getmRequesterFirstName() {
        return mRequesterFirstName;
    }

    public String getmRequesterLastName() {
        return mRequesterLastName;
    }

    public String getmEventTitle() {
        return mEventTitle;
    }

    public Date getmTime() {
        return mTime;
    }

    public String getmHostName() {
        return mHostName;
    }

    public static ArrayList<Notification> testData() {
        ArrayList<Notification> testData = new ArrayList<>();

        testData.add(new Notification("Borat", "Sagdiyev"));
        testData.add(new Notification("Borat Watchparty", "Borat", new Date(2020, 11, 13, 20, 30)));
        testData.add(new Notification("Hulk", "Smash"));
        testData.add(new Notification("Dead", "Pool"));
        testData.add(new Notification("Stranger Things Marathon", "Elle", new Date(2020, 11, 29, 11, 30)));
        testData.add(new Notification("Christmas Party!", "Santa", new Date(2020, 12, 24, 21, 0)));
        testData.add(new Notification("Alexandria", "Ocasio-Cortez"));
        testData.add(new Notification("Skydiving", "Usain", new Date(2021, 3, 24, 13, 30)));
        testData.add(new Notification("Bill", "Gates"));

        return testData;
    }

}
