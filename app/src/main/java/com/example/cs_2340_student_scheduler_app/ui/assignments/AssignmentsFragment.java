package com.example.cs_2340_student_scheduler_app.ui.assignments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs_2340_student_scheduler_app.R;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentAssignmentsBinding;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentDashboardBinding;
import com.example.cs_2340_student_scheduler_app.ui.classes.ClassAdapter;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
import com.example.cs_2340_student_scheduler_app.ui.classes.DashboardFragment;
import com.example.cs_2340_student_scheduler_app.ui.classes.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AssignmentsFragment extends Fragment {
    private FloatingActionButton buttonAdd;
    private ArrayList<Assignment> assignmentList = new ArrayList<>();

    private ArrayList<Integer> index = new ArrayList<>();


    private FragmentAssignmentsBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        index.add(0);

        binding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        buttonAdd = root.findViewById(R.id.buttAdd);
        RecyclerView assignmentCards = root.findViewById(R.id.idAssignments);

        AssignmentAdapter assignmentAdapter = new AssignmentAdapter(getContext(), assignmentList, this, index);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        assignmentCards.setLayoutManager(linearLayoutManager);
        assignmentCards.setAdapter(assignmentAdapter);


        NavController navController = NavHostFragment.findNavController(this);


        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("titleEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Assignment Name: " +o+ index.get(0));
                assignmentList.set(index.get(0), assignmentList.get(index.get(0))).setTitle(o.toString());
//                classAdapter.notifyItemChanged(index.get(0));
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("dueDateEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Assignment Name: " +o + index.get(0));
                assignmentList.set(index.get(0), assignmentList.get(index.get(0))).setDueDate(o.toString());
//                classAdapter.notifyItemChanged(index.get(0));
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("classEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Assignment Name: " +o+ index.get(0));
                assignmentList.set(index.get(0), assignmentList.get(index.get(0))).setAssociatedClass(new Classes(o.toString(), "default", "default"));
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                assignmentList.add(new Assignment(new Classes(), "default", "default"));
                index.set(0, assignmentList.size() - 1);
                NavHostFragment.findNavController(AssignmentsFragment.this).navigate(R.id.action_navigation_notifications_to_navigation_assignment_menu_fragment);
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