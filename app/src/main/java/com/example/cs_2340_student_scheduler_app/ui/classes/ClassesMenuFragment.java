package com.example.cs_2340_student_scheduler_app.ui.classes;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
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
import com.example.cs_2340_student_scheduler_app.databinding.FragmentClassesMenuBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class ClassesMenuFragment extends Fragment {

    private FragmentClassesMenuBinding binding;
    private EditText courseName;
    private EditText instructName;
    private EditText daysText;
    private EditText sectionText;
    private EditText locationText;
    private EditText roomText;

    private ArrayList<Classes> classList;
    private int index;
    private DialogInterface.OnClickListener dialogClickListener;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClassesMenuBinding.inflate(inflater, container, false);

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
        index = ClassesMenuFragmentArgs.fromBundle(getArguments()).getIndex();
        loadDB();
        courseName = binding.editTextClassName;
        instructName = binding.editTextInstructorName;
        daysText = binding.editTextDays;
        sectionText = binding.editTextSection;
        locationText = binding.editTextLocation;
        roomText = binding.editTextRoom;

        if (index < classList.size()) {
            binding.textView2.setText("CLASS TIME: " + classList.get(index).getTime());
            instructName.setText(classList.get(index).getInstructor());
            courseName.setText(classList.get(index).getCourseName());
            daysText.setText(classList.get(index).getDays());
            sectionText.setText(classList.get(index).getSection());
            locationText.setText(classList.get(index).getLocation());
            roomText.setText(classList.get(index).getRoomNumber());
        }


        binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                binding.textView2.setText("CLASS TIME: " + String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));

                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });
        binding.submitButt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    private void done() {
        String courseNameStr = courseName.getText().toString();
        String instructNameStr = instructName.getText().toString();
        String timeTextStr = binding.textView2.getText().toString().replace("CLASS TIME: ", "");
        String daysTextStr = daysText.getText().toString();
        String sectionTextStr = sectionText.getText().toString();
        String locationTextStr = locationText.getText().toString();
        String roomTextStr = roomText.getText().toString();
        boolean goodData = true;
        if (!ManipulateData.validateDayOfWeek(daysTextStr) ||
                !ManipulateData.validateTime(timeTextStr) || locationTextStr.trim().isEmpty()
                || courseNameStr.trim().isEmpty()
                || instructNameStr.trim().isEmpty()
                || sectionTextStr.trim().isEmpty()
                || roomTextStr.trim().isEmpty()) {
            goodData = false;
        }
        if (goodData) {
            if(index >= classList.size())
                classList.add(new Classes());
            updateDB();
            classList.get(index).setLocation(locationTextStr);
            classList.get(index).setDays(daysTextStr);
            classList.get(index).setTime(timeTextStr);
            classList.get(index).setRoomNumber(roomTextStr);
            classList.get(index).setInstructor(instructNameStr);
            classList.get(index).setCourseName(courseNameStr);
            classList.get(index).setSection(sectionTextStr);
            updateDB();
            NavController navController = NavHostFragment.findNavController(ClassesMenuFragment.this);
            navController.getPreviousBackStackEntry().getSavedStateHandle().set("locationEdit", locationTextStr);
            navController.popBackStack();
        } else {
            String message = "";
            if (courseNameStr.trim().isEmpty()) {
                message = "Course must have a name.";
            } else if (!ManipulateData.validateDayOfWeek(daysTextStr)) {
                message += "Days of Week must be comma separated with no spaces";
            } else if (!ManipulateData.validateTime(timeTextStr)) {
                message += "Invalid time input";
            } else if (locationTextStr.trim().isEmpty()){
                message = "Location cannot be blank";
            } else if (instructNameStr.trim().isEmpty()) {
                message = "Instructor cannot be blank";
            } else if (sectionTextStr.trim().isEmpty()) {
                message = "Section cannot be blank";
            } else {
                message = "Room cannot be blank";
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
        classList = gson.fromJson(json, type);
        if (classList == null) {
            classList = new ArrayList<>();
        }
    }

    public void updateDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(MainActivity.currUser);
        Gson gson = new Gson();
        user.classes = gson.toJson(classList);
        userDao.updateUsers(user);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}