package com.WKNS.gather.databaseModels;

import com.google.firebase.firestore.Exclude;

public class Budget {
    @Exclude private String budgetID;

    private String name;
    private String description;
    private float cost;
    private boolean purchased;

    public Budget(){ }

    public Budget(String name, String description, float cost, boolean purchased){
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.purchased = purchased;
    }

    public void getID(String key){
        this.budgetID = key;
    }

    public String getKey(){
        return this.budgetID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getCost() {
        return cost;
    }

    public boolean getPurchased(){
        return purchased;
    }
}
