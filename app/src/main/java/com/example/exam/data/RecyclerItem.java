package com.example.exam.data;

import java.io.Serializable;

public class RecyclerItem implements Serializable, Comparable<RecyclerItem> {
    String name;
    Integer points;
    long dateTimeMillis;

    public RecyclerItem(String name, Integer points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public long getDateTimeMillis() {
        return dateTimeMillis;
    }

    public void setDateTimeMillis(long dateTimeMillis) {
        this.dateTimeMillis = dateTimeMillis;
    }

    @Override
    public int compareTo(RecyclerItem anotherItem) {
        return Long.compare(anotherItem.getDateTimeMillis(), this.getDateTimeMillis());
    }
}