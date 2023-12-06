package com.example.exam.data;

import android.annotation.SuppressLint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private ArrayList<RecyclerItem> historyList = new ArrayList<>();

    public HistoryAdapter() {
        this.historyList.addAll(GlobalData.finishedTasks);
        this.historyList.addAll(GlobalData.finishedAwards);
        sortHistory();
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryAdapter.ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        RecyclerItem historyItem = historyList.get(position);
        holder.historyName.setText(historyItem.getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.historyDate.setText(simpleDateFormat.format(historyItem.getDateTimeMillis()));
        if (historyItem instanceof TaskItem) {
            holder.historyScore.setTextColor(ContextCompat.getColor(GlobalData.context, R.color.azure));
            holder.historyScore.setText("+" + historyItem.getPoints().toString());
        }
        else {
            holder.historyScore.setTextColor(ContextCompat.getColor(GlobalData.context, R.color.red));
            holder.historyScore.setText("-" + historyItem.getPoints().toString());
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView historyName;
        TextView historyDate;
        TextView historyScore;

        ViewHolder(View view, final HistoryAdapter adapter) {
            super(view);
            historyName = view.findViewById(R.id.history_name);
            historyDate = view.findViewById(R.id.history_date);
            historyScore = view.findViewById(R.id.history_score);
        }
    }

    public void sortHistory() {
        Collections.sort(historyList);
        notifyDataSetChanged();
    }
}