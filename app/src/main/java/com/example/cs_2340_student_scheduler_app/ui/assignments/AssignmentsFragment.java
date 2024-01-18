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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs_2340_student_scheduler_app.R;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentAssignmentsBinding;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
import com.example.cs_2340_student_scheduler_app.ui.classes.ClassesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
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
        ClassesViewModel classesViewModel =
                new ViewModelProvider(this).get(ClassesViewModel.class);
        index.add(0);

        binding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loadData();
        Assignment.setContext(getActivity());
        Assignment.loadData();
        RecyclerView assignmentCards = root.findViewById(R.id.idAssignments);

        AssignmentAdapter assignmentAdapter = new AssignmentAdapter(getContext(), assignmentList, this, index);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        assignmentCards.setLayoutManager(linearLayoutManager);
        assignmentCards.setAdapter(assignmentAdapter);

        setUpSpinner(assignmentAdapter);
        updateChanges(assignmentAdapter);
        setUpAddButton(root);
        return root;

    }

    private void setUpAddButton(View root) {
        buttonAdd = root.findViewById(R.id.buttAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                assignmentList.add(new Assignment(new Classes(), "default", "01/01/2000"));
                saveData();
                index.set(0, assignmentList.size() - 1);
                int indexPar = index.get(0);
                AssignmentsFragmentDirections.ActionNavigationNotificationsToNavigationAssignmentMenuFragment action = AssignmentsFragmentDirections.actionNavigationNotificationsToNavigationAssignmentMenuFragment(indexPar);
                NavHostFragment.findNavController(AssignmentsFragment.this).navigate(action);
            }
        });
    }

    private void updateChanges(AssignmentAdapter assignmentAdapter) {
        NavController navController = NavHostFragment.findNavController(this);


        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("titleEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Assignment Name: " +o+ index.get(0));
                if (!assignmentList.isEmpty())
                    assignmentList.set(index.get(0), assignmentList.get(index.get(0))).setTitle(o.toString());
                saveData();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("dueDateEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Assignment Name: " +o + index.get(0));
                if (!assignmentList.isEmpty())
                    assignmentList.set(index.get(0), assignmentList.get(index.get(0))).setDueDate(o.toString());
                saveData();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("classEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Assignment Name: " +o+ index.get(0));
                if (!assignmentList.isEmpty())
                    assignmentList.set(index.get(0), assignmentList.get(index.get(0))).setAssociatedClass(new Classes(o.toString(), "default", "default"));
                if (binding.sortSpinner.getSelectedItemPosition() == 0) {
                    System.out.println("due date");
                    sortDueDate();
                } else {
                    sortCourseName();
                }
                saveData();
                assignmentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpSpinner(AssignmentAdapter assignmentAdapter) {
        Spinner spinner = binding.sortSpinner;
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                        getActivity(),
                        R.array.sorter_array,
                        android.R.layout.simple_spinner_item

                );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sortAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (assignmentList.isEmpty()) return;
                if (position == 0) {
                    System.out.println("due date");
                    sortDueDate();
                } else {
                    sortCourseName();
                }
                saveData();
                assignmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadData() {
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("assignments", null);
        Type type = new TypeToken<ArrayList<Assignment>>() {}.getType();
        assignmentList = gson.fromJson(json, type);
        if (assignmentList == null) {
            assignmentList = new ArrayList<>();
        }
    }

    private void saveData() {
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(assignmentList);
        editor.putString("assignments", json);
        editor.apply();
    }

    public void sortDueDate() {
        for (int i = 0; i < assignmentList.size() - 1; i++) {
            for (int j = 0; j < assignmentList.size() - 1 - i; j++) {
                if (j + 1 < assignmentList.size())
                if (assignmentList.get(j).compareDate(assignmentList.get(j + 1)) > 0) {
                    Assignment temp = assignmentList.get(j);
                    assignmentList.set(j, assignmentList.get(j + 1));
                    assignmentList.set(j + 1, temp);
                }
            }
        }
    }

    public void sortCourseName() {
        for (int i = 0; i < assignmentList.size() - 1; i++) {
            for (int j = 0; j < assignmentList.size() - 1 - i; j++) {
                if (assignmentList.get(j).getClassName().compareTo(assignmentList.get(j + 1).getClassName()) > 0) {
                    Assignment temp = assignmentList.get(j);
                    assignmentList.set(j, assignmentList.get(j + 1));
                    assignmentList.set(j + 1, temp);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}