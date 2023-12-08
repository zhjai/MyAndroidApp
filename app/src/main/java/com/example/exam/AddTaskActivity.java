package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exam.data.GroupDataBank;

public class AddTaskActivity extends AppCompatActivity {

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
        setTitle("添加任务");

        taskNameEditText = findViewById(R.id.task_name_edit_text);
        taskPointsEditText = findViewById(R.id.task_points_edit_text);
        submitButton = findViewById(R.id.submit_button);
        taskGroupCheckBox = findViewById(R.id.check_box_task_group);
        taskGroupSpinner = findViewById(R.id.spinner_task_group);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupDataBank.loadObject());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskGroupSpinner.setAdapter(spinnerAdapter);

        taskGroupSpinner.setEnabled(false);
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
                 resultIntent.putExtra("TASK_NAME", taskName);
                 resultIntent.putExtra("TASK_POINTS", taskPoints);
                 resultIntent.putExtra("TASK_GROUP", taskGroup);
                 setResult(RESULT_OK, resultIntent);
                 finish();
            }
        });
    }
}