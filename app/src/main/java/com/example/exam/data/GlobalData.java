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
    static public MutableLiveData<String> currentSortMode = new MutableLiveData<>("");
    static public MutableLiveData<Boolean> isSignedIn = new MutableLiveData<>(false);

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

    static public MutableLiveData<String> getCurrentSortMode() {
        return currentSortMode;
    }

    static public void setCurrentSortMode(String currentSortMode) {
        GlobalData.currentGroup.setValue(currentSortMode);
    }

    static public MutableLiveData<Boolean> getIsSignedIn() {
        return isSignedIn;
    }

    static public void setIsSignedIn(Boolean isSignedIn) {
        GlobalData.isSignedIn.setValue(isSignedIn);
    }
}