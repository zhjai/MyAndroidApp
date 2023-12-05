package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAwardActivity extends AppCompatActivity {

    private EditText awardNameEditText;
    private EditText awardPointsEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_award);

        awardNameEditText = findViewById(R.id.award_name_edit_text);
        awardPointsEditText = findViewById(R.id.award_points_edit_text);
        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String awardName = awardNameEditText.getText().toString().trim();
                int awardPoints = Integer.parseInt(awardPointsEditText.getText().toString().trim());
                // 这里您需要实现数据的返回逻辑，例如使用 Intent
                 Intent resultIntent = new Intent();
                 resultIntent.putExtra("AWARD_NAME", awardName);
                 resultIntent.putExtra("AWARD_POINTS", awardPoints);
                 setResult(RESULT_OK, resultIntent);
                 finish();
            }
        });
    }
}