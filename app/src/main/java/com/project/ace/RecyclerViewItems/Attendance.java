package com.project.ace.RecyclerViewItems;

public class Attendance {
    private String courseName;
    private String courseCode;
    private int target;
    private int classAttended;
    private int classTotal;

    public Attendance(){
        //Empty constructor
    }

    public Attendance(String courseName,String courseCode,int target,int classAttended,int classTotal){
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.target = target;
        this.classAttended = classAttended;
        this.classTotal = classTotal;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getTarget() {
        return target;
    }

    public int getClassAttended(){
        return classAttended;
    }

    public int getClassTotal(){
        return classTotal;
    }
}
