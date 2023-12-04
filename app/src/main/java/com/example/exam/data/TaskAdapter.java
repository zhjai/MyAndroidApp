package com.example.exam.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<TaskItem> taskList;

    public TaskAdapter(ArrayList<TaskItem> taskList) {
        this.taskList = taskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskItem taskItem = taskList.get(position);
        holder.textTask.setText(taskItem.getName());
        holder.textScore.setText("+" + taskItem.getPoints().toString());
        // Set listeners or other properties here
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTask;
        TextView textScore;

        ViewHolder(View view) {
            super(view);
            textTask = view.findViewById(R.id.text_task);
            textScore = view.findViewById(R.id.text_score);
        }
    }
}