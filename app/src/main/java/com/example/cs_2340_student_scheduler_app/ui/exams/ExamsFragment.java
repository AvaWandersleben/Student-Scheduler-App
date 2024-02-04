package com.example.cs_2340_student_scheduler_app.ui.exams;

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
import com.example.cs_2340_student_scheduler_app.databinding.FragmentExamsBinding;
import com.example.cs_2340_student_scheduler_app.ui.assignments.Assignment;
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

public class ExamsFragment extends Fragment {
    private FloatingActionButton buttonAdd;
    private ArrayList<Exam> examList = new ArrayList<>();

    private ArrayList<Integer> index = new ArrayList<>();


    private FragmentExamsBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExamViewModel examsViewModel =
                new ViewModelProvider(this).get(ExamViewModel.class);
        index.add(0);

        binding = FragmentExamsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loadDB();
        Assignment.setContext(getActivity());
        Assignment.loadDB();
        RecyclerView examCards = root.findViewById(R.id.idExams);

        ExamAdapter examAdapter = new ExamAdapter(getContext(), examList, this, index);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        examCards.setLayoutManager(linearLayoutManager);
        examCards.setAdapter(examAdapter);

        setUpSpinner(examAdapter);
        updateChanges(examAdapter);
        setUpAddButton(root);
        return root;

    }

    private void setUpAddButton(View root) {
        buttonAdd = root.findViewById(R.id.buttAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateDB();
                index.set(0, examList.size());
                int indexPar = index.get(0);
                ExamsFragmentDirections.ActionNavigationExamsToNavigationExamsMenuFragment action = ExamsFragmentDirections.actionNavigationExamsToNavigationExamsMenuFragment(indexPar);
                NavHostFragment.findNavController(ExamsFragment.this).navigate(action);
            }
        });
    }

    private void updateChanges(ExamAdapter assignmentAdapter) {
        NavController navController = NavHostFragment.findNavController(this);


        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("titleEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                if (!examList.isEmpty() && index.get(0) < examList.size())
                    examList.set(index.get(0), examList.get(index.get(0))).setTitle(o.toString());
                updateDB();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("dateEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                if (!examList.isEmpty() && index.get(0) < examList.size())
                    examList.set(index.get(0), examList.get(index.get(0))).setDate(o.toString());
                updateDB();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("timeEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                if (!examList.isEmpty() && index.get(0) < examList.size())
                    examList.set(index.get(0), examList.get(index.get(0))).setTime(o.toString());
                updateDB();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("locEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                if (!examList.isEmpty() && index.get(0) < examList.size())
                    examList.set(index.get(0), examList.get(index.get(0))).setLocation(o.toString());
                updateDB();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("classEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                if (!examList.isEmpty() && index.get(0) < examList.size())
                    examList.set(index.get(0), examList.get(index.get(0))).setAssociatedClass(new Classes(o.toString()));
                updateDB();
            }
        });
    }

    private void setUpSpinner(ExamAdapter examAdapter) {
        Spinner spinner = binding.sortSpinnerExam;
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
                if (examList.isEmpty()) return;
                if (position == 0) {
                    sortDueDate();
                } else {
                    sortCourseName();
                }
                updateDB();
                examAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(MainActivity.currUser);
        Gson gson = new Gson();
        user.exams = gson.toJson(examList);
        userDao.updateUsers(user);
    }

    public void loadDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(MainActivity.currUser);
        Gson gson = new Gson();
        String json = user.exams;
        Type type = new TypeToken<ArrayList<Exam>>() {}.getType();
        examList = gson.fromJson(json, type);
        if (examList == null) {
            examList = new ArrayList<>();
        }
    }

    public void sortDueDate() {
        for (int i = 0; i < examList.size() - 1; i++) {
            for (int j = 0; j < examList.size() - 1 - i; j++) {
                if (j + 1 < examList.size())
                    if (examList.get(j).compareDate(examList.get(j + 1)) > 0) {
                        Exam temp = examList.get(j);
                        examList.set(j, examList.get(j + 1));
                        examList.set(j + 1, temp);
                    }
            }
        }
    }

    public void sortCourseName() {
        for (int i = 0; i < examList.size() - 1; i++) {
            for (int j = 0; j < examList.size() - 1 - i; j++) {
                if (examList.get(j).getTitle().compareTo(examList.get(j + 1).getTitle()) > 0) {
                    Exam temp = examList.get(j);
                    examList.set(j, examList.get(j + 1));
                    examList.set(j + 1, temp);
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