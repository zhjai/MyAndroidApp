package com.example.exam.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class GlobalData extends ViewModel {
    static public Context context;
    static private final MutableLiveData<Integer> points = new MutableLiveData<>();
    static public ArrayList<TaskItem> finishedTasks = new ArrayList<>();
    static public ArrayList<AwardItem> finishedAwards = new ArrayList<>();
    static public MutableLiveData<String> currentGroup = new MutableLiveData<>("全部");

    static public MutableLiveData<Integer> getPoints() {
        return points;
    }
    static public void setPoints(Integer points) {
        GlobalData.points.setValue(points);
    }

    static public MutableLiveData<String> getCurrentGroup() {
        return currentGroup;
    }

    static public void setCurrentGroup(String groupName) {
        GlobalData.currentGroup.setValue(groupName);
    }
}