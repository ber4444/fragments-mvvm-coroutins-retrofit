package com.example;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    @Rule public ActivityScenarioRule activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void findSomeView(){
        onView(withId(R.id.action_search)).check(matches(isDisplayed()));
        onView(withId(com.google.android.material.R.id.search_src_text)).perform(typeText("blah"));
        onView(withId(com.google.android.material.R.id.search_src_text)).perform(pressImeActionButton());
        try { Thread.sleep(2000); } catch (Exception e) {}
        onView(withId(R.id.recycler_main)).check(new RecyclerViewItemCountAssertion(100));
        onView(withRecyclerView(R.id.recycler_main)
            .atPositionOnView(0, R.id.text_item))
            .check(matches(withText(containsString("G08A8836.jpg"))));

        onView(withId(R.id.recycler_main)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.pic)).check(matches(isDisplayed()));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {

        return new RecyclerViewMatcher(recyclerViewId);
    }
}
