package com.example.exam.data;

import java.io.Serializable;

public class TaskItem extends RecyclerItem implements Serializable {

    public TaskItem(String name, Integer points) {
        super(name, points);
    }
}