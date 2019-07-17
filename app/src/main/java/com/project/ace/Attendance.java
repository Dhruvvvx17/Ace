package com.project.ace;

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

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getClassAttended() {
        return classAttended;
    }

    public void setClassAttended(int classAttended) {
        this.classAttended = classAttended;
    }

    public int getClassTotal() {
        return classTotal;
    }

    public void setClassTotal(int classTotal) {
        this.classTotal = classTotal;
    }
}
