package com.example.exam;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;

import com.example.exam.data.TaskAdapter;
import com.example.exam.data.TaskDataBank;
import com.example.exam.data.TaskItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyTaskFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyTaskTextView;
    private TaskAdapter taskAdapter;
    private TaskDataBank dataBank;
    private ArrayList<TaskItem> taskList = new ArrayList<>();
    private ActivityResultLauncher<Intent> addTaskLauncher;
    private ActivityResultLauncher<Intent> modifyTaskLauncher;
    private boolean isCurrentFragment = false;

    public DailyTaskFragment() {
        // Required empty public constructor
    }

    public static DailyTaskFragment newInstance() {
        DailyTaskFragment fragment = new DailyTaskFragment();
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
                        for (TaskItem taskItem : taskList) {
                            if (taskItem.getName().equals(taskName)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                                builder.setTitle("添加失败");
                                builder.setMessage("任务已存在");
                                builder.setPositiveButton("确定", (dialog, which) -> {});
                                builder.show();
                                return;
                            }
                        }
                        TaskItem newTask = new TaskItem(taskName, taskPoints);
                        taskList.add(newTask);
                        taskAdapter.notifyItemInserted(taskList.size() - 1);
                        dataBank.saveObject(taskList);
                        checkIfEmpty();
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
        View rootView = inflater.inflate(R.layout.fragment_daily_task, container, false);
        recyclerView = rootView.findViewById(R.id.daily_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataBank = new TaskDataBank(getContext(), "dailyTasks");
        taskList = dataBank.loadObject();
        taskAdapter = new TaskAdapter(taskList, dataBank);
        recyclerView.setAdapter(taskAdapter);
        emptyTaskTextView = rootView.findViewById(R.id.empty_daily_task_text_view);
        checkIfEmpty();

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.fab_add_daily_task);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("删除任务");
                builder.setMessage("你真的要删除任务吗？");
                builder.setPositiveButton("是", (dialog, which) -> {
                    taskList.remove(position);
                    taskAdapter.notifyItemRemoved(position);
                    dataBank.saveObject(taskList);
                });
                builder.setNegativeButton("否", (dialog, which) -> {});
                builder.show();
                checkIfEmpty();
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


    public void checkIfEmpty() {
        if (taskList.isEmpty()) {
            emptyTaskTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTaskTextView.setVisibility(View.GONE);
        }
    }
}