package com.example.cs_2340_student_scheduler_app.ui.assignments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cs_2340_student_scheduler_app.databinding.FragmentAssignmentMenuBinding;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class AssignmentMenuFragment extends Fragment {

    private FragmentAssignmentMenuBinding binding;
    private EditText title;
    private EditText dueDate;
    private EditText associatedCourse;

    private ArrayList<Classes> classList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAssignmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        title = binding.editTitle;
        dueDate = binding.editDueDate;

        Spinner spinner = binding.classSpinner;
        ArrayList<String> classNames = new ArrayList<>();
        for (Classes c : classList) {
            classNames.add(c.getCourseName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        binding.submitButtEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString();
                String dueDateStr = dueDate.getText().toString();
                String associatedCourseStr = spinner.getSelectedItem().toString();

                NavController navController = NavHostFragment.findNavController(AssignmentMenuFragment.this);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("titleEdit", titleStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("dueDateEdit", dueDateStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("classEdit", associatedCourseStr);
                navController.popBackStack();
            }
        });
    }

    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("courses", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<Classes>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        classList = gson.fromJson(json, type);
        System.out.println(classList.size());

        // checking below if the array list is empty or not
        if (classList == null) {
            // if the array list is empty
            // creating a new array list.
            classList = new ArrayList<>();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}