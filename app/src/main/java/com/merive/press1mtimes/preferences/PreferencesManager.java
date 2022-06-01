package com.merive.press1mtimes.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.merive.press1mtimes.utils.Icons;

public class PreferencesManager {

    private final SharedPreferences sharedPreferences;

    public PreferencesManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getScore() {
        return Integer.parseInt(sharedPreferences.getString("score", "000000"));
    }

    public void setScore(int score) {
        sharedPreferences.edit().putString("score", String.format("%06d", score)).apply();
    }

    public void setScore() {
        sharedPreferences.edit().putString("score", String.format("%06d", 0)).apply();
    }

    public boolean getVibration() {
        return sharedPreferences.getBoolean("vibration", false);
    }

    public void setVibration(boolean value) {
        sharedPreferences.edit().putBoolean("vibration", value).apply();
    }

    public boolean getNotification() {
        return sharedPreferences.getBoolean("notification", false);
    }

    public void setNotification(boolean value) {
        sharedPreferences.edit().putBoolean("notification", value).apply();
    }

    public boolean getAnimation() {
        return sharedPreferences.getBoolean("animation", false);
    }

    public void setAnimation(boolean value) {
        sharedPreferences.edit().putBoolean("animation", value).apply();
    }

    public boolean getSplash() {
        return sharedPreferences.getBoolean("splash", false);
    }

    public void setSplash(boolean value) {
        sharedPreferences.edit().putBoolean("splash", value).apply();
    }

    public float getSplashPositionVertical() {
        return sharedPreferences.getFloat("splash_vertical", 0.98f);
    }

    public float getSplashPositionHorizontal() {
        return sharedPreferences.getFloat("splash_horizontal", 0.98f);
    }

    public void setSplashPosition(float horizontalValue, float verticalValue) {
        sharedPreferences.edit().putFloat("splash_horizontal", horizontalValue).putFloat("splash_vertical", verticalValue).apply();
    }

    public int getIcon() {
        return sharedPreferences.getInt("icon", Icons.DEFAULT.getValue());
    }

    public void setIcon(int value) {
        sharedPreferences.edit().putInt("icon", value).apply();
    }
}
