package com.merive.press1mtimes;

import static com.merive.press1mtimes.utils.Rotation.defineRotation;
import static java.util.Calendar.YEAR;

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
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.merive.press1mtimes.fragments.ChangeIconFragment;
import com.merive.press1mtimes.fragments.ConfirmFragment;
import com.merive.press1mtimes.fragments.OptionsFragment;
import com.merive.press1mtimes.fragments.ScoreShareFragment;
import com.merive.press1mtimes.fragments.ToastFragment;
import com.merive.press1mtimes.fragments.UpdateFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    static SharedPreferences sharedPreferences;

    static Boolean vibrationState, accelerationState, notificationState;
    TextView label, counter, info;
    ImageButton button;
    SwitchCompat vibration, notification, acceleration;

    SensorManager sensorManager;
    Sensor accelerometer;
    float[] axisData = new float[3];

    int HOUR = 12, MINUTE = 0;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Press1MTimes);

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_main);

        makeNotificationChannel();

        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());

        initLayoutVariables();
        initSettings();

        setScoreToCounter();
        setSwitchesToSettings();
        setInfo();
        setSnowFallingVisibility();
        setSensors();

        checkVersion();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            try {
                checkPatternQR(intent.getStringExtra("SCAN_RESULT"));
            } catch (Exception exc) {
                makeToast("Something went wrong");
            }
        }
    }

    public void initLayoutVariables() {
        counter = findViewById(R.id.counter);
        label = findViewById(R.id.label);
        button = findViewById(R.id.button);
    }

    public void initSettings() {
        vibration = findViewById(R.id.vibration);
        notification = findViewById(R.id.notification);
        acceleration = findViewById(R.id.acceleration);
        info = findViewById(R.id.info);
    }

    @SuppressLint("DefaultLocale")
    public void setScoreToCounter() {
        counter.setText(String.format("%06d", getScore()));
    }

    public void setSwitchesToSettings() {
        vibrationState = sharedPreferences.getBoolean("vibration", false);
        vibration.setChecked(vibrationState);

        notificationState = sharedPreferences.getBoolean("notification", false);
        notification.setChecked(notificationState);

        accelerationState = sharedPreferences.getBoolean("acceleration", false);
        acceleration.setChecked(accelerationState);
    }

    public void setInfo() {
        info.setText(("Version: " + BuildConfig.VERSION_NAME +
                "\n@merive-studio, " + Calendar.getInstance().get(YEAR)));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSnowFallingVisibility() {
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (localDate.getMonthValue() == 12 || localDate.getMonthValue() == 1 || localDate.getMonthValue() == 2)
            findViewById(R.id.snow).setVisibility(View.VISIBLE);
    }

    public void setSensors() {
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
    }

    @SuppressLint("DefaultLocale")
    public void setScoreToSharePreference(int score) {
        sharedPreferences.edit().putString("score", String.format("%06d", score)).apply();
    }

    public void setAccelerationState(boolean state) {
        sharedPreferences.edit().putBoolean("acceleration", state).apply();
        accelerationState = state;
    }

    public void setVibrationTimes(int score) {
        if (score % 100000 == 0) makeVibration(3);
        else if (score % 10000 == 0) makeVibration(2);
        else if (score % 1000 == 0) makeVibration(1);
    }

    public void setScoreByQRResult(String result) {
        setScoreToSharePreference(Integer.parseInt(result.replace("P1MT:", "").
                replace("(", "").replace(")", "")));
        setScoreToCounter();
        makeVibration(1);
        makeToast("Press1MTimes Score was updated");
    }

    public void checkVersion() {
        Thread thread = new Thread(() -> {
            try {
                if (!getActualVersion().equals(BuildConfig.VERSION_NAME))
                    openUpdateFragment(BuildConfig.VERSION_NAME, getActualVersion());
            } catch (Exception ignored) {
            }
        });

        thread.start();
    }

    public void checkPatternQR(String result) {
        Pattern pattern = Pattern.compile("P1MT:[(][0-9][0-9][0-9][)][(][0-9][0-9][0-9][)]");
        if (pattern.matcher(result).find())
            setScoreByQRResult(result);
        else makeToast("Something went wrong");
    }

    public void clickButton(View view) {
        if (getScore() == 999999) {
            resetCounter();
            openFinish();
        } else setScoreToSharePreference(getScore() + 1);
        setScoreToCounter();
        setVibrationTimes(getScore());
    }

    public void clickVibration(View view) {
        sharedPreferences.edit().putBoolean("vibration", vibration.isChecked()).apply();
        vibrationState = vibration.isChecked();
    }

    public void clickNotification(View view) {
        sharedPreferences.edit().putBoolean("notification", notification.isChecked()).apply();
        notificationState = notification.isChecked();
        if (notificationState) setAlarm();
        else offAlarm();
    }

    public void clickAcceleration(View view) {
        setAccelerationState(acceleration.isChecked());
        if (!acceleration.isChecked()) {
            setDefaultRotation(label);
            setDefaultRotation(counter);
            setDefaultRotation(button);
        }
    }

    public void clickOptions(View view) {
        makeVibration(1);
        FragmentManager fm = getSupportFragmentManager();
        OptionsFragment optionsFragment = OptionsFragment.newInstance();
        optionsFragment.show(fm, "options_fragment");
    }

    public void clickReset() {
        makeVibration(1);
        FragmentManager fm = getSupportFragmentManager();
        ConfirmFragment confirmFragment = ConfirmFragment.newInstance();
        confirmFragment.show(fm, "confirm_fragment");
    }

    public void clickScoreShare() {
        makeVibration(1);
        FragmentManager fm = getSupportFragmentManager();
        ScoreShareFragment scoreShareFragment = ScoreShareFragment.newInstance(String.valueOf(getScore()));
        scoreShareFragment.show(fm, "score_share_fragment");
    }

    public void clickChangeIcon() {
        makeVibration(1);
        FragmentManager fm = getSupportFragmentManager();
        ChangeIconFragment changeIconFragment = ChangeIconFragment.newInstance();
        changeIconFragment.show(fm, "change_icon_fragment");
    }

    public void openUpdateFragment(String oldVersion, String newVersion) {
        FragmentManager fm = getSupportFragmentManager();
        UpdateFragment updateFragment = UpdateFragment.newInstance(oldVersion, newVersion);
        updateFragment.show(fm, "update_fragment");
    }

    public void openFinish() {
        Intent intent = new Intent(this, FinishActivity.class);
        startActivity(intent);
    }

    public int getScore() {
        return Integer.parseInt(sharedPreferences.getString("score", "000000"));
    }

    public String getApplicationIcon() {
        return sharedPreferences.getString("icon", "default");
    }

    public String getActualVersion() throws IOException {
        URL url = new URL(getResources().getString(R.string.link));
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            for (String line; (line = reader.readLine()) != null; ) builder.append(line.trim());
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException ignored) {
            }
        }
        return builder.substring(builder.indexOf("<i>") + "<i>".length()).substring(1, builder.substring(builder.indexOf("<i>") + "<i>".length()).indexOf("</i>"));
    }

    public void makeVibration(int times) {
        if (vibrationState) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(150L * times);
        }
    }

    public void makeToast(String message) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.setReorderingAllowed(true);

        transaction.replace(R.id.toast_fragment, ToastFragment.newInstance(message), null);
        transaction.commit();
    }

    public void removeToast() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.setReorderingAllowed(true);

        transaction.remove(fragmentManager.findFragmentById(R.id.toast_fragment));
        transaction.commit();
    }

    public void makeNotificationChannel() {
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

    public void changeIcon(String icon) {
        sharedPreferences.edit().putString("icon", icon).apply();
    }

    public void resetCounter() {
        setScoreToSharePreference(0);
        setScoreToCounter();
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
        defineRotation(0, 0, view);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void setAlarm() {
        Intent intent = new Intent(getBaseContext(), NotificationsReceiver.class);
        intent.putExtra("score", String.valueOf(getScore()));

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, HOUR);
        calendar.set(Calendar.MINUTE, MINUTE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
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
}
