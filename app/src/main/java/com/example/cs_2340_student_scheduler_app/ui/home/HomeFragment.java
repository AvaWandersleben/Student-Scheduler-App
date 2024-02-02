package com.example.cs_2340_student_scheduler_app.ui.home;

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
import com.example.cs_2340_student_scheduler_app.databinding.FragmentHomeBinding;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
import com.example.cs_2340_student_scheduler_app.ui.classes.ClassesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private FloatingActionButton buttonAdd;
    private ArrayList<Home> todoList = new ArrayList<>();

    private ArrayList<Integer> index = new ArrayList<>();
    public static HomeAdapter homeAdapter;


    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClassesViewModel classesViewModel =
                new ViewModelProvider(this).get(ClassesViewModel.class);
        index.add(0);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loadData();
        Home.setContext(getActivity());
        Home.loadData();
        RecyclerView homeCards = root.findViewById(R.id.idAssignments);

        homeAdapter = new HomeAdapter(getContext(), todoList, this, index, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        homeCards.setLayoutManager(linearLayoutManager);
        homeCards.setAdapter(homeAdapter);

        setUpSpinner(homeAdapter);
        updateChanges(homeAdapter);
        setUpAddButton(root);

        binding.incompleteSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                ArrayList<Home> completedList = getCompletedHome(todoList);
                HomeAdapter completedAdapter = new HomeAdapter(getContext(), completedList, this, index, false);
                homeCards.setAdapter(completedAdapter);
            } else {
                homeCards.setAdapter(homeAdapter);
            }
            homeAdapter.notifyDataSetChanged();
        }));
        return root;

    }

    public ArrayList<Home> getCompletedHome(ArrayList<Home> arr) {
        ArrayList<Home> completedList = new ArrayList<>();
        for (Home home : arr) {
            if (home.isCompleted()) {
                completedList.add(home);
            }
        }
        return completedList;
    }

    private void setUpAddButton(View root) {
        buttonAdd = root.findViewById(R.id.buttAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                todoList.add(new Home(new Classes(), "default", "01/01/2000", false));
                saveData();
                index.set(0, todoList.size() - 1);
                int indexPar = index.get(0);
                HomeFragmentDirections.ActionNavigationHomeToNavigationHomeMenuFragment action = HomeFragmentDirections.actionNavigationHomeToNavigationHomeMenuFragment(indexPar);
                NavHostFragment.findNavController(HomeFragment.this).navigate(action);
            }
        });
    }

    private void updateChanges(HomeAdapter homeAdapter) {
        NavController navController = NavHostFragment.findNavController(this);

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("titleEdit").observe(getViewLifecycleOwner(), new Observer() {


            @Override
            public void onChanged(Object o) {
                System.out.println("Set Task Name: " +o+ index.get(0));
                if (!todoList.isEmpty())
                    todoList.set(index.get(0), todoList.get(index.get(0))).setTitle(o.toString());
                saveData();
            }
        });


        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("dueDateEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Task Name: " +o + index.get(0));
                if (!todoList.isEmpty())
                    todoList.set(index.get(0), todoList.get(index.get(0))).setDueDate(o.toString());
                saveData();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("classEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                System.out.println("Set Task Name: " +o+ index.get(0));
                if (!todoList.isEmpty())
                    todoList.set(index.get(0), todoList.get(index.get(0))).setAssociatedClass(new Classes(o.toString(), "default", "default", "default", "monday", "default", "default", "default"));
                if (binding.sortSpinner.getSelectedItemPosition() == 0) {
                    System.out.println("due date");
                    sortDueDate();
                } else {
                    sortCourseName();
                }
                saveData();
                homeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpSpinner(HomeAdapter homeAdapter) {
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
                if (todoList.isEmpty()) return;
                if (position == 0) {
                    System.out.println("due date");
                    sortDueDate();
                } else {
                    sortCourseName();
                }
                saveData();
                homeAdapter.notifyDataSetChanged();
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
        String json = sharedPreferences.getString("todo", null);
        Type type = new TypeToken<ArrayList<Home>>() {}.getType();
        todoList = gson.fromJson(json, type);
        if (todoList == null) {
            todoList = new ArrayList<>();
        }
    }

    private void saveData() {
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(todoList);
        editor.putString("todo", json);
        editor.apply();
    }

    public void sortDueDate() {
        for (int i = 0; i < todoList.size() - 1; i++) {
            for (int j = 0; j < todoList.size() - 1 - i; j++) {
                if (j + 1 < todoList.size())
                    if (todoList.get(j).compareDate(todoList.get(j + 1)) > 0) {
                        Home temp = todoList.get(j);
                        todoList.set(j, todoList.get(j + 1));
                        todoList.set(j + 1, temp);
                    }
            }
        }
    }

    public void sortCourseName() {
        for (int i = 0; i < todoList.size() - 1; i++) {
            for (int j = 0; j < todoList.size() - 1 - i; j++) {
                if (todoList.get(j).getClassName().compareTo(todoList.get(j + 1).getClassName()) > 0) {
                    Home temp = todoList.get(j);
                    todoList.set(j, todoList.get(j + 1));
                    todoList.set(j + 1, temp);
                }
            }
        }
    }

    public ArrayList<Home> getIncompleteHome(ArrayList<Home> arr) {
        ArrayList<Home> newArr = new ArrayList<>();
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