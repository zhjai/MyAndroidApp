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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

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
                            Date date = (Date) data.getSerializableExtra("TASK_DATE");
                            int importance = data.getIntExtra("TASK_IMPORTANCE", 0);
                            taskList = dataBank.loadObject();
                            for (TaskItem taskItem : taskList) {
                                if (taskItem.getName().equals(taskName)) {
                                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
                                    builder.setTitle("添加失败");
                                    builder.setMessage("任务已存在");
                                    builder.setPositiveButton("确定", (dialog, which) -> {});
                                    builder.show();
                                    return;
                                }
                            }
                            filteredTaskList.add(new TaskItem(taskName, taskPoints, taskGroup, date, importance));
                            taskList.add(new TaskItem(taskName, taskPoints, taskGroup, date, importance));
                            taskAdapter.notifyItemInserted(filteredTaskList.size() - 1);
                            dataBank.saveObject(taskList);
                            checkIfEmpty();
                            Snackbar.make(recyclerView, "添加一个任务", Snackbar.LENGTH_LONG).show();
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
                            Date date = (Date) data.getSerializableExtra("TASK_DATE");
                            int importance = data.getIntExtra("TASK_IMPORTANCE", 0);
                            int position = data.getIntExtra("TASK_POSITION", -1);
                            taskList = dataBank.loadObject();
                            filteredTaskList.get(position).setName(taskName);
                            filteredTaskList.get(position).setPoints(taskPoints);
                            filteredTaskList.get(position).setGroup(taskGroup);
                            filteredTaskList.get(position).setDate(date);
                            filteredTaskList.get(position).setImportance(importance);
                            modifyTask(oldTaskName, taskName, taskPoints, taskGroup, date, importance);
                            setFilteredTasks();
                            dataBank.saveObject(taskList);
                            Snackbar.make(recyclerView, "修改一个任务", Snackbar.LENGTH_LONG).show();
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

        GlobalData.getPoints().observe(getViewLifecycleOwner(), points -> {
            TextView pointsTextView = rootView.findViewById(R.id.weekly_tasks_total_points);
            pointsTextView.setText(points + (GlobalData.getIsSignedIn().getValue() ? 100 : 0) + "");
        });

        GlobalData.getCurrentSortMode().observe(getViewLifecycleOwner(), currenSortMode -> {
            if (currenSortMode == "") return;
            taskList.sort(new Comparator<TaskItem>() {
                @Override
                public int compare(TaskItem o1, TaskItem o2) {
                    if (currenSortMode == "按照名称排序") return o1.getName().compareTo(o2.getName());
                    else if (currenSortMode == "按照日期排序") return o1.getDate().compareTo(o2.getDate());
                    else if (currenSortMode == "按照重要程度排序") return o2.getImportance() - o1.getImportance();
                    return 0;
                }
            });
            dataBank.saveObject(taskList);
            filteredTaskList.sort(new Comparator<TaskItem>() {
                @Override
                public int compare(TaskItem o1, TaskItem o2) {
                    if (currenSortMode == "按照名称排序") return o1.getName().compareTo(o2.getName());
                    else if (currenSortMode == "按照日期排序") return o1.getDate().compareTo(o2.getDate());
                    else if (currenSortMode == "按照重要程度排序") return o2.getImportance() - o1.getImportance();
                    return 0;
                }
            });
            taskAdapter.notifyDataSetChanged();
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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
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
                Snackbar.make(recyclerView, "删除一个任务", Snackbar.LENGTH_LONG).show();
                return true;
            case 2:
                intent = new Intent(getActivity(), ModifyTaskActivity.class);
                intent.putExtra("taskName", filteredTaskList.get(position).getName());
                intent.putExtra("taskPoints", filteredTaskList.get(position).getPoints());
                intent.putExtra("taskGroup", filteredTaskList.get(position).getGroup());
                intent.putExtra("taskDate", filteredTaskList.get(position).getDate());
                intent.putExtra("taskImportance", filteredTaskList.get(position).getImportance());
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

    public void modifyTask(String oldtaskName, String taskName, int taskPoints, String taskGroup, Date date, int importance) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getName().equals(oldtaskName)) {
                taskList.get(i).setName(taskName);
                taskList.get(i).setPoints(taskPoints);
                taskList.get(i).setGroup(taskGroup);
                taskList.get(i).setDate(date);
                taskList.get(i).setImportance(importance);
                break;
            }
        }
    }
}