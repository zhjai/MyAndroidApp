package com.example.exam;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exam.data.AwardItem;
import com.example.exam.data.GlobalData;
import com.example.exam.data.TaskItem;

import de.hdodenhof.circleimageview.CircleImageView;

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

        CircleImageView profileImageView = rootView.findViewById(R.id.profile_image);
        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Account_and_Security_Activity.class);
            startActivity(intent);
        });

        TextView userNameTextView = rootView.findViewById(R.id.username);
        GlobalData.getUserName().observe(getViewLifecycleOwner(), userName -> {
            userNameTextView.setText(userName);
        });
        userNameTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Account_and_Security_Activity.class);
            startActivity(intent);
        });

        TextView pointsTextView = rootView.findViewById(R.id.user_points);
        GlobalData.getPoints().observe(getViewLifecycleOwner(), points -> {
            pointsTextView.setText("我的积分：" + (points + (GlobalData.getIsSignedIn().getValue() ? 100 : 0)));
        });

        Button signInButton = rootView.findViewById(R.id.sign_in_button);
        GlobalData.getIsSignedIn().observe(getViewLifecycleOwner(), isSignedIn -> {
            pointsTextView.setText("我的积分：" + (GlobalData.getPoints().getValue() + (isSignedIn ? 100 : 0)));
            if (isSignedIn) {
                signInButton.setText("已签到+100");
                signInButton.setEnabled(false);
            } else {
                signInButton.setText("签到");
                signInButton.setEnabled(true);
            }
        });
        signInButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SigninActivity.class);
            startActivity(intent);
        });

        taskCountTextView = rootView.findViewById(R.id.text_completed_tasks);
        taskCountTextView.setText("完成任务数\n" + GlobalData.finishedTasks.size());

        taskPointsTextView = rootView.findViewById(R.id.text_task_points);
        taskPointsTextView.setText("消耗积分\n" + getTaskPoints());

        awardPointsTextView = rootView.findViewById(R.id.text_award_points);
        awardPointsTextView.setText("奖励积分\n" + getAwardPoints());

        Button openVipButton = rootView.findViewById(R.id.button_open_vip);
        openVipButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), VipActivity.class);
            startActivity(intent);
        });

        TextView settingsTextView = rootView.findViewById(R.id.fragment_settings_account_and_security);
        settingsTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        TextView helpTextView = rootView.findViewById(R.id.fragment_help);
        helpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title", "帮助");
            intent.putExtra("url", "https://github.com/zhjai/MyAndroidApp");
            startActivity(intent);
        });

        TextView updateTextView = rootView.findViewById(R.id.fragment_update);
        updateTextView.setOnClickListener(v -> {
            Toast toast = Toast.makeText(getContext(), "已经是最新版本", Toast.LENGTH_SHORT);
            toast.show();
        });

        TextView shareTextView = rootView.findViewById(R.id.fragment_share);
        shareTextView.setOnClickListener(v -> {
            String link = "https://github.com/zhjai/MyAndroidApp";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, link);

            startActivity(Intent.createChooser(intent, "分享"));
        });

        LinearLayout fragmentMyAcademy1 = rootView.findViewById(R.id.fragment_my_academy1);
        fragmentMyAcademy1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title", "如何度过大学的每一天");
            intent.putExtra("url", "https://zhuanlan.zhihu.com/p/587930552");
            startActivity(intent);
        });

        LinearLayout fragmentMyAcademy2 = rootView.findViewById(R.id.fragment_my_academy2);
        fragmentMyAcademy2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title", "大学生的职业生涯规划");
            intent.putExtra("url", "https://zhuanlan.zhihu.com/p/534387457");
            startActivity(intent);
        });

        LinearLayout fragmentMyAcademy3 = rootView.findViewById(R.id.fragment_my_academy3);
        fragmentMyAcademy3.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title", "Android如何入门和进阶");
            intent.putExtra("url", "https://zhuanlan.zhihu.com/p/658909393");
            startActivity(intent);
        });

        TextView aboutTextView = rootView.findViewById(R.id.fragment_my_about);
        aboutTextView.setOnClickListener(v -> {
            Toast toast = Toast.makeText(getContext(), "该版权为ZHJ所有，可作教育用途，不可商用", Toast.LENGTH_SHORT);
            toast.show();
        });

        return rootView;
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