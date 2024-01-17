package com.example.cs_2340_student_scheduler_app.ui.classes;

public class Classes {
    private String courseName;

    /*
    Store time as an integer value:
    - 123 = 1:23
    - 1230 = 12:30
    - 2314 = 23:14 = 11:14
     */
    private String time;

    private String instructor;

    public Classes(String courseName, String time, String instructor) {
        this.courseName = courseName;
        this.time = time;
        this.instructor = instructor;
    }

    public String getTime() {
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

    public void setTime(String time) {
        this.time = time;
    }

    //returns time as a formatted string
//    public String getTimeString() {
//        String postMark = (time > 1200 ? "pm" : "am");
//        return (time / 100) % 12 + ":" + time % 100 + postMark;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) return false;
        Classes other = (Classes) o;
        return other.courseName.equals(courseName) && other.time == time && other.instructor.equals(instructor);
    }
}
