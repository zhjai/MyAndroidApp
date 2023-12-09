package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.exam.data.GroupDataBank;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyAwardActivity extends AppCompatActivity {

    private String oldAwardName;
    private EditText awardNameEditText;
    private EditText awardPointsEditText;
    private CheckBox awardGroupCheckBox;
    private Spinner awardGroupSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private Button submitButton;
    private ChipGroup chipGroup;
    private GroupDataBank groupDataBank = new GroupDataBank(this, "groups");
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_award);
        setTitle("修改奖励");

        awardNameEditText = findViewById(R.id.award_name_edit_text);
        awardPointsEditText = findViewById(R.id.award_points_edit_text);
        submitButton = findViewById(R.id.submit_button);
        awardGroupCheckBox = findViewById(R.id.check_box_award_group);
        awardGroupSpinner = findViewById(R.id.spinner_award_group);
        chipGroup = findViewById(R.id.group_date);
        Chip chipToday = findViewById(R.id.chip_today);
        Chip chipTomorrow = findViewById(R.id.chip_tomorrow);
        Chip chipChoose = findViewById(R.id.chip_choose);
        Chip chipNull = findViewById(R.id.chip_null);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupDataBank.loadObject());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        awardGroupSpinner.setAdapter(spinnerAdapter);

        // 获取传递的奖励数据
        String awardName = getIntent().getStringExtra("awardName");
        oldAwardName = awardName;
        int awardPoints = getIntent().getIntExtra("awardPoints", 0);
        String awardGroup = getIntent().getStringExtra("awardGroup");
        int awardPosition = getIntent().getIntExtra("awardPosition", -1);
        date = (Date) getIntent().getSerializableExtra("awardDate");

        // 设置输入框的数据
        awardNameEditText.setText(awardName);
        awardPointsEditText.setText(String.valueOf(awardPoints));
        awardGroupCheckBox.setChecked(awardGroup != null);
        awardGroupSpinner.setEnabled(awardGroup != null);

        if (awardGroup != null) {
            awardGroupSpinner.setSelection(spinnerAdapter.getPosition(awardGroup));
        }

        awardGroupCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            awardGroupSpinner.setEnabled(isChecked);
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String awardName = awardNameEditText.getText().toString().trim();
                int awardPoints = Integer.parseInt(awardPointsEditText.getText().toString().trim());
                String awardGroup = awardGroupCheckBox.isChecked() ? awardGroupSpinner.getSelectedItem().toString() : null;
                // 这里您需要实现数据的返回逻辑，例如使用 Intent
                Intent resultIntent = new Intent();
                resultIntent.putExtra("OLD_AWARD_NAME", oldAwardName);
                resultIntent.putExtra("AWARD_NAME", awardName);
                resultIntent.putExtra("AWARD_POINTS", awardPoints);
                resultIntent.putExtra("AWARD_GROUP", awardGroup);
                resultIntent.putExtra("AWARD_DATE", date);
                resultIntent.putExtra("AWARD_POSITION", awardPosition);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}