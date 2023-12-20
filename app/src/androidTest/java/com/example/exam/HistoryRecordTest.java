package com.example.exam;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.view.View;

@RunWith(AndroidJUnit4.class)
public class HistoryRecordTest {

    @Rule
    public ActivityScenarioRule<MainActivity2> activityRule =
            new ActivityScenarioRule<>(MainActivity2.class);

    @Test
    public void testHistoryRecord() {
        // 添加任务
        onView(withId(R.id.fab_add_daily_task)).perform(click());
        onView(withId(R.id.task_name_edit_text)).perform(replaceText("任务1"), closeSoftKeyboard());
        onView(withId(R.id.task_points_edit_text)).perform(replaceText("100"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());

        // 完成任务
        onView(withId(R.id.daily_task_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.checkbox_task)));

        // 等待一定时间，使得SnackBar消失
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 导航到统计页面
        onView(withId(R.id.navigation_statistics)).perform(click());

        // 打开历史记录
        onView(withId(R.id.action_history)).perform(click());

        // 验证任务是否在历史记录中
        onView(withId(R.id.history_recycler_view))
                .check(matches(hasDescendant(withText("任务1"))));
    }

    // 辅助方法，用于点击 RecyclerView 中的特定子视图
    private static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}