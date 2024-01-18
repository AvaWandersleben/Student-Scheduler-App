package com.example.cs_2340_student_scheduler_app.ui.exams;

import com.example.cs_2340_student_scheduler_app.ui.assignments.Assignment;

public class Exam {
    private String title;
    private String date;
    private String location;
    private String time;

    public Exam() {
        title = "default";
        date = "01/01/2000";
        location = "home";
        time = "2:00";
    }

    public Exam(String title, String date, String location, String time) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int compareDate(Exam other) {
        String[] thisDate = date.split("/");
        String[] otherDate = other.date.split("/");
        Integer x = compareIndex(thisDate, 2, otherDate);
        if (x != null) return x;
        Integer x1 = compareIndex(thisDate, 0, otherDate);
        if (x1 != null) return x1;
        Integer x2 = compareIndex(thisDate, 1, otherDate);
        if (x2 != null) return x2;
        return 0;
    }

    private static Integer compareIndex(String[] thisDate, int x, String[] otherDate) {
        if (Integer.parseInt(thisDate[x]) > Integer.parseInt(otherDate[x])) {
            return 1;
        } else if (Integer.parseInt(thisDate[x]) < Integer.parseInt(otherDate[x])) {
            return -1;
        }
        return null;
    }
}
