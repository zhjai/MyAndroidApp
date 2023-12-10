package com.example.exam.data;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<TaskItem> taskList;
    private ArrayList<TaskItem> filteredTaskList;
    private TaskDataBank dataBank;
    private TaskDataBank finishedDataBank = new TaskDataBank(GlobalData.context, "finishedTasks");
    private Boolean contextMenuEnabled = true;
    private Boolean isSortMode = false;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");


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
        if (taskItem.getDate() == null) {
            holder.textDate.setText("无日期");
        }
        else {
            holder.textDate.setText(simpleDateFormat.format(taskItem.getDate()));
        }

        switch (taskItem.getImportance()) {
            case 0:
                holder.chipImportance.setText("一般");
                holder.chipImportance.setChipBackgroundColorResource(R.color.gray);
                break;
            case 1:
                holder.chipImportance.setText("重要");
                holder.chipImportance.setChipBackgroundColorResource(R.color.orange);
                break;
            case 2:
                holder.chipImportance.setText("紧急");
                holder.chipImportance.setChipBackgroundColorResource(R.color.red);
                break;
            default:
                holder.chipImportance.setText("一般");
                holder.chipImportance.setChipBackgroundColorResource(R.color.gray);
                break;
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
                    Snackbar.make(holder.itemView, "完成一个任务", Snackbar.LENGTH_SHORT).show();
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
        TextView textDate;
        Chip chipImportance;
        CheckBox checkbox;

        ViewHolder(View view, final TaskAdapter adapter) {
            super(view);
            this.adapter = adapter;
            textTask = view.findViewById(R.id.task_name);
            textGroup = view.findViewById(R.id.task_group);
            textScore = view.findViewById(R.id.task_score);
            textDate = view.findViewById(R.id.task_date);
            chipImportance = view.findViewById(R.id.task_importance);
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