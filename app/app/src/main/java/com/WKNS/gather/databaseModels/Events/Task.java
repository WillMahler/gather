package com.WKNS.gather.databaseModels.Events;

import com.google.firebase.firestore.Exclude;

public class Task {
    @Exclude private String taskID;

    private String taskName;
    private String taskDescription;
    private String assigneeFirstName;
    private String assigneeLastName;

    public Task() { }

    /**
     * A task is a simple object that represents a task of an event. It is assumed that only
     * one person can be assigned to a task.
     * @param taskName              Name of task
     * @param taskDescription       Description of task
     * @param assigneeFirstName     First name of assignee
     * @param assigneeLastName      Last name of assignee
     */
    public Task(String taskName, String taskDescription, String assigneeFirstName, String assigneeLastName){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.assigneeFirstName = assigneeFirstName;
        this.assigneeLastName = assigneeLastName;
    }

    //Setters
    public void setID(String taskID) { this.taskID = taskID; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }
    public void setAssigneeFirstName(String assigneeFirstName) { this.assigneeFirstName = assigneeFirstName; }
    public void setAssigneeLastName(String assigneeLastName) { this.assigneeLastName = assigneeLastName; }

    //Getters
    public String getTaskID(){ return this.taskID; }
    public String getTaskName(){ return this.taskName; }
    public String getTaskDescription(){ return this.taskDescription; }
    public String getAssigneeFirstName(){ return this.assigneeFirstName; }
    public String getAssigneeLastName(){ return this.assigneeLastName; }
}
