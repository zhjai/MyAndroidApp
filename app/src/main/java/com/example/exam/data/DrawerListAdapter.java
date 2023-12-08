package com.example.exam.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;

import java.util.List;

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.ViewHolder> {

    private List<String> groupNames; // 这是您的分组名称列表
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener; // 点击事件监听器接口

    // 适配器的构造函数
    public DrawerListAdapter(LayoutInflater inflater, List<String> data) {
        this.mInflater = inflater;
        this.groupNames = data;
    }

    // 创建新的视图（由布局管理器调用）
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.drawer_list_item, parent, false);
        return new ViewHolder(view);
    }

    // 绑定数据到正确的视图（由布局管理器调用）
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String groupName = groupNames.get(position);
        holder.tvGroupName.setText(groupName);
    }

    // 返回总的项数
    @Override
    public int getItemCount() {
        return groupNames.size();
    }

    // 存储和复用视图的容器类
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvGroupName;
        ImageView ivDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            itemView.setOnClickListener(this); // 设置整个分组项的点击事件
            ivDelete.setOnClickListener(this); // 设置删除图标的点击事件

            GlobalData.currentGroup.observeForever(groupName -> {
                if (groupName.equals(tvGroupName.getText().toString())) {
                    itemView.setBackgroundColor(mInflater.getContext().getColor(R.color.light_green));
                } else {
                    itemView.setBackgroundColor(mInflater.getContext().getColor(R.color.white));
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // 允许点击事件被捕获
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // 父活动将实现此方法来响应点击事件
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}