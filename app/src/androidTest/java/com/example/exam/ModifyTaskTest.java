package com.example.exam;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class ModifyTaskTest {

    @Rule
    public ActivityScenarioRule<MainActivity2> activityRule =
            new ActivityScenarioRule<>(MainActivity2.class);

    @Test
    public void testModifyTask() {
        // 首先添加一个任务
        onView(withId(R.id.fab_add_daily_task)).perform(click());
        onView(withId(R.id.task_name_edit_text)).perform(replaceText("任务1"), closeSoftKeyboard());
        onView(withId(R.id.task_points_edit_text)).perform(replaceText("100"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());

        // 长按 RecyclerView 中的第一个任务项，打开上下文菜单
        onView(withId(R.id.daily_task_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        // 点击上下文菜单中的修改任务
        onView(withText("修改任务")).perform(click());

        // 假设 ModifyTaskActivity 已经打开
        onView(withId(R.id.task_name_edit_text)).perform(replaceText("任务2"), closeSoftKeyboard());
        onView(withId(R.id.task_points_edit_text)).perform(replaceText("200"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());

        // 验证 RecyclerView 中的任务是否已更新
        onView(withId(R.id.daily_task_recycler_view))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText("任务2"))));
        onView(withId(R.id.daily_task_recycler_view))
                .check(matches(hasDescendant(withText("+200"))));
    }
}