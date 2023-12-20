package com.example.exam;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import com.example.exam.data.TaskDataBank;
import com.example.exam.data.TaskItem;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class TaskDataBankTest {

    @Test
    public void testLoadObject() {
        // 获取一个真实的 Context
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        ArrayList<TaskItem> taskList1 = new ArrayList<>();
        TaskItem taskItem = new TaskItem("test", 1000);
        TaskDataBank taskDataBank = new TaskDataBank(context, "test");
        taskList1.add(taskItem);
        taskDataBank.saveObject(taskList1);
        ArrayList<TaskItem> taskList2 = taskDataBank.loadObject();
        assertEquals(taskList1.get(0).getName(), taskList2.get(0).getName());
        assertEquals(taskList1.get(0).getPoints(), taskList2.get(0).getPoints());
    }
}