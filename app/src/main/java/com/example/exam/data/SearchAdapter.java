package com.example.exam.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    private ArrayList<RecyclerItem> searchList;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");

    public SearchAdapter(ArrayList<RecyclerItem> searchList) {
        this.searchList = searchList;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchAdapter.ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position) {
        RecyclerItem searchItem = searchList.get(position);
        holder.searchName.setText(searchItem.getName());
        if (searchItem.getGroup() == null) {
            holder.searchGroup.setText("未分组");
        }
        else {
            holder.searchGroup.setText(searchItem.getGroup());
        }
        if (searchItem.getDate() == null) {
            holder.searchDate.setText("无日期");
        }
        else {
            holder.searchDate.setText(simpleDateFormat.format(searchItem.getDate()));
        }

        if (searchItem instanceof TaskItem) {
            holder.searchScore.setTextColor(ContextCompat.getColor(GlobalData.context, R.color.deep_green));
            holder.searchScore.setText("+" + searchItem.getPoints().toString());
        }
        else {
            holder.searchScore.setTextColor(ContextCompat.getColor(GlobalData.context, R.color.orange));
            holder.searchScore.setText("-" + searchItem.getPoints().toString());
        }
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView searchName;
        TextView searchDate;
        TextView searchGroup;
        TextView searchScore;

        ViewHolder(View view, final SearchAdapter adapter) {
            super(view);
            searchName = view.findViewById(R.id.search_name);
            searchDate = view.findViewById(R.id.search_date);
            searchGroup = view.findViewById(R.id.search_group);
            searchScore = view.findViewById(R.id.search_score);
        }
    }
}
