package com.project.ace.RecyclerViewItems;

public class Reminder {
    private String reminderTitle;
    private String reminderDescription;
    private String reminderTime;
    private String reminderDate;

    public Reminder(){
        //Empty constructor
    }

    public Reminder(String reminderTitle,String reminderDescription,String reminderDate,String reminderTime){
        this.reminderTitle = reminderTitle;
        this.reminderDescription = reminderDescription;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
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
}
