package com.merive.press1mtimes;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class About extends AppCompatActivity implements SensorEventListener {

    Button exit;
    ImageView easter;
    Handler handler;
    Handler.Callback callback;
    TextView title;

    // Variables for accelerometer
    SensorManager sensorManager;
    Sensor accelerometer;
    float[] axisData = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Init Activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* Init variables */
        exit = findViewById(R.id.exit);
        easter = findViewById(R.id.easter_egg);
        title = findViewById(R.id.title);

        /* Init callback for Handler */
        callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                return false;
            }
        };

        /* Init sensorManager & accelerometer */
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
    }

    public void exitClick(View view) {
        /* Set visibility exit & pascal */
        exit.setVisibility(View.INVISIBLE);
        easter.setVisibility(View.VISIBLE);

        /* Start easter egg animation */
        easter.animate().translationY(-100f).setDuration(200L).start();
        handler = new Handler(Objects.requireNonNull(Looper.myLooper()), callback);
        handler.postDelayed(new Runnable() {
            public void run() {
                easter.animate().translationY(100f).setDuration(200L).start();
            }
        }, 300);

        /* Finish layout */
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 500);
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
        setXData(axisData[1], title);
        setYData(axisData[0], title);
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