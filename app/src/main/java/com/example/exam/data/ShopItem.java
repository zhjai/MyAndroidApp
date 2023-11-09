package com.example.exam.data;

public class ShopItem {
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

    private final String name;
    private final double price;

    public ShopItem(String name_, double price_, int imageResourceId) {
        this.name = name_;
        this.price = price_;
        this.imageResourceId = imageResourceId;
    }
}
