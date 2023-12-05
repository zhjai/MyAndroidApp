package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifyTaskActivity extends AppCompatActivity {

    private EditText taskNameEditText;
    private EditText taskPointsEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskNameEditText = findViewById(R.id.task_name_edit_text);
        taskPointsEditText = findViewById(R.id.task_points_edit_text);
        submitButton = findViewById(R.id.submit_button);

        // 获取传递的任务数据
        String taskName = getIntent().getStringExtra("taskName");
        int taskPoints = getIntent().getIntExtra("taskPoints", 0);
        int taskPosition = getIntent().getIntExtra("taskPosition", -1);

        // 设置输入框的数据
        taskNameEditText.setText(taskName);
        taskPointsEditText.setText(String.valueOf(taskPoints));

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = taskNameEditText.getText().toString().trim();
                int taskPoints = Integer.parseInt(taskPointsEditText.getText().toString().trim());
                // 这里您需要实现数据的返回逻辑，例如使用 Intent
                Intent resultIntent = new Intent();
                resultIntent.putExtra("TASK_NAME", taskName);
                resultIntent.putExtra("TASK_POINTS", taskPoints);
                resultIntent.putExtra("TASK_POSITION", taskPosition);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}