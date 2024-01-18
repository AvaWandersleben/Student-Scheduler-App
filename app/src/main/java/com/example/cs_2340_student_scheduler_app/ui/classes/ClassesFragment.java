package com.example.cs_2340_student_scheduler_app.ui.classes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cs_2340_student_scheduler_app.databinding.FragmentClassesBinding;
import com.example.cs_2340_student_scheduler_app.ui.assignments.AssignmentsFragmentDirections;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs_2340_student_scheduler_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ClassesFragment extends Fragment {
    private FloatingActionButton buttonAdd;
    private ArrayList<Classes> classList = new ArrayList<>();

    private ArrayList<Integer> index = new ArrayList<>();


    private FragmentClassesBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClassesViewModel classesViewModel =
                new ViewModelProvider(this).get(ClassesViewModel.class);

        index.add(0);
        loadData();
        binding = FragmentClassesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView courseCards =root.findViewById(R.id.idClass);

        ClassAdapter classAdapter = new ClassAdapter(getContext(), classList, this, index);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        courseCards.setLayoutManager(linearLayoutManager);
        courseCards.setAdapter(classAdapter);


        handleChanges();
        handleAddButton(root);
        return root;

    }

    private void handleAddButton(View root) {
        buttonAdd = root.findViewById(R.id.buttAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                classList.add(new Classes("default", "default", "default"));
                saveData();
                index.set(0, classList.size() - 1);
                ClassesFragmentDirections.ActionNavigationDashboardToNavigationAddClass action = ClassesFragmentDirections.actionNavigationDashboardToNavigationAddClass(index.get(0));
                NavHostFragment.findNavController(ClassesFragment.this).navigate(action);
            }
        });
    }

    private void handleChanges() {
        NavController navController = NavHostFragment.findNavController(this);


        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("instructorEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Instruct Name: " +o+ index.get(0));
                if (!classList.isEmpty())
                    classList.set(index.get(0), classList.get(index.get(0))).setInstructor(o.toString());
                saveData();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("timeEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Time Name: " +o + index.get(0));
                if (!classList.isEmpty())
                    classList.set(index.get(0), classList.get(index.get(0))).setTime(o.toString());
                saveData();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("courseEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Course Name: " +o+ index.get(0));
                if (!classList.isEmpty())
                    classList.set(index.get(0), classList.get(index.get(0))).setCourseName(o.toString());
                saveData();
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

    private void saveData() {
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(classList);
        editor.putString("courses", json);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}