package com.example.exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.exam.data.AwardItem;
import com.example.exam.data.GlobalData;
import com.example.exam.data.MyFragmentAdapter;
import com.example.exam.data.TaskItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {

    TextView taskCountTextView;
    TextView taskPointsTextView;
    TextView awardPointsTextView;

    public MyFragment() {
        // Required empty public constructor
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);

        ListView listView = rootView.findViewById(R.id.my_fragment_list);
        String[] items = {"帮助", "关于", "版本更新"};
        MyFragmentAdapter adapter = new MyFragmentAdapter(getContext(), items);
        listView.setAdapter(adapter);

        TextView pointsTextView = rootView.findViewById(R.id.user_points);
        GlobalData.getPoints().observe(getViewLifecycleOwner(), points -> {
            pointsTextView.setText("我的积分：" + points);
        });

        taskCountTextView = rootView.findViewById(R.id.text_completed_tasks);
        taskCountTextView.setText("完成任务数\n" + GlobalData.finishedTasks.size());

        taskPointsTextView = rootView.findViewById(R.id.text_task_points);
        taskPointsTextView.setText("消耗积分\n" + getTaskPoints());

        awardPointsTextView = rootView.findViewById(R.id.text_award_points);
        awardPointsTextView.setText("奖励积分\n" + getAwardPoints());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        taskCountTextView.setText("完成任务数\n" + GlobalData.finishedTasks.size());
        taskPointsTextView.setText("消耗积分\n" + getTaskPoints());
        awardPointsTextView.setText("奖励积分\n" + getAwardPoints());
    }

    public int getTaskPoints() {
        int points = 0;
        for (TaskItem taskItem : GlobalData.finishedTasks) {
            points += taskItem.getPoints();
        }
        return points;
    }

    public int getAwardPoints() {
        int points = 0;
        for (AwardItem awardItem : GlobalData.finishedAwards) {
            points += awardItem.getPoints();
        }
        return points;
    }
}