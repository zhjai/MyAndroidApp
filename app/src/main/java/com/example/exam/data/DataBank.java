package com.example.exam.data;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class DataBank {
    final String DATA_FILENAME = "shopitems.data";
    public ArrayList<ShopItem> loadShopItems(Context context) {
        ArrayList<ShopItem> data = new ArrayList<>();
        try {
            FileInputStream fileIn = context.openFileInput(DATA_FILENAME);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            data = (ArrayList<ShopItem>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            Log.d("Serialization", "Data loaded successfully. Item count: " + data.size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void SaveShopItems(Context context, ArrayList<ShopItem> shopItems) {
        try {
            java.io.FileOutputStream fileOut = context.openFileOutput(DATA_FILENAME, Context.MODE_PRIVATE);
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(fileOut);
            out.writeObject(shopItems);
            out.close();
            fileOut.close();
            Log.d("Serialization", "Data is serialized and saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}