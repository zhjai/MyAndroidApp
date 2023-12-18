package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

public class UserAgreementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView tvUserAgreement = findViewById(R.id.tv_user_agreement);
        String userAgreementText = getString(R.string.user_agreement);
        tvUserAgreement.setText(Html.fromHtml(userAgreementText, Html.FROM_HTML_MODE_COMPACT));
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