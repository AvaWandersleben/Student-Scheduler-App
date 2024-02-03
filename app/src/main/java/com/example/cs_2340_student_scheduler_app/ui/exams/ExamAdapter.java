package com.example.cs_2340_student_scheduler_app.ui.exams;

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

import com.example.cs_2340_student_scheduler_app.MainActivity;
import com.example.cs_2340_student_scheduler_app.R;
import com.example.cs_2340_student_scheduler_app.User;
import com.example.cs_2340_student_scheduler_app.UserDao;
import com.example.cs_2340_student_scheduler_app.ui.assignments.AssignmentsFragmentDirections;
import com.example.cs_2340_student_scheduler_app.ui.classes.Classes;
import com.example.cs_2340_student_scheduler_app.ui.exams.Exam;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder>{
    private final Context context;
    private ArrayList<Exam> examList;
    private Fragment from;

    private ArrayList<Integer> index;

    public ExamAdapter(Context context, ArrayList<Exam> examList, Fragment from, ArrayList<Integer> index) {
        this.index = index;
        this.from = from;
        this.context = context;
        this.examList = examList;
    }

    @NonNull
    @Override
    public ExamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_card, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamAdapter.ViewHolder holder, int position) {
        Exam model = examList.get(position);
        holder.title.setText(model.getTitle());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.location.setText(model.getLocation());
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView date;
        private final TextView time;
        private final TextView location;

        private FloatingActionButton deleteButt;
        private Button editButt;
        private ExamAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
            deleteButt = itemView.findViewById(R.id.deleteBut);
            editButt = itemView.findViewById(R.id.editButt);

            final boolean[] confirmed = {false};
            deleteButt.setOnClickListener(view -> {
                if (!confirmed[0]) {
                    deleteButt.setImageResource(R.drawable.ic_home_black_24dp);
                    confirmed[0] = true;
                } else {
                    adapter.examList.remove(getAdapterPosition());
                    adapter.updateDB();
                    adapter.notifyItemRemoved(getAdapterPosition());
                }
            });

            editButt.setOnClickListener(view -> {
                adapter.index.set(0, getAdapterPosition());
                int indexPar = adapter.index.get(0);
                ExamsFragmentDirections.ActionNavigationExamsToNavigationExamsMenuFragment action = ExamsFragmentDirections.actionNavigationExamsToNavigationExamsMenuFragment(indexPar);
                NavHostFragment.findNavController(adapter.from).navigate(action);
            });
        }

        public ViewHolder linkAdapter(ExamAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }

    public void updateDB() {
        UserDao userDao = MainActivity.db.userDao();
        User user = userDao.getUser(MainActivity.currUser);
        Gson gson = new Gson();
        user.exams = gson.toJson(examList);
        userDao.updateUsers(user);
    }
}
