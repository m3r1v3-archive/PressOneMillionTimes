package com.merive.press1mtimes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static com.merive.press1mtimes.utils.Rotation.defineRotation;

public class FinishActivity extends AppCompatActivity
        implements SensorEventListener {

    ImageButton exit;
    ImageView easter;
    Handler handler;
    Handler.Callback callback;
    TextView title, label, footer;
    Boolean accelerationState, vibrationState;

    SensorManager sensorManager;
    Sensor accelerometer;
    float[] axisData = new float[3];

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        initLayoutVariables();

        callback = message -> false;

        setStates();
        setSnowFallingVisibility();
        setSensors();
    }

    /* ************ */
    /* Init methods */
    /* ************ */
    public void initLayoutVariables() {
        /* Initializations layout variables */
        exit = findViewById(R.id.exit);
        easter = findViewById(R.id.easter_egg);
        title = findViewById(R.id.title);
        label = findViewById(R.id.label);
        footer = findViewById(R.id.footer);
    }

    /* *********** */
    /* Set methods */
    /* *********** */
    public void setStates() {
        accelerationState = MainActivity.accelerationState;
        vibrationState = MainActivity.vibrationState;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSnowFallingVisibility() {
        /* Set visibility for snow if it is winter */
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (localDate.getMonthValue() == 12 || localDate.getMonthValue() == 1)
            findViewById(R.id.snow).setVisibility(View.VISIBLE);
    }

    public void setSensors() {
        /* Set sensors variables */
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
    }

    public void clickExit(View view) {
        exit.setVisibility(View.INVISIBLE);
        easter.setVisibility(View.VISIBLE);

        /* Easter egg animation */
        makeAnimation();

        /* Finish layout */
        handler.postDelayed(this::finish, 500);
    }

    /* Make method */
    public void makeAnimation() {
        easter.animate().translationY(-100f).setDuration(200L).start();
        handler = new Handler(Objects.requireNonNull(Looper.myLooper()), callback);
        handler.postDelayed(() -> {
            easter.animate().translationY(80f).setDuration(200L).start();
            if (vibrationState) makeVibration();
        }, 300);
    }

    public void makeVibration() {
        /* Make vibrations on device */
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            v.vibrate(VibrationEffect.createOneShot(250,
                    VibrationEffect.DEFAULT_AMPLITUDE));
        else v.vibrate(250);
    }

    /* ******************** */
    /* Acceleration methods */
    /* ******************** */
    @Override
    protected void onStart() {
        super.onStart();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (accelerationState) {
            int sensorType = sensorEvent.sensor.getType();
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                axisData = sensorEvent.values.clone();

                defineRotation(axisData[1], axisData[0], title);
                defineRotation(axisData[1], axisData[0], label);
                defineRotation(axisData[1], axisData[0], footer);
                defineRotation(axisData[1], axisData[0], exit);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        /* Don't write */
    }
}