package com.innocept.taximasterdriver;

import com.innocept.taximasterdriver.model.Communicator;
import com.innocept.taximasterdriver.model.foundation.State;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(true, Communicator.isOnline());
    }
}