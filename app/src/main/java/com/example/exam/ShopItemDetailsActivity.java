package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ShopItemDetailsActivity extends AppCompatActivity {

    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_details);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            if (name != null) {
                Double price = intent.getDoubleExtra("price", 0);
                position = intent.getIntExtra("position", -1);
                EditText editTextItemName = findViewById(R.id.edittext_item_name);
                editTextItemName.setText(name);
                EditText editTextItemPrice = findViewById(R.id.edittext_item_price);
                editTextItemPrice.setText(price.toString());
            }
        }

        Button buttonOk = findViewById(R.id.button_item_details_ok);
        buttonOk.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            EditText editTextItemName = findViewById(R.id.edittext_item_name);
            EditText editTextItemPrice = findViewById(R.id.edittext_item_price);
            intent1.putExtra("name", editTextItemName.getText().toString());
            intent1.putExtra("price", editTextItemPrice.getText().toString());
            intent1.putExtra("position", position);
            setResult(Activity.RESULT_OK, intent1);
            finish();
        });
    }
}