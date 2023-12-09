package com.example.exam.data;

import java.io.Serializable;
import java.util.Date;

public class AwardItem extends RecyclerItem implements Serializable {

    public AwardItem(String name, Integer points) {
        super(name, points);
    }

    public AwardItem(String name, Integer points, String group) {
        super(name, points, group);
    }

    public AwardItem(String name, Integer points, String group, Date date) {
        super(name, points, group, date);
    }
}