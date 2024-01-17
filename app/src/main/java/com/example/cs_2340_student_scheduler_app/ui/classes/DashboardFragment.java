package com.example.cs_2340_student_scheduler_app.ui.classes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs_2340_student_scheduler_app.R;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardFragment extends Fragment {
    private FloatingActionButton buttonAdd;
    private ArrayList<Classes> classList = new ArrayList<>();

    private ArrayList<Integer> index = new ArrayList<>();


    private FragmentDashboardBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        index.add(0);
        loadData();
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        buttonAdd = root.findViewById(R.id.buttAdd);
        RecyclerView courseCards =root.findViewById(R.id.idClass);

        ClassAdapter classAdapter = new ClassAdapter(getContext(), classList, this, index);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        courseCards.setLayoutManager(linearLayoutManager);
        courseCards.setAdapter(classAdapter);


        NavController navController = NavHostFragment.findNavController(this);


        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("instructorEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Instruct Name: " +o+ index.get(0));
                if (!classList.isEmpty())
                    classList.set(index.get(0), classList.get(index.get(0))).setInstructor(o.toString());
                saveData();
//                classAdapter.notifyItemChanged(index.get(0));
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("timeEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Time Name: " +o + index.get(0));
                if (!classList.isEmpty())
                    classList.set(index.get(0), classList.get(index.get(0))).setTime(o.toString());
                saveData();
//                classAdapter.notifyItemChanged(index.get(0));
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
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                classList.add(new Classes("default", "default", "default"));
                saveData();
                index.set(0, classList.size() - 1);
                NavHostFragment.findNavController(DashboardFragment.this).navigate(R.id.action_navigation_dashboard_to_navigation_addClass);
            }
        });
        return root;

    }

    private void loadData() {
        Context context = getActivity();
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
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

        // checking below if the array list is empty or not
        if (classList == null) {
            // if the array list is empty
            // creating a new array list.
            classList = new ArrayList<>();
        }
    }

    private void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(classList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("courses", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}