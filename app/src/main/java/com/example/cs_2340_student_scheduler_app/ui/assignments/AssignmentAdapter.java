package com.example.cs_2340_student_scheduler_app.ui.assignments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_2340_student_scheduler_app.R;
import com.example.cs_2340_student_scheduler_app.ui.assignments.Assignment;
import com.example.cs_2340_student_scheduler_app.ui.assignments.AssignmentAdapter;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder>{
    private final Context context;
    private ArrayList<Assignment> assignmentList;
    private Fragment from;

    private ArrayList<Integer> index;

    public AssignmentAdapter(Context context, ArrayList<Assignment> assignmentList, Fragment from, ArrayList<Integer> index) {
        this.index = index;
        this.from = from;
        this.context = context;
        this.assignmentList = assignmentList;
    }

    @NonNull
    @Override
    public AssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_card, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentAdapter.ViewHolder holder, int position) {
        Assignment model = assignmentList.get(position);
        holder.title.setText(model.getTitle());
        holder.dueDate.setText(model.getDueDate());
        holder.associatedClass.setText(model.getClassName());
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView dueDate;
        private final TextView associatedClass;

        private FloatingActionButton deleteButt;
        private Button editButt;
        private AssignmentAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.assignTitle);
            dueDate = itemView.findViewById(R.id.dueDate);
            associatedClass = itemView.findViewById(R.id.associatedClass);
            deleteButt = itemView.findViewById(R.id.deleteBut);
            editButt = itemView.findViewById(R.id.editButt);

            deleteButt.setOnClickListener(view -> {
                adapter.assignmentList.remove(getAdapterPosition());
                adapter.saveData();
                adapter.notifyItemRemoved(getAdapterPosition());
            });

            editButt.setOnClickListener(view -> {
                adapter.index.set(0, getAdapterPosition());
                NavHostFragment.findNavController(adapter.from).navigate(R.id.action_navigation_notifications_to_navigation_assignment_menu_fragment);
            });
        }

        public ViewHolder linkAdapter(AssignmentAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }

    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("assignments", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<Assignment>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        assignmentList = gson.fromJson(json, type);
        System.out.println(assignmentList.size());

        // checking below if the array list is empty or not
        if (assignmentList == null) {
            // if the array list is empty
            // creating a new array list.
            assignmentList = new ArrayList<>();
        }
    }

    private void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(assignmentList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("assignments", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();
        System.out.println(assignmentList.size());
    }
}
