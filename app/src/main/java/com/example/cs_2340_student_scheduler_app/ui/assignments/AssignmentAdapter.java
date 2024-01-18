package com.example.cs_2340_student_scheduler_app.ui.assignments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Notification;
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
                int indexPar = adapter.index.get(0);
                AssignmentsFragmentDirections.ActionNavigationNotificationsToNavigationAssignmentMenuFragment action = AssignmentsFragmentDirections.actionNavigationNotificationsToNavigationAssignmentMenuFragment(indexPar);
                NavHostFragment.findNavController(adapter.from).navigate(action);
            });
        }

        public ViewHolder linkAdapter(AssignmentAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("assignments", null);
        Type type = new TypeToken<ArrayList<Assignment>>() {}.getType();
        assignmentList = gson.fromJson(json, type);
        if (assignmentList == null) {
            assignmentList = new ArrayList<>();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(assignmentList);
        editor.putString("assignments", json);
        editor.apply();
    }
}
