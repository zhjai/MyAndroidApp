package com.example.exam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.exam.data.DragAndDropTaskCallback;
import com.example.exam.data.GlobalData;
import com.example.exam.data.SortModeListener;
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
public class WeeklyTaskFragment extends Fragment implements SortModeListener {

    private RecyclerView recyclerView;
    private TextView emptyTaskTextView;
    private TaskAdapter taskAdapter;
    private TaskDataBank dataBank;
    private ArrayList<TaskItem> taskList = new ArrayList<>();
    private ArrayList<TaskItem> filteredTaskList = new ArrayList<>();
    private ActivityResultLauncher<Intent> addTaskLauncher;
    private ActivityResultLauncher<Intent> modifyTaskLauncher;
    private boolean isCurrentFragment = false;
    private ItemTouchHelper itemTouchHelper;

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
                            String taskGroup = data.getStringExtra("TASK_GROUP");
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
                            filteredTaskList.add(new TaskItem(taskName, taskPoints, taskGroup));
                            taskList.add(new TaskItem(taskName, taskPoints, taskGroup));
                            taskAdapter.notifyItemInserted(filteredTaskList.size() - 1);
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
                            String oldTaskName = data.getStringExtra("OLD_TASK_NAME");
                            String taskName = data.getStringExtra("TASK_NAME");
                            int taskPoints = data.getIntExtra("TASK_POINTS", -1);
                            String taskGroup = data.getStringExtra("TASK_GROUP");
                            int position = data.getIntExtra("TASK_POSITION", -1);
                            filteredTaskList.get(position).setName(taskName);
                            filteredTaskList.get(position).setPoints(taskPoints);
                            filteredTaskList.get(position).setGroup(taskGroup);
                            modifyTask(oldTaskName, taskName, taskPoints, taskGroup);
                            setFilteredTasks();
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
        taskAdapter = new TaskAdapter(taskList, filteredTaskList, dataBank);
        recyclerView.setAdapter(taskAdapter);
        GlobalData.currentGroup.observe(getViewLifecycleOwner(), t -> {
            taskList = dataBank.loadObject();
            setFilteredTasks();
        });
        emptyTaskTextView = rootView.findViewById(R.id.empty_weekly_task_text_view);
        setFilteredTasks();
        checkIfEmpty();

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
        if (getActivity() instanceof MainActivity2) {
            ((MainActivity2) getActivity()).setCurrentSortModeListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof MainActivity2) {
            ((MainActivity2) getActivity()).setCurrentSortModeListener(null);
        }
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
                    deleteTask(filteredTaskList.get(position).getName());
                    filteredTaskList.remove(position);
                    taskAdapter.notifyItemRemoved(position);
                    dataBank.saveObject(taskList);
                });
                builder.setNegativeButton("否", (dialog, which) -> {});
                builder.show();
                checkIfEmpty();
                return true;
            case 2:
                intent = new Intent(getActivity(), ModifyTaskActivity.class);
                intent.putExtra("taskName", filteredTaskList.get(position).getName());
                intent.putExtra("taskPoints", filteredTaskList.get(position).getPoints());
                intent.putExtra("taskGroup", filteredTaskList.get(position).getGroup());
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

    @Override
    public void onEnterSortMode() {
        itemTouchHelper = new ItemTouchHelper(new DragAndDropTaskCallback(taskAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        taskAdapter.setContextMenuEnabled(false);
        taskAdapter.setSortMode(true);
    }

    @Override
    public void onExitSortMode() {
        itemTouchHelper.attachToRecyclerView(null);
        taskAdapter.setContextMenuEnabled(true);
        taskAdapter.setSortMode(false);
    }

    public void setFilteredTasks() {
        filteredTaskList.clear();
        for (TaskItem taskItem : taskList) {
            if (GlobalData.currentGroup.getValue().equals("全部")) {
                filteredTaskList.add(taskItem);
            }
            else if (GlobalData.currentGroup.getValue().equals("未分组") && taskItem.getGroup() == null) {
                filteredTaskList.add(taskItem);
            }
            else if (GlobalData.currentGroup.getValue().equals(taskItem.getGroup())) {
                filteredTaskList.add(taskItem);
            }
        }
        taskAdapter.notifyDataSetChanged();
        checkIfEmpty();
    }

    public void deleteTask(String taskName) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getName().equals(taskName)) {
                taskList.remove(i);
                break;
            }
        }
    }

    public void modifyTask(String oldtaskName, String taskName, int taskPoints, String taskGroup) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getName().equals(oldtaskName)) {
                taskList.get(i).setName(taskName);
                taskList.get(i).setPoints(taskPoints);
                taskList.get(i).setGroup(taskGroup);
                break;
            }
        }
    }
}