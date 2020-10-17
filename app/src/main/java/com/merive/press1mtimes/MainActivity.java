package com.merive.press1mtimes;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView label, counter;
    SharedPreferences sharedPreferences;
    int score;

    // Variables for accelerometer
    SensorManager sensorManager;
    Sensor accelerometer;
    float[] axisData = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Init Activity
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Init counter & title
        counter = findViewById(R.id.counter);
        label = findViewById(R.id.label);
        /* Get score in storage */
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        StringBuilder score = new StringBuilder(sharedPreferences.getString("score", ""));
        // Add 0s for scoreTV
        while (score.length() != 6)
            score.insert(0, "0");
        counter.setText(score);

        /* Init sensorManager & accelerometer */
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
    }

    public void buttonClick(View view) {
        /* Click red button */
        score = Integer.parseInt(String.valueOf(counter.getText()));
        /* If score == 999999, them scoreTV = "000000" & make Toast */
        if (score == 999999) {
            /* Fix bug #1 (Check GitHub Issues) */
            sharedPreferences.edit().putString("score", "000000").apply();
            counter.setText(R.string.counter);
            /* Switch activity */
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        } else {
            // Update score
            score += 1;
            // Format score
            @SuppressLint("DefaultLocale") String result = String.format("%06d", score);
            // Edit score in storage
            sharedPreferences.edit().putString("score", result).apply();
            /* Set score in counter */
            counter.setText(result);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* Set accelerometer listener */
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        /* Stop accelerometer listener */
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /* Get values */
        int sensorType = sensorEvent.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            axisData = sensorEvent.values.clone();
        } else {
            return;
        }

        /* Set rotation for title */
        setXData(axisData[1], label);
        setYData(axisData[0], label);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        /* Don't write */
    }

    public void setXData(float data, View view) {
        if (Math.abs(data) * 10 < 40)
            view.animate().rotationX(data * 10).setDuration(200L).start();
    }

    public void setYData(float data, View view) {
        if (Math.abs(data) * 10 < 20)
            view.animate().rotationY(data * 10).setDuration(200L).start();
    }
}
