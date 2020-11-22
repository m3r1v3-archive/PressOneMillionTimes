package com.merive.press1mtimes;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.PreferenceManager;

import java.util.Calendar;

import static com.merive.press1mtimes.Rotation.runRotation;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SharedPreferences sharedPreferences;
    String vibrationState, notificationState, accelerationState;
    TextView label, counter;
    ImageButton button;
    SwitchCompat vibration, notification, acceleration;
    int score;

    SensorManager sensorManager;
    Sensor accelerometer;
    float[] axisData = new float[3];

    int HOUR = 12;
    int MINUTE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        counter = findViewById(R.id.counter);
        label = findViewById(R.id.label);
        button = findViewById(R.id.button);

        vibration = findViewById(R.id.vibration);
        notification = findViewById(R.id.notification);
        acceleration = findViewById(R.id.acceleration);

        setCounter();
        setSwitches();

        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
    }

    /* Set methods */
    public void setCounter() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        StringBuilder score = new StringBuilder(sharedPreferences.getString("score", ""));
        // Add 0s for counter
        while (score.length() != 6)
            score.insert(0, "0");
        counter.setText(score);
    }

    public void setSwitches() {
        vibrationState = sharedPreferences.getString("vibration", "");
        if (vibrationState.equals("on")) {
            vibration.setChecked(true);
        }

        notificationState = sharedPreferences.getString("notification", "");
        if (notificationState.equals("on")) {
            notification.setChecked(true);
            setAlarm();
        }

        accelerationState = sharedPreferences.getString("acceleration", "");
        if (accelerationState.equals("on")) {
            acceleration.setChecked(true);
        }
    }

    /* Click methods */
    public void buttonClick(View view) {
        score = Integer.parseInt(String.valueOf(counter.getText()));
        if (score == 999999) {
            sharedPreferences.edit().putString("score", "000000").apply();
            counter.setText(R.string.counter);

            Intent intent = new Intent(this, Finish.class);
            intent.putExtra("accelerationState", accelerationState);
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
            v.vibrate(250);
        }
    }

    public void clickVibration(View view) {
        if (vibration.isChecked()) {
            sharedPreferences.edit().putString("vibration", "on").apply();
            vibrationState = "on";
        } else {
            sharedPreferences.edit().putString("vibration", "off").apply();
            vibrationState = "off";
        }
    }

    public void clickNotification(View view) {
        if (notification.isChecked()) {
            sharedPreferences.edit().putString("notification", "on").apply();
            accelerationState = "on";
            setAlarm();
        } else {
            sharedPreferences.edit().putString("notification", "off").apply();
            accelerationState = "off";
            offAlarm();
        }
    }

    public void setAlarm() {
        Intent intent = new Intent(MainActivity.this, RemindBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, HOUR);
        calendar.set(Calendar.MINUTE, MINUTE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 60000,
                AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
    }

    public void offAlarm() {
        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, RemindBroadcast.class);
        PendingIntent pendingIntent =
                PendingIntent.getService(MainActivity.this, 0, intent,
                        0);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Press1MTimesChannel";
            String description = "Channel for Press1MTimes";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyPress1MTimes", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void clickAcceleration(View view) {
        if (acceleration.isChecked()) {
            sharedPreferences.edit().putString("acceleration", "on").apply();
            accelerationState = "on";
        } else {
            sharedPreferences.edit().putString("acceleration", "off").apply();
            accelerationState = "off";
        }
        runRotation(0, 0, label);
        runRotation(0, 0, counter);
        runRotation(0, 0, button);
    }

    public void clickReset(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        sharedPreferences.edit().putString("score", "000000").apply();
                        counter.setText(R.string.counter);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    /* Accelerometer methods */
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
        if (sharedPreferences.getString("acceleration", "").equals("on")) {
            int sensorType = sensorEvent.sensor.getType();
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                axisData = sensorEvent.values.clone();

                runRotation(axisData[1], axisData[0], label);
                runRotation(axisData[1], axisData[0], counter);
                runRotation(axisData[1], axisData[0], button);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        /* Don't write */
    }
}
