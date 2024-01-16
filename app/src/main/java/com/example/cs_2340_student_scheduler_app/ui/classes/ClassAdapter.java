package com.example.cs_2340_student_scheduler_app.ui.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_2340_student_scheduler_app.R;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder>{
    private final Context context;
    private final ArrayList<Classes> courseModelArrayList;

    // Constructor
    public ClassAdapter(Context context, ArrayList<Classes> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.ViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        Classes model = courseModelArrayList.get(position);
        holder.className.setText(model.getCourseName());
        holder.timeText.setText(model.getTimeString());
        holder.instruct.setText(model.getInstructor());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return courseModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView className;
        private final TextView timeText;
        private final TextView instruct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
            timeText = itemView.findViewById(R.id.timeText);
            instruct = itemView.findViewById(R.id.instructName);
        }
    }
}
