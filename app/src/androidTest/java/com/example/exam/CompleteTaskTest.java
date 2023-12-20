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
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.not;

import static java.lang.Thread.sleep;

import android.view.View;

@RunWith(AndroidJUnit4.class)
public class CompleteTaskTest {

    @Rule
    public ActivityScenarioRule<MainActivity2> activityRule =
            new ActivityScenarioRule<>(MainActivity2.class);

    @Test
    public void testCompleteTask(){
        // 添加一个任务
        onView(withId(R.id.fab_add_daily_task)).perform(click());
        onView(withId(R.id.task_name_edit_text)).perform(replaceText("任务1"), closeSoftKeyboard());
        onView(withId(R.id.task_points_edit_text)).perform(replaceText("100"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());

        // 勾选任务完成的复选框
        onView(withId(R.id.daily_task_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.checkbox_task)));

        // 等待一定时间，使得任务完成的动画完成
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 验证任务是否从列表中消失
        onView(withId(R.id.daily_task_recycler_view))
                .check(matches(not(hasDescendant(withText("任务1")))));

        // 验证总分数是否为 100
        onView(withId(R.id.daily_tasks_total_points))
                .check(matches(withText("100")));
    }

    // 辅助方法用于点击 RecyclerView 中的特定子视图
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