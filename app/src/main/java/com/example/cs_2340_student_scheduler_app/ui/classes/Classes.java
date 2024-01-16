package com.example.cs_2340_student_scheduler_app.ui.classes;

public class Classes {
    private String courseName;

    /*
    Store time as an integer value:
    - 123 = 1:23
    - 1230 = 12:30
    - 2314 = 23:14 = 11:14
     */
    private int time;

    private String instructor;

    public Classes(String courseName, int time, String instructor) {
        this.courseName = courseName;
        this.time = time;
        this.instructor = instructor;
    }

    public int getTime() {
        return time;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setTime(int time) {
        this.time = time;
    }

    //returns time as a formatted string
    public String getTimeString() {
        String postMark = (time > 1200 ? "pm" : "am");
        return time / 100 + ":" + time % 100 + postMark;
    }
}
