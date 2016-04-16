package com.innocept.taximasterdriver.model.foundation;

/**
 * Created by Dulaj on 14-Apr-16.
 */
public enum State {

    AVAILABLE(1),
    IN_HIRE(2),
    NOT_IN_SERVICE(3),
    GOING_FOR_HIRE(4);

    int value;

    State(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

}
