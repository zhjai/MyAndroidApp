package com.example.exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
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
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2 = view.findViewById(R.id.task_viewpage2);
        viewPager2.setSaveEnabled(false);
        tabLayout = view.findViewById(R.id.task_tablayout);
        List<String> titleList = initPageTitleList();
        TabLayoutChildAdapter tabLayoutChildAdapter =
                new TabLayoutChildAdapter(getActivity(), initChildFragmentList());
        // ViewPager2绑定Adapter
        viewPager2.setAdapter(tabLayoutChildAdapter);
        // 关联 TabLayout 和 ViewPager2
        new TabLayoutMediator(tabLayout, viewPager2, true,
                (tab, position) -> tab.setText(titleList.get(position)))
                .attach();

    }

    private List<String> initPageTitleList() {
        return Arrays.asList("每日任务", "每周任务", "普通任务");
    }

    private List<Fragment> initChildFragmentList() {
        //在 tablayout 的第一个fragment 中使用 RecycleView 优化一下页面
        return Arrays.asList(DailyTaskFragment.newInstance(),
                WeeklyTaskFragment.newInstance(),
                NormalTaskFragment.newInstance());
    }

    static class TabLayoutChildAdapter extends FragmentStateAdapter {

        private List<Fragment> fragmentList;



        public TabLayoutChildAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragmentList) {
            super(fragmentActivity);
            this.fragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}