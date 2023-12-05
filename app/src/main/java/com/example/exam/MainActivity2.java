package com.example.exam;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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


        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_my) {
                invalidateOptionsMenu();
            }
            return true;
        });
        navView.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.navigation_my) {
                invalidateOptionsMenu();
            }
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 根据当前选中的导航项决定是否显示齿轮图标
        getMenuInflater().inflate(R.menu.menu_my_fragment, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 根据当前选中的导航项决定是否显示齿轮图标
        boolean showSettings = isMyFragmentDisplayed();
        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        if (settingsItem != null) {
            settingsItem.setVisible(showSettings);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // Helper method to determine if MyFragment is currently displayed
    private boolean isMyFragmentDisplayed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            invalidateOptionsMenu();
        });
        return navController.getCurrentDestination().getId() == R.id.navigation_my;
    }
}