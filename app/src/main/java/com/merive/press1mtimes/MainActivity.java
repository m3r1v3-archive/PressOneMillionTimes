package com.merive.press1mtimes;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jetradarmobile.snowfall.SnowfallView;
import com.merive.press1mtimes.fragments.ChangeIconFragment;
import com.merive.press1mtimes.fragments.ConfirmFragment;
import com.merive.press1mtimes.fragments.OptionsFragment;
import com.merive.press1mtimes.fragments.ScoreShareFragment;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static com.merive.press1mtimes.utils.Rotation.runRotation;
import static java.util.Calendar.YEAR;


public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    static SharedPreferences sharedPreferences;
    static int score;
    static Boolean vibrationState;
    static Boolean accelerationState;
    Boolean notificationState;
    TextView label, counter, info;
    ImageButton button;
    SwitchCompat vibration, notification, acceleration;
    SensorManager sensorManager;
    Sensor accelerometer;
    float[] axisData = new float[3];
    int HOUR = 12;
    int MINUTE = 0;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Press1MTimes);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());

        /* Initializations layout variables */
        counter = findViewById(R.id.counter);
        label = findViewById(R.id.label);
        button = findViewById(R.id.button);

        /* Initializations settings variables */
        vibration = findViewById(R.id.vibration);
        notification = findViewById(R.id.notification);
        acceleration = findViewById(R.id.acceleration);
        info = findViewById(R.id.info);

        /* Set values, parameters, etc. */
        setCounter();
        setSwitches();
        setInfo();
        setSnowFalling();

        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
    }

    /* Set methods */
    public void setCounter() {
        StringBuilder score =
                new StringBuilder(sharedPreferences.getString("score", ""));

        while (score.length() != 6)
            score.insert(0, "0");
        counter.setText(score);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSnowFalling() {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();

        if (month == 12 || month == 1) {
            SnowfallView snow = findViewById(R.id.snow);
            snow.setVisibility(View.VISIBLE);
        }
    }

    public void setInfo() {
        String s = "Version: " + BuildConfig.VERSION_NAME +
                "\n@merive-studio, " + Calendar.getInstance().get(YEAR);
        info.setText(s);
    }

    public void setSwitches() {
        vibrationState = sharedPreferences.getBoolean("vibration", false);
        vibration.setChecked(vibrationState);

        notificationState = sharedPreferences.getBoolean("notification", false);
        notification.setChecked(notificationState);
        if (notificationState) setAlarm(HOUR);

        accelerationState = sharedPreferences.getBoolean("acceleration", false);
        acceleration.setChecked(accelerationState);
    }

    /* Click methods */
    public void buttonClick(View view) {
        score = Integer.parseInt(String.valueOf(counter.getText()));
        if (score == 999999) {
            sharedPreferences.edit().putString("score", "000000").apply();
            counter.setText(R.string.counter);

            Intent intent = new Intent(this, FinishActivity.class);
            startActivity(intent);
        } else {
            score += 1;
            @SuppressLint("DefaultLocale") String result = String.format("%06d", score);

            sharedPreferences.edit().putString("score", result).apply();
            counter.setText(result);
        }
        vibrationTimes(Integer.parseInt(sharedPreferences.getString("score",
                "000000")));
    }

    public void vibrationTimes(int score) {
        if (vibrationState) {
            if (score % 100000 == 0) {
                vibration(3);
            } else if (score % 10000 == 0) {
                vibration(2);
            } else if (score % 1000 == 0) {
                vibration(1);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            try {
                String result = intent.getStringExtra("SCAN_RESULT");
                if (Integer.parseInt(result) < 1000000 && Integer.parseInt(result) > 0) {
                    sharedPreferences.edit().putString("score", result).apply();
                    setCounter();
                    Toast.makeText(this, "Score was updated.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickVibration(View view) {
        if (vibration.isChecked()) {
            sharedPreferences.edit().putBoolean("vibration", true).apply();
            vibrationState = true;
        } else {
            sharedPreferences.edit().putBoolean("vibration", false).apply();
            vibrationState = false;
        }
    }

    public void clickNotification(View view) {
        if (notification.isChecked()) {
            sharedPreferences.edit().putBoolean("notification", true).apply();
            notificationState = true;
            setAlarm(HOUR);
        } else {
            sharedPreferences.edit().putBoolean("notification", false).apply();
            notificationState = false;
            offAlarm();
        }
    }


    public void clickAcceleration(View view) {
        if (acceleration.isChecked()) {
            /* In accelerometer methods */
            setAccelerationState(true);
        } else {
            /* In accelerometer methods */
            setAccelerationState(false);
            setDefaultRotation(label);
            setDefaultRotation(counter);
            setDefaultRotation(button);
        }
    }

    public void clickReset() {
        FragmentManager fm = getSupportFragmentManager();
        ConfirmFragment confirmFragment = ConfirmFragment.newInstance();
        confirmFragment.show(fm, "confirm_fragment");
    }

    public void clickOptions(View view) {
        FragmentManager fm = getSupportFragmentManager();
        OptionsFragment optionsFragment = OptionsFragment.newInstance();
        optionsFragment.show(fm, "options_fragment");
    }

    public void clickScoreShare() {
        FragmentManager fm = getSupportFragmentManager();
        ScoreShareFragment scoreShareFragment = ScoreShareFragment.newInstance(sharedPreferences.getString("score", "0"));
        scoreShareFragment.show(fm, "score_share_fragment");
    }

    public void clickChangeIcon() {
        FragmentManager fm = getSupportFragmentManager();
        ChangeIconFragment changeIconFragment = ChangeIconFragment.newInstance();
        changeIconFragment.show(fm, "change_icon_fragment");
    }

    public void changeIcon(String icon) {
        sharedPreferences.edit().putString("icon", icon).apply();
    }

    public void resetCounter() {
        sharedPreferences.edit().putString("score", "000000").apply();
        counter.setText(R.string.counter);
    }

    public String getIcon() {
        return sharedPreferences.getString("icon", "default");
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
        if (sharedPreferences.getBoolean("acceleration", false)) {
            int sensorType = sensorEvent.sensor.getType();
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                axisData = sensorEvent.values.clone();

                runRotation(axisData[1], axisData[0], label);
                runRotation(axisData[1], axisData[0], counter);
                runRotation(axisData[1], axisData[0], button);
            }
        }
    }

    public void setAccelerationState(boolean state) {
        sharedPreferences.edit().putBoolean("acceleration", state).apply();
        accelerationState = state;
    }

    public void setDefaultRotation(View view) {
        runRotation(0, 0, view);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        /* Don't write */
    }

    /* Notification methods */
    public void setAlarm(int HOUR) {
        Intent intent = new Intent(MainActivity.this, NotificationsReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if (calendar.get(Calendar.HOUR_OF_DAY) >= HOUR) calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, HOUR);
        calendar.set(Calendar.MINUTE, MINUTE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void offAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, NotificationsReceiver.class);
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
            NotificationChannel channel =
                    new NotificationChannel("notifyPress1MTimes", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /* Vibration method */
    public void vibration(int times) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        for (int i = 0; i < times; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(250,
                        VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(250);
            }
        }
    }
}
