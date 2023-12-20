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

@RunWith(AndroidJUnit4.class)
public class SearchTaskTest {

    @Rule
    public ActivityScenarioRule<MainActivity2> activityRule =
            new ActivityScenarioRule<>(MainActivity2.class);

    @Test
    public void testSearchTask() {
        // 添加一个任务
        onView(withId(R.id.fab_add_daily_task)).perform(click());
        onView(withId(R.id.task_name_edit_text)).perform(replaceText("任务1"), closeSoftKeyboard());
        onView(withId(R.id.task_points_edit_text)).perform(replaceText("100"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());

        // 打开 ActionBar 的更多，并点击搜索
        onView(withId(R.id.action_more)).perform(click());
        onView(withText("搜索")).perform(click());

        // 等待一定时间，使得 SearchActivity 打开
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 在 SearchActivity 中输入搜索内容
        onView(withId(R.id.search_edit_text)).perform(typeText("1"), closeSoftKeyboard());

        // 验证 search_recycler_view 是否显示了正确的任务
        onView(withId(R.id.search_recycler_view))
                .check(matches(hasDescendant(withText("任务1"))));
    }
}