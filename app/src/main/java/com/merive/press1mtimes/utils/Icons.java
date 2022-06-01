package com.merive.press1mtimes.utils;

public enum Icons {

    DEFAULT(0), SHORT(1), MILLION(2);

    private final int value;

    Icons(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
