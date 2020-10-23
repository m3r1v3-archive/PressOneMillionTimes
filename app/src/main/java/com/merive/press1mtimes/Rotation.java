package com.merive.press1mtimes;

import android.view.View;

public class Rotation {

    static long DURATION = 350L;
    static int FACTOR = 10;

    public static void setRotation(float axisX, float axisY, View view) {
        int axisXRounded = Math.round(axisX * FACTOR);
        int axisYRounded = Math.round(axisY * FACTOR);
        if (Math.abs(axisXRounded) < 50 && Math.abs(axisYRounded) < 50)
            if (Math.abs(axisXRounded) > Math.abs(axisYRounded))
                rotation(axisXRounded, 0, view);
            else if (Math.abs(axisXRounded) < Math.abs(axisYRounded))
                rotation(0, axisYRounded, view);
            else if (Math.abs(axisXRounded) == Math.abs(axisYRounded))
                rotation(0, 0, view);
    }

    public static void rotation(float X, float Y, View view) {
        view.animate().rotationX(X).setDuration(DURATION).start();
        view.animate().rotationY(Y).setDuration(DURATION).start();
    }
}
