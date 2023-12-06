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

import java.util.ArrayList;

public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.ViewHolder>{
    private ArrayList<AwardItem> awardList;

    private AwardDataBank dataBank;
    private AwardDataBank finishedDataBank = new AwardDataBank(GlobalData.context, "finishedAwards");

    public AwardAdapter(ArrayList<AwardItem> awardList, AwardDataBank dataBank) {
        this.awardList = awardList;
        this.dataBank = dataBank;
    }

    @Override
    public AwardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.award_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AwardAdapter.ViewHolder holder, int position) {
        AwardItem awardItem = awardList.get(position);
        holder.textAward.setText(awardItem.getName());
        holder.textScore.setText("-" + awardItem.getPoints().toString());

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(false);

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                AwardItem awardItem1 = awardList.get(currentPosition);
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
                    awardList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    dataBank.saveObject(awardList);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return awardList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView textAward;
        TextView textScore;
        CheckBox checkbox;

        ViewHolder(View view) {
            super(view);
            textAward = view.findViewById(R.id.text_award);
            textScore = view.findViewById(R.id.text_award_score);
            checkbox = view.findViewById(R.id.checkbox_award);
            view.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, 0, this.getAdapterPosition(), "添加奖励");
            contextMenu.add(0, 2, this.getAdapterPosition(), "修改奖励");
            contextMenu.add(0, 1, this.getAdapterPosition(), "删除奖励");
        }

    }
}
