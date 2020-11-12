package com.WKNS.gather.testData;

import java.util.ArrayList;

public class User {
    private String email, firstName, lastName;

    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public static ArrayList<User> testUsers() {
        ArrayList<User> testData = new ArrayList<>();

        testData.add(new User("fake@email.com", "Borat", "Sagdiyev"));
        testData.add(new User("fake@email.com", "Billie", "Eilish"));
        testData.add(new User("fake@email.com", "Gordon", "Ramsay"));
        testData.add(new User("fake@email.com", "Ryan", "Reynolds"));
        testData.add(new User("fake@email.com", "Jen", "Garner"));
        testData.add(new User("fake@email.com", "Santa", "Claus"));

        return testData;
    }
}