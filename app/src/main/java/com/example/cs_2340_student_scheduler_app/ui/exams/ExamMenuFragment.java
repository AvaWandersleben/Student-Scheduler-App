package com.example.cs_2340_student_scheduler_app.ui.exams;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cs_2340_student_scheduler_app.MainActivity;
import com.example.cs_2340_student_scheduler_app.ManipulateData;
import com.example.cs_2340_student_scheduler_app.User;
import com.example.cs_2340_student_scheduler_app.UserDao;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentClassesMenuBinding;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentExamsMenuBinding;
import com.example.cs_2340_student_scheduler_app.ui.assignments.Assignment;
import com.example.cs_2340_student_scheduler_app.ui.assignments.AssignmentMenuFragmentArgs;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
import com.example.cs_2340_student_scheduler_app.ui.classes.ClassesMenuFragment;
import com.example.cs_2340_student_scheduler_app.ui.classes.ClassesMenuFragmentArgs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ExamMenuFragment extends Fragment {

    private FragmentExamsMenuBinding binding;
    private EditText titleText;
    private EditText dateText;
    private EditText timeText;
    private EditText locText;

    private ArrayList<Exam> examList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExamsMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int index = ClassesMenuFragmentArgs.fromBundle(getArguments()).getIndex();
        loadDB();
        titleText = binding.editTitle;
        timeText = binding.examTime;
        dateText = binding.editDueDate;
        locText = binding.editLoc;
        if (index < examList.size()) {
            titleText.setText(examList.get(index).getTitle());
            timeText.setText(examList.get(index).getTime());
            dateText.setText(examList.get(index).getDate());
            locText.setText(examList.get(index).getLocation());
        }
        binding.submitButtEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                examList.add(new Exam());
                updateDB();
                String titleNameStr = titleText.getText().toString();
                String dateTextStr = dateText.getText().toString();
                String timeTextStr = timeText.getText().toString();
                String locTextStr = locText.getText().toString();
                boolean goodData = true;
                if (!ManipulateData.validateDate(dateTextStr) ||
                        !ManipulateData.validateTime(timeTextStr)) {
                    goodData = false;
                }

                NavController navController = NavHostFragment.findNavController(ExamMenuFragment.this);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("titleEdit", titleNameStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("timeEdit", timeTextStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("dateEdit", dateTextStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("locEdit", locTextStr);
                if (goodData) {
                    navController.popBackStack();
                } else {
                    String message = "";
                    if (!ManipulateData.validateDate(dateTextStr)) {
                        message += "Date must be mm/dd/yyyy format. ";
                    }
                    if (!ManipulateData.validateTime(timeTextStr)) {
                        message += "Invalid time input.";
                    }
                    Toast alert = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
                    alert.show();
                }
            }
        });
    }

    public void loadDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(MainActivity.currUser);
        Gson gson = new Gson();
        String json = user.exams;
        Type type = new TypeToken<ArrayList<Exam>>() {}.getType();
        examList = gson.fromJson(json, type);
        if (examList == null) {
            examList = new ArrayList<>();
        }
    }

    public void updateDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(MainActivity.currUser);
        Gson gson = new Gson();
        user.exams = gson.toJson(examList);
        userDao.updateUsers(user);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}