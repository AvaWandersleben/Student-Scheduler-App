package com.example.cs_2340_student_scheduler_app.ui.home;

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
import com.example.cs_2340_student_scheduler_app.databinding.FragmentHomeBinding;
import com.example.cs_2340_student_scheduler_app.ui.assignments.AssignmentAdapter;
import com.example.cs_2340_student_scheduler_app.ui.home.HomeAdapter;
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
        //        User user = new User();
        //        MainActivity.db.userDao().insertAll(user);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loadDB();
        Home.setContext(getActivity());
        //Home.loadData();

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
                HomeAdapter incompleteAdapter = new HomeAdapter(getContext(), getIncompleteHome(todoList), this, index, false);
                homeCards.setAdapter(incompleteAdapter);
            } else {
                homeCards.setAdapter(homeAdapter);
            }
        }));
        return root;

    }

    private void setUpAddButton(View root) {
        buttonAdd = root.findViewById(R.id.buttAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                index.set(0, todoList.size());
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
                if (!todoList.isEmpty() && index.get(0) < todoList.size())
                    todoList.set(index.get(0), todoList.get(index.get(0))).setTitle(o.toString());
                updateDB();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("dueDateEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                if (!todoList.isEmpty() && index.get(0) < todoList.size())
                    todoList.set(index.get(0), todoList.get(index.get(0))).setDueDate(o.toString());
                updateDB();
            }
        });

        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("classEdit").observe(getViewLifecycleOwner(), new Observer() {

            @Override
            public void onChanged(Object o) {
                if (!todoList.isEmpty() && index.get(0) < todoList.size())
                    todoList.set(index.get(0), todoList.get(index.get(0))).setAssociatedClass(new Classes(o.toString(), "default", "default", "Monday", "monday", "default", "default"));
                if (binding.sortSpinner.getSelectedItemPosition() == 0) {
                    sortDueDate();
                } else {
                    sortCourseName();
                }
                updateDB();
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
                if (todoList.isEmpty() || index.get(0) >= todoList.size()) return;
                if (position == 0) {
                    sortDueDate();
                } else {
                    sortCourseName();
                }
                updateDB();
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public void updateDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(0);
        Gson gson = new Gson();
        user.tasks = gson.toJson(todoList);
        userDao.updateUsers(user);
    }

    public void loadDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(0);
        Gson gson = new Gson();
        String json = user.tasks;
        Type type = new TypeToken<ArrayList<Home>>() {}.getType();
        todoList = gson.fromJson(json, type);
        if (todoList == null) {
            todoList = new ArrayList<>();
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
            if (arr.get(i).isCompleted()) {
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