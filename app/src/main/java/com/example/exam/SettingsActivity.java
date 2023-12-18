package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView accountAndSecurity = findViewById(R.id.fragment_settings_account_and_security);
        accountAndSecurity.setOnClickListener(v -> {
            Intent intent = new Intent(this, Account_and_Security_Activity.class);
            startActivity(intent);
        });

        TextView normalSettings = findViewById(R.id.settings_normal);
        normalSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, NormalSettingsActivity.class);
            startActivity(intent);
        });

        TextView modulesSettings = findViewById(R.id.settings_modules);
        modulesSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, ModulesActivity.class);
            startActivity(intent);
        });

        TextView eventsSettings = findViewById(R.id.settings_events);
        eventsSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventsSettingsActivity.class);
            startActivity(intent);
        });

        TextView recognitionSettings = findViewById(R.id.settings_recognition);
        recognitionSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecognitionSettingsActivity.class);
            startActivity(intent);
        });

        TextView wechatReminderSettings = findViewById(R.id.settings_wechat_reminder);
        wechatReminderSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, WechatReminderSettingsActivity.class);
            startActivity(intent);
        });

        TextView userAgreement = findViewById(R.id.settings_user_agreement);
        userAgreement.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserAgreementActivity.class);
            startActivity(intent);
        });

        TextView privacyPolicy = findViewById(R.id.settings_privacy_policy);
        privacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("title", "隐私政策");
            intent.putExtra("url", "file:///android_asset/privacy_policy.html");
            startActivity(intent);
        });

        TextView personalizedServiceSettings = findViewById(R.id.settings_personalized_service);
        personalizedServiceSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, PersonalizedServiceSettingsActivity.class);
            startActivity(intent);
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