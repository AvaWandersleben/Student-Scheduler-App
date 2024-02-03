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

import com.example.cs_2340_student_scheduler_app.MainActivity;
import com.example.cs_2340_student_scheduler_app.R;
import com.example.cs_2340_student_scheduler_app.User;
import com.example.cs_2340_student_scheduler_app.UserDao;
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
    //private ArrayList<Assignment> assignmentList;

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
            final boolean[] confirmed = {false};
            className = itemView.findViewById(R.id.className);
            timeText = itemView.findViewById(R.id.nextClass);
            instruct = itemView.findViewById(R.id.professor);
            section = itemView.findViewById(R.id.section);
            location = itemView.findViewById(R.id.location);
            room = itemView.findViewById(R.id.room);
            deleteButt = itemView.findViewById(R.id.deleteBut);
            editButt = itemView.findViewById(R.id.editButt);

            deleteButt.setOnClickListener(view -> {
                if (!confirmed[0]) {
                    deleteButt.setImageResource(R.drawable.ic_stat_name);
                } else {
                    adapter.loadDB();
//                    filterAssignments(adapter.assignmentList);
                    adapter.classList.remove(getAdapterPosition());
                    adapter.updateDB();
                    adapter.notifyItemRemoved(getAdapterPosition());
                }
                confirmed[0] = !confirmed[0];
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
            //AssignmentsFragment.assignmentAdapter.notifyDataSetChanged();
        }
    }

    public void updateDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(0);
        Gson gson = new Gson();
        user.classes = gson.toJson(classList);
        userDao.updateUsers(user);
    }

    public void loadDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(0);
        Gson gson = new Gson();
        String json = user.classes;
        Type type = new TypeToken<ArrayList<Classes>>() {}.getType();
        classList = gson.fromJson(json, type);
        if (classList == null) {
            classList = new ArrayList<>();
        }
    }
}
