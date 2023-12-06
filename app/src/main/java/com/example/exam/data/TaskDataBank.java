package com.example.exam.data;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class TaskDataBank {

    private String filename;
    private Context context;

    public TaskDataBank(Context context, String filename) {
        this.context = context;
        this.filename = filename;
    }

    // 存储对象
    public void saveObject(ArrayList<TaskItem> taskList) {
        try {
            java.io.FileOutputStream fileOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(fileOut);
            out.writeObject(taskList);
            out.close();
            fileOut.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TaskItem> loadObject() {
        ArrayList<TaskItem> taskList = new ArrayList<>();
        try {
            FileInputStream fileIn = context.openFileInput(filename);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            taskList = (ArrayList<TaskItem>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return taskList;
    }
}