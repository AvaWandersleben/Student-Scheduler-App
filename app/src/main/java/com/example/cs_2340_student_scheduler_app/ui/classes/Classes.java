package com.example.cs_2340_student_scheduler_app.ui.classes;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

public class Classes {
    private String courseName;
    private String time;
    private DayOfWeek[] days;
    private String section;
    private String location;
    private String roomNumber;

    private String instructor;

    public Classes() {
    }

    public Classes(String courseName, String time, String instructor, String daysOfWeek, String section, String location, String roomNumber) {
        this.section = section;
        this.location = location;
        this.roomNumber = roomNumber;
        this.courseName = courseName;
        this.time = time;
        this.instructor = instructor;
        String[] dayStr = daysOfWeek.split(",");
        days = new DayOfWeek[dayStr.length];
        for (int i = 0; i < dayStr.length; i++) {
            days[i] = DayOfWeek.valueOf(dayStr[i].toUpperCase());
        }
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

    public String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int count = day;
        if (days != null) {
            while (!contains(days, count)) {
                count++;
                if (count == 8) {
                    count = 1;
                }
            }
        }
        if (count == 1) return DayOfWeek.values()[6].toString();
        return DayOfWeek.values()[count - 2].toString();
    }

    public void setDays(String daysOfWeek) {
        String[] dayStr = daysOfWeek.split(",");
        days = new DayOfWeek[dayStr.length];
        for (int i = 0; i < dayStr.length; i++) {
            days[i] = DayOfWeek.valueOf(dayStr[i].toUpperCase());
        }
    }

    public String getDays() {
        String dayStr = "";
        if (days != null) {
            for (int i = 0; i < days.length; i++) {
                dayStr += days[i].toString();
                dayStr += ",";
            }
        }
        return dayStr;
    }

    public boolean contains (DayOfWeek[] arr, int day) {
        if (arr != null) {
            if (day == 1) {
                day = 6;
            } else {
                day = day - 2;
            }
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].ordinal() == day) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) return false;
        return ((Classes) o).courseName.equals(courseName);
    }
}
