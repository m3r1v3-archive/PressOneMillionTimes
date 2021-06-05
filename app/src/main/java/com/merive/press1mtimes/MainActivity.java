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
import com.merive.press1mtimes.fragments.ChangeIconFragment;
import com.merive.press1mtimes.fragments.ConfirmFragment;
import com.merive.press1mtimes.fragments.OptionsFragment;
import com.merive.press1mtimes.fragments.ScoreShareFragment;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static com.merive.press1mtimes.utils.Rotation.defineRotation;
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
        setSensors();
    }

    /* Set methods */
    @SuppressLint("DefaultLocale")
    public void setCounter() {
        /* Set score to counter */
        counter.setText(String.format("%06d", getScore()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSnowFalling() {
        /* Set visibility for snow if it is winter */
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (localDate.getMonthValue() == 12 || localDate.getMonthValue() == 1)
            findViewById(R.id.snow).setVisibility(View.VISIBLE);
    }

    public void setInfo() {
        /* Set info to Settings */
        String s = "Version: " + BuildConfig.VERSION_NAME +
                "\n@merive-studio, " + Calendar.getInstance().get(YEAR);
        info.setText(s);
    }

    public void setSwitches() {
        /* Set switches to Settings */
        vibrationState = sharedPreferences.getBoolean("vibration", false);
        vibration.setChecked(vibrationState);

        notificationState = sharedPreferences.getBoolean("notification", false);
        notification.setChecked(notificationState);
        if (notificationState) setAlarm(HOUR);

        accelerationState = sharedPreferences.getBoolean("acceleration", false);
        acceleration.setChecked(accelerationState);
    }

    public void setSensors() {
        /* Set sensors variables */
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
    }

    public void setAccelerationState(boolean state) {
        /* Update Acceleration state in SharedPreferences */
        sharedPreferences.edit().putBoolean("acceleration", state).apply();
        accelerationState = state;
    }

    /* Click methods */
    public void buttonClick(View view) {
        /* OnClick Button */
        if (getScore() == 999999) {
            updateScore(0);
            startEaster();
        } else updateScore(getScore() + 1);
        counter.setText(String.valueOf(getScore()));
        vibrationTimes(getScore());
    }

    public void clickVibration(View view) {
        /* OnClick Vibration Switch */
        if (vibration.isChecked()) {
            sharedPreferences.edit().putBoolean("vibration", true).apply();
            vibrationState = true;
        } else {
            sharedPreferences.edit().putBoolean("vibration", false).apply();
            vibrationState = false;
        }
    }

    public void clickNotification(View view) {
        /* OnClick Notifications Switch */
        sharedPreferences.edit().putBoolean("notification", notification.isChecked()).apply();
        notificationState = notification.isChecked();
        if (notification.isChecked()) setAlarm(HOUR);
        else offAlarm();
    }


    public void clickAcceleration(View view) {
        /* onClick Acceleration in Settings */
        if (acceleration.isChecked()) setAccelerationState(true);
        else {
            setAccelerationState(false);
            setDefaultRotation(label);
            setDefaultRotation(counter);
            setDefaultRotation(button);
        }
    }

    public void clickOptions(View view) {
        /* OnClick Options in Settings */
        FragmentManager fm = getSupportFragmentManager();
        OptionsFragment optionsFragment = OptionsFragment.newInstance();
        optionsFragment.show(fm, "options_fragment");
    }

    public void clickReset() {
        /* OnClick Reset in OptionsFragment */
        FragmentManager fm = getSupportFragmentManager();
        ConfirmFragment confirmFragment = ConfirmFragment.newInstance();
        confirmFragment.show(fm, "confirm_fragment");
    }

    public void clickScoreShare() {
        /* OnClick ScoreShare in OptionsFragment */
        FragmentManager fm = getSupportFragmentManager();
        ScoreShareFragment scoreShareFragment = ScoreShareFragment.newInstance(sharedPreferences.getString("score", "0"));
        scoreShareFragment.show(fm, "score_share_fragment");
    }

    public void clickChangeIcon() {
        /* OnClick Change Icon in OptionsFragment */
        FragmentManager fm = getSupportFragmentManager();
        ChangeIconFragment changeIconFragment = ChangeIconFragment.newInstance();
        changeIconFragment.show(fm, "change_icon_fragment");
    }

    @SuppressLint("DefaultLocale")
    public void updateScore(int score) {
        /* Update score in shared preference */
        sharedPreferences.edit().putString("score", String.format("%06d", score)).apply();

    }

    /* Get methods */
    public int getScore() {
        /* Return Integer Score */
        return Integer.parseInt(sharedPreferences.getString("score", "000000"));
    }

    public String getIcon() {
        /* Get Current Icon of Application */
        return sharedPreferences.getString("icon", "default");
    }


    /* Another methods */
    public void startEaster() {
        /* Start EasterEgg Activity */
        Intent intent = new Intent(this, FinishActivity.class);
        startActivity(intent);
    }

    public void vibrationTimes(int score) {
        /* Make vibration by score */
        if (vibrationState) {
            if (score % 100000 == 0) makeVibration(3);
            else if (score % 10000 == 0) makeVibration(2);
            else if (score % 1000 == 0) makeVibration(1);
        }
    }

    public void changeIcon(String icon) {
        /* Change Current Icon of Application in SharedPreferences */
        sharedPreferences.edit().putString("icon", icon).apply();
    }

    public void resetCounter() {
        /* Reset Counter to default */
        updateScore(0);
        counter.setText(getScore());
    }

    public void makeVibration(int times) {
        /* Make vibrations on device */
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        for (int i = 0; i < times; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                v.vibrate(VibrationEffect.createOneShot(250,
                        VibrationEffect.DEFAULT_AMPLITUDE));
            else v.vibrate(250);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            try {
                String result = intent.getStringExtra("SCAN_RESULT");
                if (Integer.parseInt(result) < 1000000 && Integer.parseInt(result) > 0) {
                    updateScore(Integer.parseInt(result));
                    setCounter();
                    Toast.makeText(this, "Score was updated.", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            } catch (Exception exc) {
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }
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

                defineRotation(axisData[1], axisData[0], label);
                defineRotation(axisData[1], axisData[0], counter);
                defineRotation(axisData[1], axisData[0], button);
            }
        }
    }

    public void setDefaultRotation(View view) {
        /* Set Default Rotation for view */
        defineRotation(0, 0, view);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        /* Don't write */
    }

    /* Notification methods */
    public void setAlarm(int HOUR) {
        /* Turn on Notification Alarm */
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
        /* Turn off Notification Alarm */
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
}
