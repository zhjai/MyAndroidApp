package com.example.exam.data;

import java.util.ArrayList;
import java.util.Random;

public class ExampleData {
    static public ArrayList <Integer> countTask = new ArrayList<>();
    static public ArrayList <Integer> countAward = new ArrayList<>();
    static public ArrayList <Integer> pointsTask = new ArrayList<>();
    static public ArrayList <Integer> pointsAward = new ArrayList<>();
    static private final Random random = new Random();

    static public void refreshData() {
        countTask.clear();
        countAward.clear();
        pointsTask.clear();
        pointsAward.clear();
        for (int i = 0; i < 7; i++) {
            countTask.add(random.nextInt(20) + 10);
            countAward.add(random.nextInt(20) + 10);
            pointsTask.add(random.nextInt(3000) + 500);
            pointsAward.add(random.nextInt(3000) + 500);
        }
    }
}