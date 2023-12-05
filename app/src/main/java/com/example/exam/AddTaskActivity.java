package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = taskNameEditText.getText().toString().trim();
                int taskPoints = Integer.parseInt(taskPointsEditText.getText().toString().trim());
                // 这里您需要实现数据的返回逻辑，例如使用 Intent
                 Intent resultIntent = new Intent();
                 resultIntent.putExtra("TASK_NAME", taskName);
                 resultIntent.putExtra("TASK_POINTS", taskPoints);
                 setResult(RESULT_OK, resultIntent);
                 finish();
            }
        });
    }
}
