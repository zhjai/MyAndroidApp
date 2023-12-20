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
public class AddTaskTest {

    @Rule
    public ActivityScenarioRule<MainActivity2> activityRule =
            new ActivityScenarioRule<>(MainActivity2.class);

    @Test
    public void testAddTask() {
        // 1. 点击 FloatingActionButton 添加任务
        onView(withId(R.id.fab_add_daily_task)).perform(click());

        // 2. 检查是否打开了 AddTaskActivity
        onView(withId(R.id.task_name_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.task_points_edit_text)).check(matches(isDisplayed()));

        // 3. 输入任务名称和分数
        onView(withId(R.id.task_name_edit_text)).perform(replaceText("任务1"), closeSoftKeyboard());
        onView(withId(R.id.task_points_edit_text)).perform(replaceText("100"), closeSoftKeyboard());

        // 4. 可能需要点击保存按钮，这里假设它的id为 save_button
        onView(withId(R.id.submit_button)).perform(click());

        // 5. 检查是否返回到 MainActivity
        onView(withId(R.id.fab_add_daily_task)).check(matches(isDisplayed()));

        // 6. 检查 RecyclerView 中是否有新任务
        onView(withId(R.id.daily_task_recycler_view))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText("任务1"))));

        // 7. 验证新任务的分数
        onView(withId(R.id.daily_task_recycler_view))
                .check(matches(hasDescendant(withText("+100"))));
    }
}