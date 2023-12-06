package com.example.exam.data;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DragAndDropAwardCallback extends ItemTouchHelper.Callback{
    private final AwardAdapter adapter;

    public DragAndDropAwardCallback(AwardAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        // 返回true允许长按拖拽
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        // 返回false禁用侧滑删除
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 设置拖拽方向为上下
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // 调用适配器的方法来交换位置
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // 不处理侧滑
    }
}
