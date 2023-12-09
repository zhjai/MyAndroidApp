package com.example.exam.data;

import static com.example.exam.data.GlobalData.context;
import static com.example.exam.data.GlobalData.getPoints;

import android.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.ViewHolder>{
    private ArrayList<AwardItem> awardList;
    private ArrayList<AwardItem> filteredAwardList;
    private AwardDataBank dataBank;
    private AwardDataBank finishedDataBank = new AwardDataBank(GlobalData.context, "finishedAwards");
    private Boolean contextMenuEnabled = true;
    private Boolean isSortMode = false;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");

    public AwardAdapter(ArrayList<AwardItem> awardList, ArrayList<AwardItem> filteredAwardList, AwardDataBank dataBank) {
        this.awardList = awardList;
        this.filteredAwardList = filteredAwardList;
        this.dataBank = dataBank;
    }

    @Override
    public AwardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.award_item, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(AwardAdapter.ViewHolder holder, int position) {
        AwardItem awardItem = filteredAwardList.get(position);
        holder.textAward.setText(awardItem.getName());
        holder.textScore.setText("-" + awardItem.getPoints().toString());
        if (awardItem.getGroup() == null) {
            holder.textGroup.setText("未分组");
        }
        else {
            holder.textGroup.setText(awardItem.getGroup());
        }
        if (awardItem.getDate() == null) {
            holder.textDate.setText("无日期");
        }
        else {
            holder.textDate.setText(simpleDateFormat.format(awardItem.getDate()));
        }

        holder.itemView.setOnCreateContextMenuListener(isSortMode || !contextMenuEnabled ? null : holder);

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(false);
        holder.checkbox.setEnabled(!isSortMode);

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                AwardItem awardItem1 = filteredAwardList.get(currentPosition);
                if (isChecked) {
                    if (getPoints().getValue() < awardItem1.getPoints()) {
                        holder.checkbox.setChecked(false);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("完成失败");
                        builder.setMessage("积分不足");
                        builder.setPositiveButton("确定", (dialog, which) -> {});
                        builder.show();
                        return;
                    }
                    awardItem1.setDateTimeMillis(System.currentTimeMillis());
                    GlobalData.finishedAwards.add(awardItem1);
                    finishedDataBank.saveObject(GlobalData.finishedAwards);
                    GlobalData.setPoints(getPoints().getValue() - awardItem1.getPoints());
                    deleteAward(awardItem1.getName());
                    filteredAwardList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    dataBank.saveObject(awardList);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredAwardList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        AwardAdapter adapter;
        TextView textAward;
        TextView textGroup;
        TextView textScore;
        TextView textDate;
        CheckBox checkbox;

        ViewHolder(View view, final AwardAdapter adapter) {
            super(view);
            this.adapter = adapter;
            textAward = view.findViewById(R.id.award_name);
            textGroup = view.findViewById(R.id.award_group);
            textScore = view.findViewById(R.id.award_score);
            textDate = view.findViewById(R.id.award_date);
            checkbox = view.findViewById(R.id.checkbox_award);

            view.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if (!adapter.isSortMode) {
                contextMenu.add(0, 0, this.getAdapterPosition(), "添加奖励");
                contextMenu.add(0, 2, this.getAdapterPosition(), "修改奖励");
                contextMenu.add(0, 1, this.getAdapterPosition(), "删除奖励");
            }
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(filteredAwardList, i, i + 1);
            }
        }
        else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(filteredAwardList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        moveAward(filteredAwardList.get(fromPosition).getName(), filteredAwardList.get(toPosition).getName());
        dataBank.saveObject(awardList);
    }

    public void setContextMenuEnabled(boolean enabled) {
        contextMenuEnabled = enabled;
        notifyDataSetChanged(); // 刷新适配器以应用更改
    }

    public void setSortMode(boolean isSortMode) {
        this.isSortMode = isSortMode;
        notifyDataSetChanged();
    }

    public void deleteAward(String awardName) {
        for (int i = 0; i < awardList.size(); i++) {
            if (awardList.get(i).getName().equals(awardName)) {
                awardList.remove(i);
                break;
            }
        }
    }

    public void moveAward(String fromAwardName, String toAwardName) {
        int fromPosition = -1;
        int toPosition = -1;
        for (int i = 0; i < awardList.size(); i++) {
            if (awardList.get(i).getName().equals(fromAwardName)) {
                fromPosition = i;
            }
            if (awardList.get(i).getName().equals(toAwardName)) {
                toPosition = i;
            }
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(awardList, i, i + 1);
            }
        }
        else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(awardList, i, i - 1);
            }
        }
    }
}
