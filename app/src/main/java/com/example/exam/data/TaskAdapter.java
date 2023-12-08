package com.example.exam.data;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;

import java.util.ArrayList;
import java.util.Collections;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<TaskItem> taskList;
    private ArrayList<TaskItem> filteredTaskList;
    private TaskDataBank dataBank;
    private TaskDataBank finishedDataBank = new TaskDataBank(GlobalData.context, "finishedTasks");
    private Boolean contextMenuEnabled = true;
    private Boolean isSortMode = false;

    public TaskAdapter(ArrayList<TaskItem> taskList, ArrayList<TaskItem> filteredTaskList, TaskDataBank dataBank) {
        this.taskList = taskList;
        this.filteredTaskList = filteredTaskList;
        this.dataBank = dataBank;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskItem taskItem = filteredTaskList.get(position);
        holder.textTask.setText(taskItem.getName());
        holder.textScore.setText("+" + taskItem.getPoints().toString());
        if (taskItem.getGroup() == null) {
            holder.textGroup.setText("未分组");
        }
        else {
            holder.textGroup.setText(taskItem.getGroup());
        }
        holder.itemView.setOnCreateContextMenuListener(isSortMode || !contextMenuEnabled ? null : holder);

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(false);
        holder.checkbox.setEnabled(!isSortMode);

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                TaskItem taskItem1 = filteredTaskList.get(currentPosition);
                if (isChecked) {
                    taskItem1.setDateTimeMillis(System.currentTimeMillis());
                    GlobalData.finishedTasks.add(taskItem1);
                    finishedDataBank.saveObject(GlobalData.finishedTasks);
                    GlobalData.setPoints(GlobalData.getPoints().getValue() + taskItem1.getPoints());
                    deleteTask(taskItem1.getName());
                    filteredTaskList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    dataBank.saveObject(taskList);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredTaskList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TaskAdapter adapter;
        TextView textTask;
        TextView textGroup;
        TextView textScore;
        CheckBox checkbox;

        ViewHolder(View view, final TaskAdapter adapter) {
            super(view);
            this.adapter = adapter;
            textTask = view.findViewById(R.id.task_name);
            textGroup = view.findViewById(R.id.task_group);
            textScore = view.findViewById(R.id.task_score);
            checkbox = view.findViewById(R.id.checkbox_task);

            view.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if (!adapter.isSortMode) {
                contextMenu.add(0, 0, this.getAdapterPosition(), "添加任务");
                contextMenu.add(0, 2, this.getAdapterPosition(), "修改任务");
                contextMenu.add(0, 1, this.getAdapterPosition(), "删除任务");
            }
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(filteredTaskList, i, i + 1);
            }
        }
        else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(filteredTaskList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        moveTask(filteredTaskList.get(fromPosition).getName(), filteredTaskList.get(toPosition).getName());
        dataBank.saveObject(taskList);
    }

    public void setContextMenuEnabled(boolean enabled) {
        contextMenuEnabled = enabled;
        notifyDataSetChanged(); // 刷新适配器以应用更改
    }

    public void setSortMode(boolean isSortMode) {
        this.isSortMode = isSortMode;
        notifyDataSetChanged();
    }

    public void deleteTask(String taskName) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getName().equals(taskName)) {
                taskList.remove(i);
                break;
            }
        }
    }

    public void moveTask(String fromTaskName, String toTaskName) {
        int fromPosition = -1;
        int toPosition = -1;
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getName().equals(fromTaskName)) {
                fromPosition = i;
            }
            if (taskList.get(i).getName().equals(toTaskName)) {
                toPosition = i;
            }
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(taskList, i, i + 1);
            }
        }
        else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(taskList, i, i - 1);
            }
        }
    }
}