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
        // Used to provide time delays between actions, see details at http://droidtestlab.com/delay.html
        IdlingResource idlingResource;

        idlingResource = startTiming(8300);
        // Set text to 'anuradha' in AppCompatEditText with id R.id.input_password
        onView(withId(R.id.input_password)).perform(replaceText("anuradha"));
        stopTiming(idlingResource);

        idlingResource = startTiming(8300);
        // Set text to 'anuradha' in AppCompatEditText with id R.id.input_username
        onView(withId(R.id.input_username)).perform(replaceText("anuradha"));
        stopTiming(idlingResource);

        idlingResource = startTiming(8300);
        // Click at AppCompatButton with id R.id.btn_sign_in
        onView(withId(R.id.btn_sign_in)).perform(click());
        stopTiming(idlingResource);

        idlingResource = startTiming(4500);
        // Click at AppCompatButton with id R.id.button_state_available
        onView(withId(R.id.button_state_available)).perform(click());
        stopTiming(idlingResource);

        idlingResource = startTiming(1300);
        // Check Switch with id R.id.switch_location_updates
        onView(withId(R.id.switch_location_updates)).perform(click());
        stopTiming(idlingResource);

        idlingResource = startTiming(1400);
        // Click at AppCompatButton with id R.id.button_state_not_in_service
        onView(withId(R.id.button_state_not_in_service)).perform(click());
        stopTiming(idlingResource);

        idlingResource = startTiming(1400);
        // Uncheck Switch with id R.id.switch_location_updates
        onView(withId(R.id.switch_location_updates)).perform(click());
        stopTiming(idlingResource);
    }


    // See details at http://droidtestlab.com/delay.html
    public IdlingResource startTiming(long time) {
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(time);
        Espresso.registerIdlingResources(idlingResource);
        return idlingResource;
    }

    public void stopTiming(IdlingResource idlingResource) {
        Espresso.unregisterIdlingResources(idlingResource);
    }

    public class ElapsedTimeIdlingResource implements IdlingResource {
        private long startTime;
        private final long waitingTime;
        private ResourceCallback resourceCallback;

        public ElapsedTimeIdlingResource(long waitingTime) {
            this.startTime = System.currentTimeMillis();
            this.waitingTime = waitingTime;
        }

        @Override
        public String getName() {
            return ElapsedTimeIdlingResource.class.getName() + ":" + waitingTime;
        }

        @Override
        public boolean isIdleNow() {
            long elapsed = System.currentTimeMillis() - startTime;
            boolean idle = (elapsed >= waitingTime);
            if (idle) {
                resourceCallback.onTransitionToIdle();
            }
            return idle;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            this.resourceCallback = resourceCallback;
        }
    }
}