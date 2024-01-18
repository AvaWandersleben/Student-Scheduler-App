package com.example.cs_2340_student_scheduler_app.ui.assignments;

import androidx.annotation.Nullable;

import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;

public class Assignment {
    private Classes associatedClass;
    private String title;
    private String dueDate;

    public Assignment(Classes associatedClass, String title, String dueDate) {
        this.associatedClass = associatedClass;
        this.title = title;
        this.dueDate = dueDate;
    }

    public Classes getAssociatedClass() {
        return associatedClass;
    }

    public void setAssociatedClass(Classes associatedClass) {
        this.associatedClass = associatedClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getClassName() {
        return (associatedClass == null ? null : associatedClass.getCourseName());
    }

    public int compareDate(Assignment other) {
        String[] thisDate = dueDate.split("/");
        String[] otherDate = other.dueDate.split("/");
        Integer x = compareIndex(thisDate, 2, otherDate);
        if (x != null) return x;
        Integer x1 = compareIndex(thisDate, 0, otherDate);
        if (x1 != null) return x1;
        Integer x2 = compareIndex(thisDate, 1, otherDate);
        if (x2 != null) return x2;
        return 0;
    }

    @Nullable
    private static Integer compareIndex(String[] thisDate, int x, String[] otherDate) {
        if (Integer.parseInt(thisDate[x]) > Integer.parseInt(otherDate[x])) {
            return 1;
        } else if (Integer.parseInt(thisDate[x]) < Integer.parseInt(otherDate[x])) {
            return -1;
        }
        return null;
    }
}
