package com.example.cs_2340_student_scheduler_app.ui.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Class {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "course_title")
    public String title;

    @ColumnInfo(name = "course_time")
    public String time;
}
