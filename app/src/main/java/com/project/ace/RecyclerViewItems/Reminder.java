package com.project.ace.RecyclerViewItems;

public class Reminder {
    private String reminderTitle;
    private String reminderDescription;
    private String reminderTime;
    private String reminderDate;
    private int reminderID;
    private String status;

    public Reminder(){
        //Empty constructor
    }

    public Reminder(String reminderTitle,String reminderDescription,String reminderDate,String reminderTime,int reminderID,String status){
        this.reminderTitle = reminderTitle;
        this.reminderDescription = reminderDescription;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.reminderID = reminderID;
        this.status = status;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public String getReminderDescription() {
        return reminderDescription;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public int getReminderID() {
        return reminderID;
    }

    public String getStatus() {
        return status;
    }
}
