package com.merive.press1mtimes;

import static com.merive.press1mtimes.utils.Rotation.defineRotation;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class FinishActivity extends AppCompatActivity
        implements SensorEventListener {

    ImageButton exit;
    Handler.Callback callback;
    TextView title, label, afterword;
    Boolean accelerationState, vibrationState;

    SensorManager sensorManager;
    Sensor accelerometer;
    float[] axisData = new float[3];


    /**
     * This method is the start point at the FinishActivity.
     *
     * @param savedInstanceState Used by super.onCreate method.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_finish);

        initLayoutVariables();

        callback = message -> false;

        setStates();
        setSensors();
    }

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

    /**
     * This overridden method is registering accelerator changes.
     *
     * @param sensorEvent SensorEvent object.
     * @see SensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (accelerationState) {
            int sensorType = sensorEvent.sensor.getType();
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                axisData = sensorEvent.values.clone();

                defineRotation(axisData[1], axisData[0], title);
                defineRotation(axisData[1], axisData[0], label);
                defineRotation(axisData[1], axisData[0], afterword);
                defineRotation(axisData[1], axisData[0], exit);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /**
     * This method is initializing main Layout Components.
     */
    private void initLayoutVariables() {
        title = findViewById(R.id.finish_title);
        label = findViewById(R.id.title);
        afterword = findViewById(R.id.afterword);
        exit = findViewById(R.id.exit);
    }

    /**
     * This method is getting states in MainActivity and assigning to variables in FinishActivity.
     */
    private void setStates() {
        accelerationState = MainActivity.accelerationState;
        vibrationState = MainActivity.vibrationState;
    }

    /**
     * This method is setting visibility for Coins effect.
     *
     * @see com.jetradarmobile.snowfall.SnowfallView
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCoinsVisibility() {
        findViewById(R.id.coins).setVisibility(View.VISIBLE);
    }

    /**
     * This method is setting sensors that using by application.
     */
    private void setSensors() {
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * This method is executing after clicking on Finish Button.
     *
     * @param view View object.
     * @see android.widget.Button
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void clickExit(View view) {
        makeVibration();
        setCoinsVisibility();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 5000);
    }

    /**
     * This method is making vibration.
     */
    private void makeVibration() {
        if (vibrationState) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(150L);
        }
    }
}