package com.merive.pressonemilliontimes.utils;

public enum Icons {

    DEFAULT(0), ONE(1), MINIMALISTIC(2);

    private final int value;

    Icons(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
