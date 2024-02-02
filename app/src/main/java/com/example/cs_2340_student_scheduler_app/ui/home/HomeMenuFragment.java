package com.example.cs_2340_student_scheduler_app.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cs_2340_student_scheduler_app.ManipulateData;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentHomeMenuBinding;
import com.example.cs_2340_student_scheduler_app.ui.assignments.Assignment;
import com.example.cs_2340_student_scheduler_app.ui.assignments.AssignmentMenuFragment;
import com.example.cs_2340_student_scheduler_app.ui.assignments.AssignmentMenuFragmentArgs;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
public class HomeMenuFragment extends Fragment{
    private FragmentHomeMenuBinding binding;
    private EditText title;
    private EditText dueDate;

    private ArrayList<Classes> classList;
    private ArrayList<Home> todo;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        int index = HomeMenuFragmentArgs.fromBundle(getArguments()).getIndex();
        title = binding.editTitle;
        dueDate = binding.editDueDate;
        title.setText(todo.get(index).getTitle());
        dueDate.setText(todo.get(index).getDueDate());

        Spinner spinner = binding.classSpinner;
        ArrayList<String> classNames = new ArrayList<>();
        for (Classes c : classList) {
            classNames.add(c.getCourseName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(todo.get(index).getClassNameLoc());
        System.out.println(todo.get(index).getClassNameLoc());





        binding.submitButtEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString();
                String dueDateStr = dueDate.getText().toString();
                String associatedCourseStr = spinner.getSelectedItem().toString();
                boolean goodData = true;
                if (!ManipulateData.validateDate(dueDateStr)) {
                    goodData = false;
                }

                NavController navController = NavHostFragment.findNavController(HomeMenuFragment.this);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("titleEdit", titleStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("dueDateEdit", dueDateStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("classEdit", associatedCourseStr);
                if (goodData) {
                    navController.popBackStack();
                } else {
                    String message = "Date must be mm/dd/yyyy format.";
                    Toast alert = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
                    alert.show();
                }
            }
        });
    }


    private void loadData() {
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("courses", null);
        Type type = new TypeToken<ArrayList<Classes>>() {}.getType();
        Type type2 = new TypeToken<ArrayList<Home>>() {}.getType();
        String json2 = sharedPreferences.getString("todo", null);
        todo = gson.fromJson(json2, type2);
        classList = gson.fromJson(json, type);
        if (classList == null) {
            classList = new ArrayList<>();
        }
        if (todo == null) {
            todo = new ArrayList<>();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
