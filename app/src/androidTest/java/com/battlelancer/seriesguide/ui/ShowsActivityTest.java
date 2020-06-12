package com.battlelancer.seriesguide.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.battlelancer.seriesguide.R;
import com.battlelancer.seriesguide.provider.SeriesGuideContract;
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * To avoid flakiness, turn off system animations on the virtual or physical devices used for
 * testing. On your device, under Settings > Developer options, disable the following 3 settings:
 *
 * - Window animation scale
 * - Transition animation scale
 * - Animator duration scale
 */
@RunWith(AndroidJUnit4.class)
public class ShowsActivityTest {

    @Rule
    public ActivityTestRule<ShowsActivity> mActivityTestRule = new ActivityTestRule<>(
            ShowsActivity.class);

    @Before
    public void setUp() throws Exception {
        // delete the database and close the database helper inside the provider
        // to ensure a clean state for the add show test
        Context context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase(SeriesGuideDatabase.DATABASE_NAME);
        context.getContentResolver().query(SeriesGuideContract.Shows.CONTENT_URI_CLOSE,
                null, null, null, null);
    }



    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher,
            final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
