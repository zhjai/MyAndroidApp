package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ShopItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_details);

        Button buttonOk = findViewById(R.id.button_item_details_ok);
        buttonOk.setOnClickListener(v -> {
            Intent intent = new Intent();
            EditText editTextItemName = findViewById(R.id.edittext_item_name);
            EditText editTextItemPrice = findViewById(R.id.edittext_item_price);
            intent.putExtra("name", editTextItemName.getText().toString());
            intent.putExtra("price", editTextItemPrice.getText().toString());
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}