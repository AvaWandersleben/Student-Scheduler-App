package com.example.cs_2340_student_scheduler_app.ui.classes;

import android.app.Notification;
import android.os.Bundle;
import android.service.credentials.Action;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs_2340_student_scheduler_app.R;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentHomeBinding;
import com.example.cs_2340_student_scheduler_app.ui.home.HomeViewModel;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cs_2340_student_scheduler_app.databinding.NewClassMenuBinding;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class EditFragment extends Fragment {

    private NewClassMenuBinding binding;

    private Button submitButton;
    private EditText courseName;
    private EditText instructName;
    private EditText timeText;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = NewClassMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courseName = view.findViewById(R.id.editTextClassName);
        instructName = view.findViewById(R.id.editTextInstructorName);
        timeText = view.findViewById(R.id.editTextTime);


        binding.submitButt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String courseNameStr = courseName.getText().toString();
                String instructNameStr = instructName.getText().toString();
                String timeTextStr = timeText.getText().toString();
//                NavDirections action =
//                        MenuFragmentDirections.actionNavigationAddClassToNavigationDashboard(courseNameStr, instructNameStr, timeTextStr);
                NavController navController = NavHostFragment.findNavController(EditFragment.this);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("courseEdit", courseNameStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("timeEdit", timeTextStr);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("instructorEdit", instructNameStr);
                navController.popBackStack();
//                NavHostFragment.findNavController(MenuFragment.this)
//                        .navigate(action);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}