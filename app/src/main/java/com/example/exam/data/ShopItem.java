package com.example.exam.data;

import java.io.Serializable;

public class ShopItem implements Serializable {
    public int getImageResourceId() {
        return imageResourceId;
    }

    private final int imageResourceId;

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    private String name;
    private double price;

    public ShopItem(String name_, double price_, int imageResourceId) {
        this.name = name_;
        this.price = price_;
        this.imageResourceId = imageResourceId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }
}
