package com.example.cs_2340_student_scheduler_app.ui.home;

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
import com.example.cs_2340_student_scheduler_app.ui.assignments.AssignmentsFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    private final Context context;
    private ArrayList<Home> todoList;
    private Fragment from;
    private boolean deletingComplete;

    private ArrayList<Integer> index;

    public HomeAdapter(Context context, ArrayList<Home> todoList, Fragment from, ArrayList<Integer> index, boolean deletingComplete) {
        this.index = index;
        this.from = from;
        this.context = context;
        this.todoList = todoList;
        this.deletingComplete = deletingComplete;
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_card, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        Home model = todoList.get(position);
        if (!model.isCompleted() || !deletingComplete) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.title.setText(model.getTitle());
            holder.dueDate.setText(model.getDueDate());
            holder.associatedClass.setText(model.getClassName());
            holder.completedSwitch.setChecked(model.isCompleted());
        } else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

    }

    @Override
    public int getItemCount() {
        if (deletingComplete) {
            int count = 0;
            for (Home home : todoList) {
                if (!home.isCompleted()) {
                    count++;
                }
            }
            return count;
        } else {
            return todoList.size();
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView dueDate;
        private final TextView associatedClass;
        private final SwitchMaterial completedSwitch;

        private FloatingActionButton deleteButt;
        private Button editButt;
        private HomeAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.assignTitle);
            dueDate = itemView.findViewById(R.id.dueDate);
            associatedClass = itemView.findViewById(R.id.associatedClass);
            deleteButt = itemView.findViewById(R.id.deleteBut);
            editButt = itemView.findViewById(R.id.editButt);
            completedSwitch = itemView.findViewById(R.id.completedSwitch);
            final boolean[] confirmed = {false};

            deleteButt.setOnClickListener(view -> {
                if (!confirmed[0]) {
                    confirmed[0] = true;
                    deleteButt.setImageResource(R.drawable.ic_home_black_24dp);
                } else {
                    adapter.todoList.remove(getAdapterPosition());
                    adapter.saveData();
                    adapter.notifyItemRemoved(getAdapterPosition());
                }
            });

            editButt.setOnClickListener(view -> {
                adapter.index.set(0, getAdapterPosition());
                int indexPar = adapter.index.get(0);
                HomeFragmentDirections.ActionNavigationHomeToNavigationHomeMenuFragment action = HomeFragmentDirections.actionNavigationHomeToNavigationHomeMenuFragment(indexPar);
                NavHostFragment.findNavController(adapter.from).navigate(action);
            });

            completedSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                adapter.todoList.get(getAdapterPosition()).setCompleted(isChecked);
                if (adapter.deletingComplete) {
                    adapter.todoList.remove(getAdapterPosition());
                    adapter.notifyItemRemoved(getAdapterPosition());
                }
                adapter.saveData();
            });
        }

        public HomeAdapter.ViewHolder linkAdapter(HomeAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("assignments", null);
        Type type = new TypeToken<ArrayList<Home>>() {}.getType();
        todoList = gson.fromJson(json, type);
        if (todoList == null) {
            todoList = new ArrayList<>();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(todoList);
        editor.putString("assignments", json);
        editor.apply();
    }
}
