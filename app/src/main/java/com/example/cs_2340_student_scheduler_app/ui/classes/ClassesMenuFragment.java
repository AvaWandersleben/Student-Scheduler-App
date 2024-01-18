package com.example.cs_2340_student_scheduler_app.ui.classes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cs_2340_student_scheduler_app.databinding.FragmentClassesMenuBinding;
import com.example.cs_2340_student_scheduler_app.ui.assignments.Assignment;
import com.example.cs_2340_student_scheduler_app.ui.assignments.AssignmentMenuFragmentArgs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ClassesMenuFragment extends Fragment {

    private FragmentClassesMenuBinding binding;
    private EditText courseName;
    private EditText instructName;
    private EditText timeText;

    private ArrayList<Classes> classList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClassesMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int index = ClassesMenuFragmentArgs.fromBundle(getArguments()).getIndex();
        loadData();
        courseName = binding.editTextClassName;
        instructName = binding.editTextInstructorName;
        timeText = binding.editTextTime;
        timeText.setText(classList.get(index).getTime());
        instructName.setText(classList.get(index).getInstructor());
        courseName.setText(classList.get(index).getCourseName());


        binding.submitButt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String courseNameStr = courseName.getText().toString();
                String instructNameStr = instructName.getText().toString();
                String timeTextStr = timeText.getText().toString();

                NavController navController = NavHostFragment.findNavController(ClassesMenuFragment.this);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("courseEdit", courseNameStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("timeEdit", timeTextStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("instructorEdit", instructNameStr);
                navController.popBackStack();
            }
        });
    }

    private void loadData() {
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("courses", null);
        Type type = new TypeToken<ArrayList<Classes>>() {}.getType();
        classList = gson.fromJson(json, type);
        if (classList == null) {
            classList = new ArrayList<>();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}