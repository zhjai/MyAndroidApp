package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.exam.data.GroupDataBank;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyTaskActivity extends AppCompatActivity {

    private String oldTaskName;
    private EditText taskNameEditText;
    private EditText taskPointsEditText;
    private CheckBox taskGroupCheckBox;
    private Spinner taskGroupSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private Button submitButton;
    private ChipGroup chipGroup;
    private MaterialButtonToggleGroup toggleGroup;
    private GroupDataBank groupDataBank = new GroupDataBank(this, "groups");
    private Date date;
    int importance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setTitle("修改任务");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        taskNameEditText = findViewById(R.id.task_name_edit_text);
        taskPointsEditText = findViewById(R.id.task_points_edit_text);
        submitButton = findViewById(R.id.submit_button);
        taskGroupCheckBox = findViewById(R.id.check_box_task_group);
        taskGroupSpinner = findViewById(R.id.spinner_task_group);
        chipGroup = findViewById(R.id.group_date);
        Chip chipToday = findViewById(R.id.chip_today);
        Chip chipTomorrow = findViewById(R.id.chip_tomorrow);
        Chip chipChoose = findViewById(R.id.chip_choose);
        Chip chipNull = findViewById(R.id.chip_null);
        toggleGroup = findViewById(R.id.button_importance);
        Button buttonNormal = findViewById(R.id.button_normal);
        Button buttonImportant = findViewById(R.id.button_important);
        Button buttonUrgent = findViewById(R.id.button_urgent);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupDataBank.loadObject());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskGroupSpinner.setAdapter(spinnerAdapter);

        // 获取传递的任务数据
        String taskName = getIntent().getStringExtra("taskName");
        oldTaskName = taskName;
        int taskPoints = getIntent().getIntExtra("taskPoints", 0);
        String taskGroup = getIntent().getStringExtra("taskGroup");
        int taskPosition = getIntent().getIntExtra("taskPosition", -1);
        date = (Date) getIntent().getSerializableExtra("taskDate");
        importance = getIntent().getIntExtra("taskImportance", 0);

        // 设置输入框的数据
        taskNameEditText.setText(taskName);
        taskPointsEditText.setText(String.valueOf(taskPoints));
        taskGroupCheckBox.setChecked(taskGroup != null);
        taskGroupSpinner.setEnabled(taskGroup != null);

        if (taskGroup != null) {
            taskGroupSpinner.setSelection(spinnerAdapter.getPosition(taskGroup));
        }

        taskGroupCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            taskGroupSpinner.setEnabled(isChecked);
        });

        CalendarConstraints.Builder constrainsBuilder = new CalendarConstraints.Builder();
        constrainsBuilder.setStart(MaterialDatePicker.todayInUtcMilliseconds());
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("选择日期")
                .setCalendarConstraints(constrainsBuilder.build()).build();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        chipGroup.setOnCheckedStateChangeListener((group, checkedId) -> {
            if (checkedId.get(0) == R.id.chip_today) {
                date = new Date(System.currentTimeMillis());
                chipToday.setChipBackgroundColorResource(R.color.deep_green);
                chipToday.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
            else {
                chipToday.setChipBackgroundColorResource(R.color.gray_hint);
                chipToday.setTextColor(ContextCompat.getColor(this, R.color.gray));
            }
            if (checkedId.get(0) == R.id.chip_tomorrow) {
                date = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
                chipTomorrow.setChipBackgroundColorResource(R.color.deep_green);
                chipTomorrow.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
            else {
                chipTomorrow.setChipBackgroundColorResource(R.color.gray_hint);
                chipTomorrow.setTextColor(ContextCompat.getColor(this, R.color.gray));
            }
            if (checkedId.get(0) == R.id.chip_choose) {
                datePicker.show(getSupportFragmentManager(), "tag");
                datePicker.addOnPositiveButtonClickListener(selection -> {
                    date = new Date(selection);
                    chipChoose.setText(simpleDateFormat.format(date));
                    chipChoose.setChipBackgroundColorResource(R.color.deep_green);
                    chipChoose.setTextColor(ContextCompat.getColor(this, R.color.white));
                });
            }
            else {
                chipChoose.setText("选择日期");
                chipChoose.setChipBackgroundColorResource(R.color.gray_hint);
                chipChoose.setTextColor(ContextCompat.getColor(this, R.color.gray));
            }
            if (checkedId.get(0) == R.id.chip_null) {
                date = null;
                chipNull.setChipBackgroundColorResource(R.color.deep_green);
                chipNull.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
            else {
                chipNull.setChipBackgroundColorResource(R.color.gray_hint);
                chipNull.setTextColor(ContextCompat.getColor(this, R.color.gray));
            }
        });
        if (date == null) {
            chipNull.setChecked(true);
        }
        else if (simpleDateFormat.format(date).equals(simpleDateFormat.format(new Date(System.currentTimeMillis())))) {
            chipToday.setChecked(true);
        }
        else if (simpleDateFormat.format(date).equals(simpleDateFormat.format(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)))) {
            chipTomorrow.setChecked(true);
        }
        else {
            chipChoose.setText(simpleDateFormat.format(date));
            chipChoose.setChipBackgroundColorResource(R.color.deep_green);
            chipChoose.setTextColor(ContextCompat.getColor(this, R.color.white));
        }

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.button_normal) {
                importance = 0;
                buttonNormal.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
                buttonNormal.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
            else {
                buttonNormal.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_hint));
                buttonNormal.setTextColor(ContextCompat.getColor(this, R.color.gray));
            }
            if (checkedId == R.id.button_important) {
                importance = 1;
                buttonImportant.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
                buttonImportant.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
            else {
                buttonImportant.setBackgroundColor(ContextCompat.getColor(this, R.color.light_orange));
                buttonImportant.setTextColor(ContextCompat.getColor(this, R.color.gray));
            }
            if (checkedId == R.id.button_urgent) {
                importance = 2;
                buttonUrgent.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                buttonUrgent.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
            else {
                buttonUrgent.setBackgroundColor(ContextCompat.getColor(this, R.color.light_red));
                buttonUrgent.setTextColor(ContextCompat.getColor(this, R.color.gray));
            }
        });
        switch (importance) {
            case 0:
                buttonNormal.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
                buttonNormal.setTextColor(ContextCompat.getColor(this, R.color.white));
                break;
            case 1:
                buttonImportant.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
                buttonImportant.setTextColor(ContextCompat.getColor(this, R.color.white));
                break;
            case 2:
                buttonUrgent.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                buttonUrgent.setTextColor(ContextCompat.getColor(this, R.color.white));
                break;
            default:
                buttonNormal.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
                buttonNormal.setTextColor(ContextCompat.getColor(this, R.color.white));
                break;
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = taskNameEditText.getText().toString().trim();
                int taskPoints = Integer.parseInt(taskPointsEditText.getText().toString().trim());
                String taskGroup = taskGroupCheckBox.isChecked() ? taskGroupSpinner.getSelectedItem().toString() : null;
                // 这里您需要实现数据的返回逻辑，例如使用 Intent
                Intent resultIntent = new Intent();
                resultIntent.putExtra("OLD_TASK_NAME", oldTaskName);
                resultIntent.putExtra("TASK_NAME", taskName);
                resultIntent.putExtra("TASK_POINTS", taskPoints);
                resultIntent.putExtra("TASK_GROUP", taskGroup);
                resultIntent.putExtra("TASK_DATE", date);
                resultIntent.putExtra("TASK_IMPORTANCE", importance);
                resultIntent.putExtra("TASK_POSITION", taskPosition);
                setResult(RESULT_OK, resultIntent);
                finish();
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