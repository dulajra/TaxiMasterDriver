package com.innocept.taximasterdriver;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.innocept.taximasterdriver.model.Communicator;
import com.innocept.taximasterdriver.model.foundation.Location;
import com.innocept.taximasterdriver.model.foundation.State;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by Dulaj on 6/12/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CommunicatorTest {

    Communicator communicator;

    @Before
    public void testInit() {
        communicator = new Communicator();
    }

    @Test
    public void testLogin() {
        int resultSuccess = communicator.login("anuradha", "anuradha1234");
        int resultFailed = communicator.login("anuradha", "anuradha");

        assertThat(resultSuccess, is(0));
        assertThat(resultFailed, is(1));
    }

    @Test
    public void testUpdateState() throws Exception {
        boolean available = communicator.updateState(State.AVAILABLE);
        boolean goingForHire = communicator.updateState(State.GOING_FOR_HIRE);
        boolean inHire = communicator.updateState(State.IN_HIRE);
        boolean notInService = communicator.updateState(State.NOT_IN_SERVICE);

        assertThat(available, is(true));
        assertThat(goingForHire, is(false));
        assertThat(inHire, is(true));
        assertThat(notInService, is(true));
    }

    @Test
    public void testUpdateLocation() throws Exception {
        boolean result = communicator.updateLocation(new Location(7.8731, 80.7718));
        assertThat(result, is(true));
    }
}
