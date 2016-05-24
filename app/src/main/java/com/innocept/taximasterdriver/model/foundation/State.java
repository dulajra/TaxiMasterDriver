package com.innocept.taximasterdriver.model.foundation;

/**
 * Created by Dulaj on 14-Apr-16.
 */
public enum State {

    NOT_IN_SERVICE(1),
    AVAILABLE(2),
    GOING_FOR_HIRE(3),
    IN_HIRE(4);

    int value;

    State(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

}
