package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exam.data.GlobalData;

import org.w3c.dom.Text;

public class VipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView userNameTextView = findViewById(R.id.vip_username);
        GlobalData.getUserName().observe(this, userName -> {
            userNameTextView.setText(userName);
        });

        LinearLayout vipLinearLayout1 = findViewById(R.id.vip_linear_layout1);
        TextView vipMonthTextView1 = findViewById(R.id.vip_text_month1);
        TextView vipPriceTextView1 = findViewById(R.id.vip_text_price1);
        LinearLayout vipLinearLayout2 = findViewById(R.id.vip_linear_layout2);
        TextView vipMonthTextView2 = findViewById(R.id.vip_text_month2);
        TextView vipPriceTextView2 = findViewById(R.id.vip_text_price2);
        LinearLayout vipLinearLayout3 = findViewById(R.id.vip_linear_layout3);
        TextView vipMonthTextView3 = findViewById(R.id.vip_text_month3);
        TextView vipPriceTextView3 = findViewById(R.id.vip_text_price3);

        vipLinearLayout1.setOnClickListener(v -> {
            vipMonthTextView1.setTextColor(ContextCompat.getColor(this, R.color.yellow_orange));
            vipPriceTextView1.setTextColor(ContextCompat.getColor(this, R.color.yellow_orange));
            vipMonthTextView2.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipPriceTextView2.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipMonthTextView3.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipPriceTextView3.setTextColor(ContextCompat.getColor(this, R.color.black));

        });
        vipLinearLayout2.setOnClickListener(v -> {
            vipMonthTextView1.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipPriceTextView1.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipMonthTextView2.setTextColor(ContextCompat.getColor(this, R.color.yellow_orange));
            vipPriceTextView2.setTextColor(ContextCompat.getColor(this, R.color.yellow_orange));
            vipMonthTextView3.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipPriceTextView3.setTextColor(ContextCompat.getColor(this, R.color.black));
        });
        vipLinearLayout3.setOnClickListener(v -> {
            vipMonthTextView1.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipPriceTextView1.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipMonthTextView2.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipPriceTextView2.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipMonthTextView3.setTextColor(ContextCompat.getColor(this, R.color.yellow_orange));
            vipPriceTextView3.setTextColor(ContextCompat.getColor(this, R.color.yellow_orange));
        });

        vipLinearLayout1.performClick();

        Button vipOpenButton = findViewById(R.id.vip_open);
        vipOpenButton.setOnClickListener(v -> {
            Toast.makeText(this, "正在加紧开发中", Toast.LENGTH_SHORT).show();
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