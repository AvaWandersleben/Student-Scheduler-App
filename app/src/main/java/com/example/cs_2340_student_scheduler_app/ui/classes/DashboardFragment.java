package com.example.cs_2340_student_scheduler_app.ui.classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs_2340_student_scheduler_app.R;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardFragment extends Fragment {
    private LinearLayoutCompat cards;
    private FloatingActionButton buttonAdd;
    private FloatingActionButton buttonDelete;
    private ArrayList<Classes> classList = new ArrayList<>();

    private ArrayList<Integer> index = new ArrayList<>();


    private FragmentDashboardBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        index.add(0);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        buttonAdd = root.findViewById(R.id.butAd);
        buttonDelete = root.findViewById(R.id.deleteBut);
        RecyclerView courseCards =root.findViewById(R.id.idClass);

        // Here, we have created new array list and added data to it
//        classList.add(new Classes("Math", "230", "Mr Math"));
//        classList.add(new Classes("Science", "740", "Mr Science"));

        ClassAdapter classAdapter = new ClassAdapter(getContext(), classList, this, index);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        courseCards.setLayoutManager(linearLayoutManager);
        courseCards.setAdapter(classAdapter);

//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                TextView classText = root.findViewById(R.id.className);
////                TextView timeText = root.findViewById(R.id.timeText);
////                TextView instructor = root.findViewById(R.id.instructName);
//
//                //classList.remove(new Classes(classText.getText().toString(), Integer.parseInt(timeText.getText().toString()), instructor.getText().toString()));
//            }
//        });


        NavController navController = NavHostFragment.findNavController(this);
        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("course").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println(o);
                classList.add(new Classes(o.toString(), "default", "default"));
                classAdapter.notifyItemInserted(classList.size() - 1);
            }
        });
        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("time").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println(o);
                classList.set(classList.size() - 1, classList.get(classList.size() - 1)).setTime(o.toString());
                classAdapter.notifyItemChanged(classList.size() - 1);
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("instructor").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println(o);
                classList.set(classList.size() - 1, classList.get(classList.size() - 1)).setInstructor(o.toString());
                classAdapter.notifyItemChanged(classList.size() - 1);
            }
        });


        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("instructorEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println(o);
                classList.set(index.get(0), classList.get(index.get(0))).setInstructor(o.toString());
                classAdapter.notifyItemChanged(index.get(0));
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("timeEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println(o);
                classList.set(index.get(0), classList.get(index.get(0))).setTime(o.toString());
                classAdapter.notifyItemChanged(index.get(0));
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("courseEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println(o);
                classList.set(index.get(0), classList.get(index.get(0))).setCourseName(o.toString());
                classAdapter.notifyItemChanged(index.get(0));
                classList.remove(classList.size() - 1);
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DashboardFragment.this).navigate(R.id.action_navigation_dashboard_to_navigation_addClass);
                //String className = DashboardFragmentArgs.fromBundle(getArguments()).getClassName();
//                String instructName = DashboardFragmentArgs.fromBundle(getArguments()).getInstructName();
//                String timeText = DashboardFragmentArgs.fromBundle(getArguments()).getTime();
//                String className = "hi";
//                String timeText = "5:20";
//                String instructName = "Mr. Bob";
//                classList.add(new Classes(className, timeText, instructName));
//                classAdapter.notifyItemInserted(classList.size() - 1);
            }
        });
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

    }

    private void makeSelections() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}