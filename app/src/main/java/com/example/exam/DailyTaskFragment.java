package com.example.exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exam.data.TaskAdapter;
import com.example.exam.data.TaskItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyTaskFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskItem> taskList = new ArrayList<>();

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_daily_task, container, false);
        recyclerView = rootView.findViewById(R.id.daily_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList.add(new TaskItem("写作业", 500));
        taskList.add(new TaskItem("看书", 200));
        taskList.add(new TaskItem("锻炼", 300));
        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);
        return rootView;
    }
}