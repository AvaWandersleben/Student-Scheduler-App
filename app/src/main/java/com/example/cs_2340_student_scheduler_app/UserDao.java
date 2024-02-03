package com.example.cs_2340_student_scheduler_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE uid IN (:id)")
    User getUser(int id);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Update
    void updateUsers(User... users);

    @Query("SELECT COUNT(*) FROM user")
    int userCount();
}
