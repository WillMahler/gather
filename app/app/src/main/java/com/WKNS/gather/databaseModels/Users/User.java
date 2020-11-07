package com.WKNS.gather.databaseModels.Users;

// Represents a user in the Users collection
public class User {
    private String email, firstName, lastName;

    // Constructors
    public User() {}

    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters + Setters
    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
