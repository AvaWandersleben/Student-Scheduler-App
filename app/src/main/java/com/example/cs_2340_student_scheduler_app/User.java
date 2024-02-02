package com.example.cs_2340_student_scheduler_app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "classes")
    public String classes;

    @ColumnInfo(name = "assignments")
    public String assignments;

    @ColumnInfo(name = "exams")
    public String exams;

    @ColumnInfo(name = "tasks")
    public String tasks;
}
