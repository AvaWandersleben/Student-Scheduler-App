package com.example.cs_2340_student_scheduler_app.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs_2340_student_scheduler_app.MainActivity;
import com.example.cs_2340_student_scheduler_app.User;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentUserBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {


    private FragmentUserBinding binding;
    private ArrayList<Integer> users = new ArrayList<>();

    private FloatingActionButton addButt;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel classesViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //addButt = binding.addButt;
        setUpSpinner();
        return root;

    }

    private void setUpSpinner() {
        List<User> allUsers  = MainActivity.db.userDao().getAll();
        for (User u : allUsers) {
            users.add(u.uid + 1);
        }
        Spinner spinner = binding.userSpinner;
        ArrayAdapter<Integer> userAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, users);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(userAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.currUser = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}