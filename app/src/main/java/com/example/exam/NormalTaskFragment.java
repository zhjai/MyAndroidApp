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
import android.view.View;
import android.view.ViewGroup;

import com.example.exam.data.TaskAdapter;
import com.example.exam.data.TaskItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NormalTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NormalTaskFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskItem> taskList = new ArrayList<>();
    private ActivityResultLauncher<Intent> addTaskLauncher;

    public NormalTaskFragment() {
        // Required empty public constructor
    }

    public static NormalTaskFragment newInstance() {
        NormalTaskFragment fragment = new NormalTaskFragment();
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
                        }
                    }
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_normal_task, container, false);
        recyclerView = rootView.findViewById(R.id.normal_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList.add(new TaskItem("写作业", 500));
        taskList.add(new TaskItem("看书", 200));
        taskList.add(new TaskItem("锻炼", 300));
        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.fab_add_normal_task);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            addTaskLauncher.launch(intent);
        });
        return rootView;
    }
}