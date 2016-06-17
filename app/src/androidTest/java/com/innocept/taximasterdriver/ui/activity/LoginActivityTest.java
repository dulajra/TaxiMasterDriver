package com.innocept.taximasterdriver.ui.activity;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static android.support.test.InstrumentationRegistry.*;

import android.support.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.runner.RunWith;
import org.junit.Rule;
import org.junit.Test;

import android.view.View;
import android.view.ViewGroup;

import com.innocept.taximasterdriver.R;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testGenerated() {

        // Set text to 'anuradha1234' in AppCompatEditText with id R.id.input_password
        onView(withId(R.id.input_password)).perform(replaceText("anuradha1234"));

        // Set text to 'anuradha' in AppCompatEditText with id R.id.input_username
        onView(withId(R.id.input_username)).perform(replaceText("anuradha"));

        // Click at AppCompatButton with id R.id.btn_sign_in
        onView(withId(R.id.btn_sign_in)).perform(click());

        // Click at AppCompatButton with id R.id.button_state_available
        onView(withId(R.id.button_state_available)).perform(click());

        // Check Switch with id R.id.switch_location_updates
        onView(withId(R.id.switch_location_updates)).perform(click());

        // Click at AppCompatButton with id R.id.button_state_not_in_service
        onView(withId(R.id.button_state_not_in_service)).perform(click());

        // Uncheck Switch with id R.id.switch_location_updates
        onView(withId(R.id.switch_location_updates)).perform(click());

        // Open options menu
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click at menu item with text 'Logout' and id R.id.action_logout
        onView(withText("Logout")).perform(click());

        // Click at AppCompatButton with id android.R.id.button2
        onView(withId(android.R.id.button2)).perform(click());

        // Open options menu
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click at menu item with text 'Settings' and id R.id.action_settings
        onView(withText("Settings")).perform(click());

        // Swipe up at ListView with id android.R.id.list
        onView(withId(android.R.id.list)).perform(swipeUp());

        // Swipe down at ListView with id android.R.id.list
        onView(withId(android.R.id.list)).perform(swipeDown());

        // Click at ImageButton with child index 1 of parent with id R.id.toolbar
        onView(nthChildOf(withId(R.id.toolbar), 1)).perform(click());

        // Open options menu
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click at menu item with text 'Logout' and id R.id.action_logout
        onView(withText("Logout")).perform(click());

        // Click at AppCompatButton with id android.R.id.button1
        onView(withId(android.R.id.button1)).perform(click());

    }


    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return false;
                }
                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(group) && view.equals(group.getChildAt(childPosition));
            }
        };
    }

}