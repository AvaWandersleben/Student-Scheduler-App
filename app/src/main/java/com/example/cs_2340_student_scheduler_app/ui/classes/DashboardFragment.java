package com.example.cs_2340_student_scheduler_app.ui.classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    private FloatingActionButton buttonAdd;
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
                classList.set(index.get(0), classList.get(index.get(0))).setInstructor(o.toString());
//                classAdapter.notifyItemChanged(index.get(0));
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("timeEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Time Name: " +o + index.get(0));
                classList.set(index.get(0), classList.get(index.get(0))).setTime(o.toString());
//                classAdapter.notifyItemChanged(index.get(0));
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("courseEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Course Name: " +o+ index.get(0));
                classList.set(index.get(0), classList.get(index.get(0))).setCourseName(o.toString());
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                classList.add(new Classes("default", "default", "default"));
                index.set(0, classList.size() - 1);
                NavHostFragment.findNavController(DashboardFragment.this).navigate(R.id.action_navigation_dashboard_to_navigation_addClass);
            }
        });
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}