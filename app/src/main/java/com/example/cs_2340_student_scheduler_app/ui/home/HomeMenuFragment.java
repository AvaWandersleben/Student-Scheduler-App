package com.example.cs_2340_student_scheduler_app.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cs_2340_student_scheduler_app.MainActivity;
import com.example.cs_2340_student_scheduler_app.ManipulateData;
import com.example.cs_2340_student_scheduler_app.User;
import com.example.cs_2340_student_scheduler_app.UserDao;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentHomeMenuBinding;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class HomeMenuFragment extends Fragment{

    private FragmentHomeMenuBinding binding;
    private EditText title;
    private EditText dueDate;

    private ArrayList<Classes> classList;
    private ArrayList<Home> todo;
    private DialogInterface.OnClickListener dialogClickListener;

    private int index;

    private Spinner spinner;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeMenuBinding.inflate(inflater, container, false);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                done();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();

                        }
                    }
                };
                // on below line we are creating a builder variable for our alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // on below line we are setting message for our dialog box.
                builder.setMessage("Are you sure you are done editing?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDB();
        index = HomeMenuFragmentArgs.fromBundle(getArguments()).getIndex();
        title = binding.editTitle;
        dueDate = binding.editDueDate;
        if (index < todo.size()) {
            title.setText(todo.get(index).getTitle());
            dueDate.setText(todo.get(index).getDueDate());
        }

        spinner = binding.classSpinner;
        ArrayList<String> classNames = new ArrayList<>();
        for (Classes c : classList) {
            classNames.add(c.getCourseName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (index < todo.size())
            spinner.setSelection(todo.get(index).getClassNameLoc());





        binding.submitButtEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    public void done() {
        String titleStr = title.getText().toString();
        String dueDateStr = dueDate.getText().toString();
        String associatedCourseStr = "";
        boolean goodData = false;
        if (ManipulateData.validateDate(dueDateStr) && spinner.getSelectedItem() != null && !titleStr.trim().isEmpty()) {
            goodData = true;
            associatedCourseStr = spinner.getSelectedItem().toString();
        }

        if (goodData) {
            if(index >= todo.size())
                todo.add(new Home(new Classes(), "default", "01/01/2000", false));
            updateDB();
            todo.get(index).setAssociatedClass(new Classes(associatedCourseStr));
            todo.get(index).setTitle(titleStr);
            todo.get(index).setDueDate(dueDateStr);
            updateDB();
            NavController navController = NavHostFragment.findNavController(HomeMenuFragment.this);
            navController.getPreviousBackStackEntry().getSavedStateHandle().set("titleEdit", titleStr);
            navController.popBackStack();
        } else {
            String message;
            if (spinner.getSelectedItem() == null) {
                message = "Must Add A Class First";
            } else if (titleStr.trim().isEmpty()) {
                message = "Title cannot be blank";
            } else {
                message = "Date must be mm/dd/yyyy format.";
            }
            Toast alert = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
            alert.show();
        }
    }


    public void loadDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(MainActivity.currUser);
        Gson gson = new Gson();
        String json = user.classes;
        Type type = new TypeToken<ArrayList<Classes>>() {}.getType();
        Type type2 = new TypeToken<ArrayList<Home>>() {}.getType();
        String json2 = user.tasks;
        todo = gson.fromJson(json2, type2);
        classList = gson.fromJson(json, type);
        if (classList == null) {
            classList = new ArrayList<>();
        }
        if (todo == null) {
            todo = new ArrayList<>();
        }
    }

    public void updateDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(MainActivity.currUser);
        Gson gson = new Gson();
        user.tasks = gson.toJson(todo);
        userDao.updateUsers(user);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}