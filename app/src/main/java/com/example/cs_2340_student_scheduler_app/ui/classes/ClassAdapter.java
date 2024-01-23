package com.example.cs_2340_student_scheduler_app.ui.classes;

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
import com.example.cs_2340_student_scheduler_app.ui.assignments.AssignmentsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.function.Predicate;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder>{
    private final Context context;
    private ArrayList<Classes> classList;
    private Fragment from;

    private ArrayList<Integer> index;
    private ArrayList<Assignment> assignmentList;

    public ClassAdapter(Context context, ArrayList<Classes> classList, Fragment from, ArrayList<Integer> index) {
        this.index = index;
        this.from = from;
        this.context = context;
        this.classList = classList;
    }

    @NonNull
    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_card, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.ViewHolder holder, int position) {
        Classes model = classList.get(position);
        holder.className.setText(model.getCourseName());
        holder.timeText.setText("Next Class: " + model.getDayOfWeek() + " at " + model.getTime());
        holder.instruct.setText("Instructor: " + model.getInstructor());
        holder.room.setText("Room: " + model.getRoomNumber());
        holder.location.setText("Location: " + model.getLocation());
        holder.section.setText("Section: " + model.getSection());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView className;
        private final TextView timeText;
        private final TextView instruct;
        private final TextView section;
        private final TextView location;
        private final TextView room;

        private FloatingActionButton deleteButt;
        private Button editButt;
        private ClassAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
            timeText = itemView.findViewById(R.id.nextClass);
            instruct = itemView.findViewById(R.id.professor);
            section = itemView.findViewById(R.id.section);
            location = itemView.findViewById(R.id.location);
            room = itemView.findViewById(R.id.room);
            deleteButt = itemView.findViewById(R.id.deleteBut);
            editButt = itemView.findViewById(R.id.editButt);

            deleteButt.setOnClickListener(view -> {
                adapter.loadData();
                filterAssignments(adapter.assignmentList);
                adapter.classList.remove(getAdapterPosition());
                adapter.saveData();
                adapter.notifyItemRemoved(getAdapterPosition());
            });

            editButt.setOnClickListener(view -> {
                adapter.index.set(0, getAdapterPosition());
                ClassesFragmentDirections.ActionNavigationDashboardToNavigationAddClass action = ClassesFragmentDirections.actionNavigationDashboardToNavigationAddClass(adapter.index.get(0));
                NavHostFragment.findNavController(adapter.from).navigate(action);
            });
        }

        public ViewHolder linkAdapter(ClassAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public void filterAssignments(ArrayList<Assignment> assignments){
            assignments.removeIf(new Predicate<Assignment>() {
                @Override
                public boolean test(Assignment assignment) {
                    Classes course = assignment.getAssociatedClass();
                    System.out.println(course.getCourseName());
                    System.out.println(adapter.classList.contains(course));
                    return !adapter.classList.contains(course);
                }
            });
            System.out.println("*");
            for (Assignment a : assignments) {
                System.out.println(a.getClassName());
            }
            AssignmentsFragment.assignmentAdapter.notifyDataSetChanged();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(classList);
        String json2 = gson.toJson(assignmentList);
        editor.putString("courses", json);
        editor.putString("assignments", json2);
        editor.apply();
    }

    private void loadData() {
        Context context = from.getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("assignments", null);
        Type type = new TypeToken<ArrayList<Assignment>>() {}.getType();
        assignmentList = gson.fromJson(json, type);
        if (assignmentList == null) {
            assignmentList = new ArrayList<>();
        }
    }
}
