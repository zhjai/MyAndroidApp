package com.example.exam;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.exam.data.AwardDataBank;
import com.example.exam.data.AwardItem;
import com.example.exam.data.GlobalData;
import com.example.exam.data.TaskDataBank;
import com.example.exam.data.TaskItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.exam.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TaskDataBank taskDataBank = new TaskDataBank(this, "finishedTasks");
        AwardDataBank awardDataBank = new AwardDataBank(this, "finishedAwards");

        int points = 0;
        GlobalData.context = this;
        GlobalData.finishedTasks = taskDataBank.loadObject();
        GlobalData.finishedAwards = awardDataBank.loadObject();

        for (TaskItem taskItem : GlobalData.finishedTasks) {
            points += taskItem.getPoints();
        }
        for (AwardItem awardItem : GlobalData.finishedAwards) {
            points -= awardItem.getPoints();
        }

        GlobalData.setPoints(points);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            invalidateOptionsMenu();
            return true;
        });
        navView.setOnItemReselectedListener(item -> {
            invalidateOptionsMenu();
        });
        navView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_task, R.id.navigation_award, R.id.navigation_statistics, R.id.navigation_my)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            invalidateOptionsMenu();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 根据当前选中的导航项决定是否显示齿轮图标
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
                // 处理手动排序逻辑
                return true;
            }
            else if (item.getItemId() == R.id.action_sort_sub) {
                // 处理排序逻辑
                return true;
            }
            else if (item.getItemId() == R.id.action_share) {
                // 处理分享逻辑
                return true;
            }
            else if (item.getItemId() == R.id.action_history) {
                // 跳转到历史Fragment
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
                navController.navigate(R.id.historyFragment);
                return true;
            }
            else if (item.getItemId() == android.R.id.home) {
                return Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2).navigateUp()
                        || super.onSupportNavigateUp();
            }
            else {
                return super.onOptionsItemSelected(item);
        }
    }
}