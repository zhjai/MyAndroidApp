package com.example.exam.data;

import androidx.fragment.app.Fragment;

import com.example.exam.DailyTaskFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public interface SortModeListener {
    void onEnterSortMode();

    void onExitSortMode();
}
