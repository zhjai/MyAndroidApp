package com.example.exam;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class DeleteTaskTest {

    @Rule
    public ActivityScenarioRule<MainActivity2> activityRule =
            new ActivityScenarioRule<>(MainActivity2.class);

    @Test
    public void testDeleteTask() {
        // 添加一个任务
        onView(withId(R.id.fab_add_daily_task)).perform(click());
        onView(withId(R.id.task_name_edit_text)).perform(replaceText("任务1"), closeSoftKeyboard());
        onView(withId(R.id.task_points_edit_text)).perform(replaceText("100"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());

        // 长按 RecyclerView 中的第一个任务项，打开上下文菜单
        onView(withId(R.id.daily_task_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        // 点击上下文菜单中的“删除任务”
        onView(withText("删除任务"))
                .perform(click());

        // 在弹出的对话框中点击“是”
        onView(withText("是"))
                .perform(click());

        // 验证任务是否被删除（任务不再显示）
        onView(withId(R.id.daily_task_recycler_view))
                .check(matches(not(hasDescendant(withText("任务1")))));
    }
}