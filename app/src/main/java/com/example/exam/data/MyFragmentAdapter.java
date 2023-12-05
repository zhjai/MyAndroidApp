package com.example.exam.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.exam.R;

public class MyFragmentAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] items;

    public MyFragmentAdapter(Context context, String[] items) {
        super(context, R.layout.my_fragment_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listItemView = inflater.inflate(R.layout.my_fragment_item, null, true);

        TextView textViewItem = listItemView.findViewById(R.id.textViewItem);
        textViewItem.setText(items[position]);

        return listItemView;
    }
}
