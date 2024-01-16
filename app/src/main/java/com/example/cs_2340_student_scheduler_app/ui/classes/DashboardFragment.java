package com.example.cs_2340_student_scheduler_app.ui.classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.LinearLayoutCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs_2340_student_scheduler_app.R;
import com.example.cs_2340_student_scheduler_app.databinding.FragmentDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardFragment extends Fragment {
    private LinearLayoutCompat cards;
    private FloatingActionButton buttonAdd;
    private ArrayList<Classes> classList = new ArrayList<>();


    private FragmentDashboardBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        buttonAdd = root.findViewById(R.id.butAd);
        RecyclerView courseCards =root.findViewById(R.id.idClass);

        // Here, we have created new array list and added data to it
        classList.add(new Classes("Math", 230, "Mr Math"));
        classList.add(new Classes("Science", 740, "Mr Science"));

        ClassAdapter classAdapter = new ClassAdapter(getContext(), classList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        courseCards.setLayoutManager(linearLayoutManager);
        courseCards.setAdapter(classAdapter);
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                CardView newCard = new CardView(getContext());
//                getLayoutInflater().inflate(R.layout.class_card, newCard);
//
//                TextView className = newCard.findViewById(R.id.className);
//                TextView time = newCard.findViewById(R.id.timeText);
//                TextView instructor = newCard.findViewById(R.id.instructName);
//
//
//                className.setText("Class Name");
//                time.setText("Time");
//                instructor.setText("Instruct");
////                newCard.setTag(); //
//
//                cards.addView(newCard);
                classList.add(new Classes("Science", 740, "Mr Science"));
                courseCards.setAdapter(classAdapter);
            }
        });
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

    }

    private void makeSelections() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}