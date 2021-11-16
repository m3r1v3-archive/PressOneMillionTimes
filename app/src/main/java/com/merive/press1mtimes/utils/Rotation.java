package com.merive.press1mtimes.utils;

import android.view.View;

public class Rotation {
    static final long DURATION = 350L;
    static final int FACTOR = 10;

    /**
     * This method is defining rotation for views.
     *
     * @param axisX X axis value.
     * @param axisY Y axis value.
     * @param view View what will rotate.
     */
    public static void defineRotation(float axisX, float axisY, View view) {
        int axisXRounded = Math.round(axisX * FACTOR);
        int axisYRounded = Math.round(axisY * FACTOR);
        if (Math.abs(axisXRounded) < 45 && Math.abs(axisYRounded) < 45)
            if (Math.abs(axisXRounded) > Math.abs(axisYRounded))
                setRotation(axisXRounded, 0, view);
            else if (Math.abs(axisXRounded) < Math.abs(axisYRounded))
                setRotation(0, axisYRounded, view);
            else if (Math.abs(axisXRounded) == Math.abs(axisYRounded))
                setRotation(0, 0, view);
    }

    /**
     * This method is setting rotation for View.
     *
     * @param X    X axis value.
     * @param Y    Y axis value.
     * @param view View what will rotate.
     */
    private static void setRotation(float X, float Y, View view) {
        view.animate().rotationX(X).rotationY(Y).setDuration(DURATION).start();
    }
}
