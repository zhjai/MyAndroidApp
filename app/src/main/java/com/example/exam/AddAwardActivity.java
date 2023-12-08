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

public class AddAwardActivity extends AppCompatActivity {

    private EditText awardNameEditText;
    private EditText awardPointsEditText;
    private CheckBox awardGroupCheckBox;
    private Spinner awardGroupSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private Button submitButton;
    private GroupDataBank groupDataBank = new GroupDataBank(this, "groups");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_award);
        setTitle("添加奖励");

        awardNameEditText = findViewById(R.id.award_name_edit_text);
        awardPointsEditText = findViewById(R.id.award_points_edit_text);
        submitButton = findViewById(R.id.submit_button);
        awardGroupCheckBox = findViewById(R.id.check_box_award_group);
        awardGroupSpinner = findViewById(R.id.spinner_award_group);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupDataBank.loadObject());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        awardGroupSpinner.setAdapter(spinnerAdapter);

        awardGroupSpinner.setEnabled(false);
        awardGroupCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            awardGroupSpinner.setEnabled(isChecked);
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String awardName = awardNameEditText.getText().toString().trim();
                int awardPoints = Integer.parseInt(awardPointsEditText.getText().toString().trim());
                String awardGroup = awardGroupCheckBox.isChecked() ? awardGroupSpinner.getSelectedItem().toString() : null;
                // 这里您需要实现数据的返回逻辑，例如使用 Intent
                 Intent resultIntent = new Intent();
                 resultIntent.putExtra("AWARD_NAME", awardName);
                 resultIntent.putExtra("AWARD_GROUP", awardGroup);
                 resultIntent.putExtra("AWARD_POINTS", awardPoints);
                 setResult(RESULT_OK, resultIntent);
                 finish();
            }
        });
    }
}