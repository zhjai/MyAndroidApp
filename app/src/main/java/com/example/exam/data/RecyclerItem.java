package com.example.exam.data;

import java.io.Serializable;
import java.util.Date;

public class RecyclerItem implements Serializable, Comparable<RecyclerItem> {
    String name;
    Integer points;
    long dateTimeMillis;
    private String group;
    private Date date;
    private int importance;


    public RecyclerItem(String name, Integer points) {
        this.name = name;
        this.points = points;
    }

    public RecyclerItem(String name, Integer points, String group) {
        this.name = name;
        this.points = points;
        this.group = group;
    }

    public RecyclerItem(String name, Integer points, String group, Date date, int importance) {
        this.name = name;
        this.points = points;
        this.group = group;
        this.date = date;
        this.importance = importance;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    @Override
    public int compareTo(RecyclerItem anotherItem) {
        return Long.compare(anotherItem.getDateTimeMillis(), this.getDateTimeMillis());
    }
}