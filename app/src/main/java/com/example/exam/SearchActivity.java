package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import com.example.exam.data.AwardDataBank;
import com.example.exam.data.AwardItem;
import com.example.exam.data.RecyclerItem;
import com.example.exam.data.SearchAdapter;
import com.example.exam.data.TaskDataBank;
import com.example.exam.data.TaskItem;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private TextInputEditText searchEditText;
    ArrayList<RecyclerItem> searchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        searchEditText = findViewById(R.id.search_edit_text);
        RecyclerView recyclerView = findViewById(R.id.search_recycler_view);

        TaskDataBank taskDataBank = new TaskDataBank(this, "dailyTasks");
        TaskDataBank weeklyTaskDataBank = new TaskDataBank(this, "weeklyTasks");
        TaskDataBank normalTaskDataBank = new TaskDataBank(this, "normalTasks");
        AwardDataBank awardDataBank = new AwardDataBank(this, "awards");
        ArrayList<TaskItem> dailyTasks = taskDataBank.loadObject();
        ArrayList<TaskItem> weeklyTasks = weeklyTaskDataBank.loadObject();
        ArrayList<TaskItem> normalTasks = normalTaskDataBank.loadObject();
        ArrayList<AwardItem> awardList = awardDataBank.loadObject();

        SearchAdapter searchAdapter = new SearchAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SearchAdapter(searchList));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchContent = searchEditText.getText().toString();
                searchList.clear();
                for (TaskItem taskItem : dailyTasks) {
                    if (taskItem.getName().contains(searchContent)) {
                        searchList.add(taskItem);
                    }
                }
                for (TaskItem taskItem : weeklyTasks) {
                    if (taskItem.getName().contains(searchContent)) {
                        searchList.add(taskItem);
                    }
                }
                for (TaskItem taskItem : normalTasks) {
                    if (taskItem.getName().contains(searchContent)) {
                        searchList.add(taskItem);
                    }
                }
                for (AwardItem awardItem : awardList) {
                    if (awardItem.getName().contains(searchContent)) {
                        searchList.add(awardItem);
                    }
                }
                searchAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 处理返回按钮的点击事件
        if (item.getItemId() == android.R.id.home) {
            finish(); // 关闭当前Activity，返回到前一个Activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}