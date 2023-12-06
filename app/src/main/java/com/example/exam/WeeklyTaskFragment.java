package com.example.exam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.exam.data.TaskAdapter;
import com.example.exam.data.TaskDataBank;
import com.example.exam.data.TaskItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeeklyTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyTaskFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private TaskDataBank dataBank;
    private ArrayList<TaskItem> taskList = new ArrayList<>();
    private ActivityResultLauncher<Intent> addTaskLauncher;
    private ActivityResultLauncher<Intent> modifyTaskLauncher;
    private boolean isCurrentFragment = false;

    public WeeklyTaskFragment() {
        // Required empty public constructor
    }

    public static WeeklyTaskFragment newInstance() {
        WeeklyTaskFragment fragment = new WeeklyTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            addTaskLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            // 这里是您处理返回结果的地方
                            Intent data = result.getData();
                            String taskName = data.getStringExtra("TASK_NAME");
                            int taskPoints = data.getIntExtra("TASK_POINTS", -1);
                            TaskItem newTask = new TaskItem(taskName, taskPoints);
                            taskList.add(newTask);
                            taskAdapter.notifyItemInserted(taskList.size() - 1);
                            dataBank.saveObject(taskList);
                        }
                    }
            );
            modifyTaskLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            // 这里是您处理返回结果的地方
                            Intent data = result.getData();
                            String taskName = data.getStringExtra("TASK_NAME");
                            int taskPoints = data.getIntExtra("TASK_POINTS", -1);
                            int position = data.getIntExtra("TASK_POSITION", -1);
                            taskList.get(position).setName(taskName);
                            taskList.get(position).setPoints(taskPoints);
                            taskAdapter.notifyItemChanged(position);
                            dataBank.saveObject(taskList);
                        }
                    }
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weekly_task, container, false);
        recyclerView = rootView.findViewById(R.id.weekly_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataBank = new TaskDataBank(getContext(), "weeklyTasks");
        taskList = dataBank.loadObject();
        taskAdapter = new TaskAdapter(taskList, dataBank);
        recyclerView.setAdapter(taskAdapter);

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.fab_add_weekly_task);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            addTaskLauncher.launch(intent);
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isCurrentFragment = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isCurrentFragment = false;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (!isCurrentFragment) {
            return false;
        }
        int position = item.getOrder();
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                addTaskLauncher.launch(intent);
                return true;
            case 1:
                taskList.remove(position);
                taskAdapter.notifyItemRemoved(position);
                dataBank.saveObject(taskList);
                return true;
            case 2:
                intent = new Intent(getActivity(), ModifyTaskActivity.class);
                intent.putExtra("taskName", taskList.get(position).getName());
                intent.putExtra("taskPoints", taskList.get(position).getPoints());
                intent.putExtra("taskPosition", position);
                modifyTaskLauncher.launch(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}