package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mainRecyclerView = findViewById(R.id.recyclerview_main);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] itemNames = new String[]{"商品1", "商品2", "商品3"};
        CustomAdapter customAdapter = new CustomAdapter(itemNames);
        mainRecyclerView.setAdapter(customAdapter);
    }
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private String[] localDataSet;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textViewName;
            private final TextView textViewPrice;
            private final ImageView imageViewItem;

            public ViewHolder(View view) {
                super(view);

                textViewName = view.findViewById(R.id.textview_item_name);
                textViewPrice = view.findViewById(R.id.textview_item_price);
                imageViewItem = view.findViewById(R.id.imageview_item);
            }

            public TextView getTextViewName() {
                return textViewName;
            }

            public TextView getTextViewPrice() {
                return textViewPrice;
            }

            public ImageView getImageViewItem() {
                return imageViewItem;
            }
        }
        public CustomAdapter(String[] dataSet) {
            localDataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.shop_item_row, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.getTextViewName().setText(localDataSet[position]);
            viewHolder.getTextViewPrice().setText("123.00元");
        }

        @Override
        public int getItemCount() {
            return localDataSet.length;
        }
    }
}