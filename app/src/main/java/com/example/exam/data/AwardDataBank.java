package com.example.exam.data;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class AwardDataBank {
    private String filename;
    private Context context;

    public AwardDataBank(Context context, String filename) {
        this.context = context;
        this.filename = filename;
    }

    // 存储对象
    public void saveObject(ArrayList<AwardItem> awardList) {
        try {
            java.io.FileOutputStream fileOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(fileOut);
            out.writeObject(awardList);
            out.close();
            fileOut.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<AwardItem> loadObject() {
        ArrayList<AwardItem> awardList = new ArrayList<>();
        try {
            FileInputStream fileIn = context.openFileInput(filename);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            awardList = (ArrayList<AwardItem>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return awardList;
    }
}
