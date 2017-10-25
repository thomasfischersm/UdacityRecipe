package com.playposse.udacityrecipe;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.playposse.udacityrecipe.activity.LoadingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * An Espresso test that clicks a bit around in the app.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BasicEspressoTest {

    @Rule
    public ActivityTestRule<LoadingActivity> mActivityRule =
            new ActivityTestRule<>(LoadingActivity.class);

    private IntentServiceIdlingResource idlingResource;

    @Before
    public void registerIntentServiceIdlingResource() {
        Instrumentation instrumentation
                = InstrumentationRegistry.getInstrumentation();
        idlingResource = new IntentServiceIdlingResource(
                instrumentation.getTargetContext());
        Espresso.registerIdlingResources(idlingResource);
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void walkThroughTheApp() {
        // on recipe list activity
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
        onView(withText("Nutella Pie")).perform(click());

        // on recipe activity.
        onView(allOf(withText("Nutella Pie"), withId(R.id.recipe_name_text_view))).check(matches(isDisplayed()));
        onView(withText("2 CUP Graham Cracker crumbs")).check(matches(isDisplayed()));
        onView(withText("6 TBLSP unsalted butter, melted")).check(matches(isDisplayed()));
        onView(withText("Recipe Introduction")).check(matches(isDisplayed()));
        onView(withText("2. Prep the cookie crust.")).perform(click());

        // on step activity.
        onView(withText("2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.")).check(matches(isDisplayed()));

        // back at the top.
        Espresso.pressBack();
        onView(allOf(withText("Nutella Pie"), withId(R.id.recipe_name_text_view))).check(matches(isDisplayed()));
        Espresso.pressBack();
        Espresso.onView(withId(R.id.recipe_recycler_view)).perform(swipeUp());
        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, scrollTo()));
        onView(withText("Yellow Cake")).check(matches(isDisplayed()));

        // Try another recipe.
        onView(withText("Yellow Cake")).perform(click());
        onView(allOf(withText("Yellow Cake"), withId(R.id.recipe_name_text_view))).check(matches(isDisplayed()));
    }
}
