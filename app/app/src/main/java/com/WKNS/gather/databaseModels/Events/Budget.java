package com.WKNS.gather.databaseModels.Events;

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

    //Setters
    public void setBudgetID(String budgetID){
        this.budgetID = budgetID;
    }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCost(float cost) { this.cost = cost; }
    public void setPurchased(boolean purchased) { this.purchased = purchased; }

    //Getters
    public String getBudgetIDID(){
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
