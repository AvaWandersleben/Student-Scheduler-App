package com.example.cs_2340_student_scheduler_app.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.example.cs_2340_student_scheduler_app.MainActivity;
import com.example.cs_2340_student_scheduler_app.User;
import com.example.cs_2340_student_scheduler_app.UserDao;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
public class Home {
    private static ArrayList<Classes> classList = new ArrayList<>();
    private Classes associatedClass;
    private String title;
    private String dueDate;
    private boolean completed;
    private static Context context;
    public Home(Classes associatedClass, String title, String dueDate, boolean completed) {
        loadDB();
        this.associatedClass = associatedClass;
        this.title = title;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public Classes getAssociatedClass() {
        return associatedClass;
    }

    public void setAssociatedClass(Classes associatedClass) {
        this.associatedClass = associatedClass;
//        if (!classList.contains(associatedClass)) {
//            classList.add(associatedClass);
//        }
//        saveData();
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

    public int getClassNameLoc() {
        System.out.println("*");
        for (Classes c : classList) {
            System.out.println(c.getCourseName());
        }
        return classList.indexOf(associatedClass);
    }

    public static void setContext(Context contextNew) {
        context = contextNew;
    }

    public int compareDate(Home other) {
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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

    @Override
    public String toString() {
        return "Home{" +
                "associatedClass=" + associatedClass +
                ", title='" + title + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", completed=" + completed +
                '}';
    }

    public static void loadDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(0);
        Gson gson = new Gson();
        String json = user.classes;
        Type type = new TypeToken<ArrayList<Classes>>() {}.getType();
        classList = gson.fromJson(json, type);
        if (classList == null) {
            classList = new ArrayList<>();
        }
    }
}
