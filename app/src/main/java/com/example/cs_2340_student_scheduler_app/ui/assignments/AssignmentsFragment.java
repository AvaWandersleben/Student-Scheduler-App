package com.example.cs_2340_student_scheduler_app.ui.assignments;


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

import com.example.cs_2340_student_scheduler_app.MainActivity;
import com.example.cs_2340_student_scheduler_app.R;
import com.example.cs_2340_student_scheduler_app.User;
import com.example.cs_2340_student_scheduler_app.UserDao;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentAssignmentsBinding;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
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

    public static AssignmentAdapter assignmentAdapter;


    private FragmentAssignmentsBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AssignmentsViewModel classesViewModel =
                new ViewModelProvider(this).get(AssignmentsViewModel.class);
        index.add(0);

        binding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loadDB();
        Assignment.setContext(getActivity());

        RecyclerView assignmentCards = root.findViewById(R.id.idAssignments);

        assignmentAdapter = new AssignmentAdapter(getContext(), assignmentList, this, index, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        assignmentCards.setLayoutManager(linearLayoutManager);
        assignmentCards.setAdapter(assignmentAdapter);

        setUpSpinner(assignmentAdapter);
        updateChanges(assignmentAdapter);
        setUpAddButton(root);

        binding.incompleteSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                AssignmentAdapter incompleteAdapter = new AssignmentAdapter(getContext(), getIncompleteAssignment(assignmentList), this, index, true);
                assignmentCards.setAdapter(incompleteAdapter);
            } else {
                assignmentCards.setAdapter(assignmentAdapter);
            }
        }));
        return root;

    }

    private void setUpAddButton(View root) {
        buttonAdd = root.findViewById(R.id.buttAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateDB();
                index.set(0, assignmentList.size());
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
                if (!assignmentList.isEmpty() && index.get(0) < assignmentList.size()) {
                    assignmentAdapter.notifyItemChanged(index.get(0));
                }
                updateDB();
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
                if (assignmentList.isEmpty() || index.get(0) < assignmentList.size()) return;
                if (position == 0) {
                    sortDueDate();
                } else {
                    sortCourseName();
                }
                updateDB();
                assignmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public void updateDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(MainActivity.currUser);
        Gson gson = new Gson();
        user.assignments = gson.toJson(assignmentList);
        userDao.updateUsers(user);
    }

    public void loadDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(MainActivity.currUser);
        Gson gson = new Gson();
        String json = user.assignments;
        Type type = new TypeToken<ArrayList<Assignment>>() {}.getType();
        assignmentList = gson.fromJson(json, type);
        if (assignmentList == null) {
            assignmentList = new ArrayList<>();
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

    public ArrayList<Assignment> getIncompleteAssignment(ArrayList<Assignment> arr) {
        ArrayList<Assignment> newArr = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            if (!arr.get(i).isCompleted()) {
                newArr.add(arr.get(i));
            }
        }
        return newArr;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}