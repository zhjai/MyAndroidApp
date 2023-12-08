package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.exam.data.GroupDataBank;

public class ModifyTaskActivity extends AppCompatActivity {

    private String oldTaskName;
    private EditText taskNameEditText;
    private EditText taskPointsEditText;
    private CheckBox taskGroupCheckBox;
    private Spinner taskGroupSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private Button submitButton;
    private GroupDataBank groupDataBank = new GroupDataBank(this, "groups");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setTitle("修改任务");

        taskNameEditText = findViewById(R.id.task_name_edit_text);
        taskPointsEditText = findViewById(R.id.task_points_edit_text);
        submitButton = findViewById(R.id.submit_button);
        taskGroupCheckBox = findViewById(R.id.check_box_task_group);
        taskGroupSpinner = findViewById(R.id.spinner_task_group);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupDataBank.loadObject());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskGroupSpinner.setAdapter(spinnerAdapter);

        // 获取传递的任务数据
        String taskName = getIntent().getStringExtra("taskName");
        oldTaskName = taskName;
        int taskPoints = getIntent().getIntExtra("taskPoints", 0);
        String taskGroup = getIntent().getStringExtra("taskGroup");
        int taskPosition = getIntent().getIntExtra("taskPosition", -1);

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
                resultIntent.putExtra("TASK_POSITION", taskPosition);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}