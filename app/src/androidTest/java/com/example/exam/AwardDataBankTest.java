package com.example.exam;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import com.example.exam.data.AwardDataBank;
import com.example.exam.data.AwardItem;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class AwardDataBankTest {

    @Test
    public void testLoadObject() {
        // 获取一个真实的 Context
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        ArrayList<AwardItem> AwardList1 = new ArrayList<>();
        AwardItem AwardItem = new AwardItem("test", 1000);
        AwardDataBank AwardDataBank = new AwardDataBank(context, "test");
        AwardList1.add(AwardItem);
        AwardDataBank.saveObject(AwardList1);
        ArrayList<AwardItem> AwardList2 = AwardDataBank.loadObject();
        assertEquals(AwardList1.get(0).getName(), AwardList2.get(0).getName());
        assertEquals(AwardList1.get(0).getPoints(), AwardList2.get(0).getPoints());
    }
}