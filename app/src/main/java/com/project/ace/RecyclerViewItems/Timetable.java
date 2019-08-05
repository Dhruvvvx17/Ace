package com.project.ace.RecyclerViewItems;

public class Timetable {

    private String day;
    private String lectureCode;
    private String lectureTitle;
    private String lectureStartTime;
    private String lectureEndTime;
    private String lectureRoom;
    private String lectureProfessor;
    private int lectureNotificationID;
    private int checkStart;


    public Timetable(){
        //Mandatory empty constructor
    }

    public Timetable(String day,String lectureCode,String lectureTitle,String lectureStartTime,String lectureEndTime,String lectureProfessor
            ,String lectureRoom,int lectureNotificationID, int checkStart){
        this.day = day;
        this.lectureCode = lectureCode;
        this.lectureTitle = lectureTitle;
        this.lectureStartTime = lectureStartTime;
        this.lectureEndTime = lectureEndTime;
        this.lectureProfessor = lectureProfessor;
        this.lectureRoom = lectureRoom;
        this.lectureNotificationID = lectureNotificationID;
        this.checkStart = checkStart;
    }

    public String getDay() {
        return day;
    }

    public String getLectureCode() {
        return lectureCode;
    }

    public String getLectureTitle() {
        return lectureTitle;
    }

    public String getLectureStartTime() {
        return lectureStartTime;
    }

    public String getLectureEndTime() {
        return lectureEndTime;
    }

    public String getLectureRoom() {
        return lectureRoom;
    }

    public String getLectureProfessor() {
        return lectureProfessor;
    }

    public int getCheckStart() {
        return checkStart;
    }

    public int getLectureNotificationID() {
        return lectureNotificationID;
    }
}
