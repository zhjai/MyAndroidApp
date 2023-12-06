package com.example.exam.data;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<TaskItem> taskList;
    private TaskDataBank dataBank;

    static ArrayList<TaskItem> finishedTasks = new ArrayList<>();

    public TaskAdapter(ArrayList<TaskItem> taskList, TaskDataBank dataBank) {
        this.taskList = taskList;
        this.dataBank = dataBank;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskItem taskItem = taskList.get(position);
        holder.textTask.setText(taskItem.getName());
        holder.textScore.setText("+" + taskItem.getPoints().toString());

        holder.checkbox.setOnCheckedChangeListener(null);

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                TaskItem taskItem1 = taskList.get(currentPosition);
                if (isChecked) {
                    finishedTasks.add(taskItem1);
                    taskList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    dataBank.saveObject(taskList);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView textTask;
        TextView textScore;
        CheckBox checkbox;

        ViewHolder(View view, final TaskAdapter adapter) {
            super(view);
            textTask = view.findViewById(R.id.text_task);
            textScore = view.findViewById(R.id.text_score);
            checkbox = view.findViewById(R.id.checkbox_task);

            view.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, 0, this.getAdapterPosition(), "添加任务");
            contextMenu.add(0, 2, this.getAdapterPosition(), "修改任务");
            contextMenu.add(0, 1, this.getAdapterPosition(), "删除任务");
        }
    }
}