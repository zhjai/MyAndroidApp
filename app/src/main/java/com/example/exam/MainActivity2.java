package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.exam.data.AwardDataBank;
import com.example.exam.data.AwardItem;
import com.example.exam.data.DrawerListAdapter;
import com.example.exam.data.GlobalData;
import com.example.exam.data.GroupDataBank;
import com.example.exam.data.SortModeListener;
import com.example.exam.data.TaskDataBank;
import com.example.exam.data.TaskItem;
import com.example.exam.data.TimerViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.databinding.ActivityMain2Binding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements DrawerListAdapter.ItemClickListener{

    private ActivityMain2Binding binding;
    private SortModeListener currentSortModeListener;
    private boolean isInSortMode = false;
    DrawerLayout drawerLayout;
    GroupDataBank groupDataBank;
    TaskDataBank dailyTaskDataBank;
    TaskDataBank weeklyTaskDataBank;
    TaskDataBank normalTaskDataBank;
    AwardDataBank awardDataBank1;
    ArrayList<String> groupList;
    DrawerListAdapter drawerListAdapter;
    RecyclerView drawerRecyclerView;
    private TimerViewModel timerViewModel;
    BottomSheetDialogFragment bottomSheetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Exam);

        TaskDataBank taskDataBank = new TaskDataBank(this, "finishedTasks");
        AwardDataBank awardDataBank = new AwardDataBank(this, "finishedAwards");
        groupDataBank = new GroupDataBank(this, "groups");
        dailyTaskDataBank = new TaskDataBank(this, "dailyTasks");
        weeklyTaskDataBank = new TaskDataBank(this, "weeklyTasks");
        normalTaskDataBank = new TaskDataBank(this, "normalTasks");
        awardDataBank1 = new AwardDataBank(this, "awards");

        bottomSheetDialogFragment = new BottomSheetDialogFragment();
        bottomSheetDialogFragment.setBottomSheetListener(text -> {
            if (text.equals("按照名称排序")) {
                GlobalData.currentSortMode.setValue("按照名称排序");
            }
            else if (text.equals("按照日期排序")) {
                GlobalData.currentSortMode.setValue("按照日期排序");
            }
            else if (text.equals("按照重要程度排序")) {
                GlobalData.currentSortMode.setValue("按照重要程度排序");
            }
        });

        int points = 0;
        GlobalData.context = this;
        GlobalData.finishedTasks = taskDataBank.loadObject();
        GlobalData.finishedAwards = awardDataBank.loadObject();
        groupList = groupDataBank.loadObject();

        for (TaskItem taskItem : GlobalData.finishedTasks) {
            points += taskItem.getPoints();
        }
        for (AwardItem awardItem : GlobalData.finishedAwards) {
            points -= awardItem.getPoints();
        }

        GlobalData.setPoints(points);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerRecyclerView = findViewById(R.id.drawer_recycler_view);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_task, R.id.navigation_award, R.id.navigation_concentrate, R.id.navigation_statistics, R.id.navigation_my)
                .setOpenableLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            boolean isTopLevelDestination = destination.getId() == R.id.navigation_task || destination.getId() == R.id.navigation_award || destination.getId() == R.id.navigation_history;

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(isTopLevelDestination);
            }
            invalidateOptionsMenu();
        });

        navView.setOnItemSelectedListener(item -> {
            invalidateOptionsMenu();
            return NavigationUI.onNavDestinationSelected(item, navController);
        });
        navView.setOnItemReselectedListener(item -> {
            invalidateOptionsMenu();
        });

        TextView groupAddTextView = findViewById(R.id.add_group);
        groupAddTextView.setOnClickListener(v -> showAddGroupDialog());
        TextView groupAllTextView = findViewById(R.id.group_all);
        groupAllTextView.setOnClickListener(v -> {
            GlobalData.currentGroup.setValue("全部");
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        TextView groupNullTextView = findViewById(R.id.group_null);
        groupNullTextView.setOnClickListener(v -> {
            GlobalData.currentGroup.setValue("未分组");
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        GlobalData.currentGroup.observe(this, group -> {
            if (group.equals("全部")) {
                groupAllTextView.setBackgroundColor(getColor(R.color.light_green));
                groupNullTextView.setBackgroundColor(getColor(R.color.white));
            } else if (group.equals("未分组")) {
                groupAllTextView.setBackgroundColor(getColor(R.color.white));
                groupNullTextView.setBackgroundColor(getColor(R.color.light_green));
            } else {
                groupAllTextView.setBackgroundColor(getColor(R.color.white));
                groupNullTextView.setBackgroundColor(getColor(R.color.white));
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                TextView pointsTextView = findViewById(R.id.drawer_user_points);
                GlobalData.getPoints().observe(MainActivity2.this, points -> {
                    pointsTextView.setText("我的积分：" + points);
                });
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                invalidateOptionsMenu();
            }
        });

        drawerListAdapter = new DrawerListAdapter(getLayoutInflater(), groupList);
        drawerListAdapter.setClickListener(this);
        drawerRecyclerView.setAdapter(drawerListAdapter);

        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 先清除当前所有菜单项
        menu.clear();
        // 根据当前选中的Fragment加载对应的菜单资源
        if (isTaskFragmentDisplayed() || isAwardFragmentDisplayed()) {
            getMenuInflater().inflate(R.menu.menu_task_fragment, menu);

        } else if (isStatisticsFragmentDisplayed()) {
            getMenuInflater().inflate(R.menu.menu_statistics_fragment, menu);
        } else if (isMyFragmentDisplayed()) {
            getMenuInflater().inflate(R.menu.menu_my_fragment, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private boolean isTaskFragmentDisplayed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        return navController.getCurrentDestination().getId() == R.id.navigation_task;
    }

    private boolean isAwardFragmentDisplayed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        return navController.getCurrentDestination().getId() == R.id.navigation_award;
    }

    private boolean isStatisticsFragmentDisplayed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        return navController.getCurrentDestination().getId() == R.id.navigation_statistics;
    }

    private boolean isMyFragmentDisplayed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        return navController.getCurrentDestination().getId() == R.id.navigation_my;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.action_sort) {
                isInSortMode = !isInSortMode;
                item.setTitle(isInSortMode ? "确定" : "手动排序");

                if (currentSortModeListener != null) {
                    if (isInSortMode) {
                        currentSortModeListener.onEnterSortMode();
                    }
                    else {
                        currentSortModeListener.onExitSortMode();
                    }
                }
                return true;
            }
            else if (item.getItemId() == R.id.action_search) {
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == R.id.action_sort_sub) {
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "排序");
                return true;
            }
            else if (item.getItemId() == R.id.action_scan) {
                Intent intent = new Intent(this, ScanActivity.class);
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == R.id.action_share) {
                String link = "https://github.com/zhjai/MyAndroidApp";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, link);

                startActivity(Intent.createChooser(intent, "分享"));
                return true;
            }
            else if (item.getItemId() == R.id.action_help) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("title", "帮助");
                intent.putExtra("url", "https://github.com/zhjai/MyAndroidApp");
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == R.id.action_history) {
                // 跳转到历史Fragment
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
                navController.navigate(R.id.navigation_history);
                return true;
            }
            else if (item.getItemId() == R.id.action_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == android.R.id.home) {
                if (isTaskFragmentDisplayed() || isAwardFragmentDisplayed()) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    else {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                    return true;
                }
                else
                    return Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2).navigateUp()
                            || super.onSupportNavigateUp();
            }
            else {
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.ivDelete) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("删除分组");
            builder.setMessage("确定要删除分组吗？");
            builder.setPositiveButton("确定", (dialog, which) -> {

                ArrayList<TaskItem> taskItems = dailyTaskDataBank.loadObject();
                for (TaskItem taskItem : taskItems) {
                    if (taskItem.getGroup() != null && taskItem.getGroup().equals(groupList.get(position))) {
                        taskItem.setGroup(null);
                    }
                }
                dailyTaskDataBank.saveObject(taskItems);

                taskItems = weeklyTaskDataBank.loadObject();
                for (TaskItem taskItem : taskItems) {
                    if (taskItem.getGroup() != null && taskItem.getGroup().equals(groupList.get(position))) {
                        taskItem.setGroup(null);
                    }
                }
                weeklyTaskDataBank.saveObject(taskItems);

                taskItems = normalTaskDataBank.loadObject();
                for (TaskItem taskItem : taskItems) {
                    if (taskItem.getGroup() != null && taskItem.getGroup().equals(groupList.get(position))) {
                        taskItem.setGroup(null);
                    }
                }
                normalTaskDataBank.saveObject(taskItems);

                ArrayList<AwardItem> awardItems = awardDataBank1.loadObject();
                for (AwardItem awardItem : awardItems) {
                    if (awardItem.getGroup() != null && awardItem.getGroup().equals(groupList.get(position))) {
                        awardItem.setGroup(null);
                    }
                }
                awardDataBank1.saveObject(awardItems);

                if (GlobalData.currentGroup.getValue().equals(groupList.get(position))) {
                    GlobalData.currentGroup.setValue("全部");
                }

                groupList.remove(position);
                groupDataBank.saveObject(groupList);
                drawerListAdapter.notifyItemRemoved(position);

                drawerLayout.closeDrawer(GravityCompat.START);
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }
        else {
            GlobalData.currentGroup.setValue(groupList.get(position));
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void setCurrentSortModeListener(SortModeListener listener) {
        this.currentSortModeListener = listener;
    }

    private void showAddGroupDialog() {
        // 创建并设置AlertDialog
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_group, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        // 获取输入框和按钮的引用
        EditText inputGroupName = dialogView.findViewById(R.id.input_group_name);
        Button createGroupButton = dialogView.findViewById(R.id.create_group);
        ImageButton closeDialogButton = dialogView.findViewById(R.id.close_dialog);

        // 设置关闭按钮的点击事件
        closeDialogButton.setOnClickListener(v -> dialog.dismiss());

        // 设置创建分组按钮的点击事件
        createGroupButton.setOnClickListener(v -> {
            String groupName = inputGroupName.getText().toString();
            if (!groupName.isEmpty()) {
                addNewGroup(groupName);
                dialog.dismiss();
            } else {
                inputGroupName.setError("分组名不能为空");
            }
        });
    }

    private void addNewGroup(String groupName) {
        for (String group : groupList) {
            if (group.equals(groupName)) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setTitle("添加失败");
                builder.setMessage("分组已存在");
                builder.setPositiveButton("确定", null);
                builder.show();
                return;
            }
        }
        // 添加分组到列表并更新NavigationView
        groupList.add(groupName);
        groupDataBank.saveObject(groupList);
        drawerListAdapter.notifyItemInserted(groupList.size() - 1);
    }

    public TimerViewModel getTimerViewModel() {
        return timerViewModel;
    }
}