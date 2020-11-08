package com.merive.press1mtimes;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import static com.merive.press1mtimes.Rotation.runRotation;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView label, counter;
    ImageButton button;
    SwitchCompat vibration;

    SharedPreferences sharedPreferences;
    String vibrationState;
    int score;

    // Variables for accelerometer
    SensorManager sensorManager;
    Sensor accelerometer;
    float[] axisData = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = findViewById(R.id.counter);
        label = findViewById(R.id.label);
        button = findViewById(R.id.button);

        // Settings
        vibration = findViewById(R.id.vibration);

        /* Get score in storage */
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        StringBuilder score = new StringBuilder(sharedPreferences.getString("score", ""));

        // Add 0s for counter
        while (score.length() != 6)
            score.insert(0, "0");
        counter.setText(score);

        vibrationState = sharedPreferences.getString("vibration", "");

        if (vibrationState.equals("on")) {
            vibration.setChecked(true);
        }

        /* Init sensorManager & accelerometer */
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
    }

    public void buttonClick(View view) {
        score = Integer.parseInt(String.valueOf(counter.getText()));
        if (score == 999999) {
            /* Fix bug #1 (Check GitHub Issues) */
            sharedPreferences.edit().putString("score", "000000").apply();
            counter.setText(R.string.counter);

            Intent intent = new Intent(this, Finish.class);
            startActivity(intent);
        } else {
            score += 1;
            @SuppressLint("DefaultLocale") String result = String.format("%06d", score);

            sharedPreferences.edit().putString("score", result).apply();
            counter.setText(result);
        }
        if (Integer.parseInt(sharedPreferences.getString("score", "")) % 100 == 0) {
            if (sharedPreferences.getString("vibration", "").equals("on"))
                vibration();
        }
    }

    public void vibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            // deprecated in API 26
            v.vibrate(250);
        }
    }

    public void clickVibration(View view) {
        if (vibration.isChecked()) {
            sharedPreferences.edit().putString("vibration", "on").apply();
        } else {
            sharedPreferences.edit().putString("vibration", "off").apply();
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

            /* Set rotation for views */
            runRotation(axisData[1], axisData[0], label);
            runRotation(axisData[1], axisData[0], counter);
            runRotation(axisData[1], axisData[0], button);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        /* Don't write */
    }
}
