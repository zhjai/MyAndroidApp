package com.example.exam.data;

import java.io.Serializable;

public class AwardItem implements Serializable {
    private String name;
    private Integer points;

    public AwardItem(String name, Integer points) {
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
}
