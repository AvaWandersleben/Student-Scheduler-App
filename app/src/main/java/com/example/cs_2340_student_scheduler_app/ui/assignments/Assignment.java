package com.example.cs_2340_student_scheduler_app.ui.assignments;

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
}
