package com.example.exam.data;

import java.io.Serializable;
import java.util.Date;

public class TaskItem extends RecyclerItem implements Serializable {

    public TaskItem(String name, Integer points) {
        super(name, points);
    }

    public TaskItem(String name, Integer points, String group) {
        super(name, points, group);
    }

    public TaskItem(String name, Integer points, String group, Date date, int importance) {
        super(name, points, group, date, importance);
    }
}