package com.example.exam.data;

import java.io.Serializable;

public class AwardItem extends RecyclerItem implements Serializable {

    public AwardItem(String name, Integer points) {
        super(name, points);
    }
}