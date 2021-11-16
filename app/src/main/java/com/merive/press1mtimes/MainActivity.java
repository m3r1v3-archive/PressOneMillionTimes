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


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    static SharedPreferences sharedPreferences;

    static Boolean vibrationState, accelerationState, notificationState;
    TextView label, counter, info;
    ImageButton button;
    SwitchCompat vibration, notification, acceleration;

    SensorManager sensorManager;
    Sensor accelerometer;
    float[] axisData = new float[3];

    int HOUR = 12, MINUTE = 0;

    /**
     * This method is the start point at the MainActivity.
     *
     * @param savedInstanceState Used by super.onCreate method.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Press1MTimes);

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());

        initLayoutVariables();
        initSettings();

        setScoreToCounter();
        setSwitchValues();
        setInfo();

        setSnowFallingVisibility();

        setSensors();

        makeNotificationChannel();

        checkVersion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (accelerometer != null)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /**
     * This method is checking result of QR scanning.
     *
     * @param requestCode The code what was requested.
     * @param resultCode  The code what was returned.
     * @param intent      Intent object.
     */
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

    /**
     * This method is checking QR result on pattern.
     *
     * @param result QR result.
     * @see Pattern
     */
    private void checkPatternQR(String result) {
        Pattern pattern = Pattern.compile("P1MT:[(][0-9][0-9][0-9][)][(][0-9][0-9][0-9][)]");
        if (pattern.matcher(result).find()) setScoreByQRResult(result);
        else makeToast("QR-Code cannot be scanning");
    }

    /**
     * This method is setting score by QR result.
     *
     * @param result Score value from QR-Code.
     */
    private void setScoreByQRResult(String result) {
        setScoreToSharePreference(Integer.parseInt(result.replace("P1MT:", "").
                replace("(", "").replace(")", "")));
        setScoreToCounter();
        makeVibration(1);
        makeToast("Press1MTimes Score was updated");
    }

    /**
     * This method is setting new score value to sharedPreference.
     *
     * @param score Score value.
     * @see SharedPreferences
     */
    @SuppressLint("DefaultLocale")
    private void setScoreToSharePreference(int score) {
        sharedPreferences.edit().putString("score", String.format("%06d", score)).apply();
    }

    /**
     * This method is making Vibration.
     *
     * @param times Number of times.
     */
    public void makeVibration(int times) {
        if (vibrationState) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(150L * times);
        }
    }

    /**
     * This method is open ToastFragment.
     *
     * @param message Toast message.
     * @see ToastFragment
     */
    public void makeToast(String message) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.setReorderingAllowed(true);

        transaction.replace(R.id.toast_fragment, ToastFragment.newInstance(message), null);
        transaction.commit();
    }

    /**
     * This method is initializing main Layout Components.
     */
    private void initLayoutVariables() {
        counter = findViewById(R.id.counter);
        label = findViewById(R.id.label);
        button = findViewById(R.id.button);
    }

    /**
     * This method is initializing Settings Components.
     */
    private void initSettings() {
        vibration = findViewById(R.id.vibration);
        notification = findViewById(R.id.notification);
        acceleration = findViewById(R.id.acceleration);
        info = findViewById(R.id.info);
    }

    /**
     * This method is setting score to counter.
     */
    @SuppressLint("DefaultLocale")
    private void setScoreToCounter() {
        counter.setText(String.format("%06d", getScore()));
    }

    /**
     * This method is getting score from SharedPreference.
     *
     * @return Score Value.
     * @see SharedPreferences
     */
    private int getScore() {
        return Integer.parseInt(sharedPreferences.getString("score", "000000"));
    }

    /**
     * This method is setting switch values from SharedPreference.
     *
     * @see SharedPreferences
     */
    private void setSwitchValues() {
        vibrationState = sharedPreferences.getBoolean("vibration", false);
        vibration.setChecked(vibrationState);

        notificationState = sharedPreferences.getBoolean("notification", false);
        notification.setChecked(notificationState);

        accelerationState = sharedPreferences.getBoolean("acceleration", false);
        acceleration.setChecked(accelerationState);
    }

    /**
     * This method is setting info to settings.
     */
    private void setInfo() {
        info.setText(("Version: " + BuildConfig.VERSION_NAME +
                "\n@merive-studio, " + Calendar.getInstance().get(YEAR)));
    }

    /**
     * This method is setting visibility for Snow Falling effect if now is winter.
     *
     * @see com.jetradarmobile.snowfall.SnowfallView
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setSnowFallingVisibility() {
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (localDate.getMonthValue() == 12 || localDate.getMonthValue() == 1 || localDate.getMonthValue() == 2)
            findViewById(R.id.snow).setVisibility(View.VISIBLE);
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
     * This method is making notification channel.
     */
    private void makeNotificationChannel() {
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

    /**
     * This method is checking actual application version.
     * If P1MT have new version on website, this method will open UpdateFragment.
     *
     * @see UpdateFragment
     */
    private void checkVersion() {
        new Thread(() -> {
            try {
                if (!getActualVersion().equals(BuildConfig.VERSION_NAME))
                    openUpdateFragment(getActualVersion());
            } catch (IOException ignored) {
            }
        }).start();
    }

    /**
     * This method is getting actual application version on website.
     *
     * @return Actual application version.
     * @throws IOException ignored.
     * @see UpdateFragment
     */
    private String getActualVersion() throws IOException {
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

    /**
     * This method is opening UpdateFragment.
     *
     * @param actualVersion Actual application version.
     */
    private void openUpdateFragment(String actualVersion) {
        FragmentManager fm = getSupportFragmentManager();
        UpdateFragment updateFragment = UpdateFragment.newInstance(BuildConfig.VERSION_NAME, actualVersion);
        updateFragment.show(fm, "update_fragment");
    }

    /**
     * This method is executing after clicking on button.
     *
     * @param view View object.
     */
    public void clickButton(View view) {
        if (getScore() == 999999) {
            resetCounter();
            openFinish();
        } else setScoreToSharePreference(getScore() + 1);
        setScoreToCounter();
        setVibrationTimes(getScore());
    }

    /**
     * This method is resetting counter value to default value.
     */
    public void resetCounter() {
        setScoreToSharePreference(0);
        setScoreToCounter();
    }

    /**
     * This method is opening FinishActivity.
     */
    private void openFinish() {
        Intent intent = new Intent(this, FinishActivity.class);
        startActivity(intent);
    }

    /**
     * This method is setting number of vibration times by score.
     *
     * @param score Score value.
     */
    private void setVibrationTimes(int score) {
        if (score % 100000 == 0) makeVibration(3);
        else if (score % 10000 == 0) makeVibration(2);
        else if (score % 1000 == 0) makeVibration(1);
    }

    /**
     * This method is executing after clicking on vibration switch.
     * The method is setting vibration switch value to sharedPreference and vibrationState variable.
     *
     * @param view View object.
     */
    public void clickVibration(View view) {
        sharedPreferences.edit().putBoolean("vibration", vibration.isChecked()).apply();
        vibrationState = vibration.isChecked();
    }

    /**
     * This method is executing after clicking on notification switch.
     * The method is setting notification switch value to sharedPreference and notificationState variable.
     * If notification switch is true, will be unable alarm for notification.
     * Else will be disabled.
     *
     * @param view View object.
     */
    public void clickNotification(View view) {
        sharedPreferences.edit().putBoolean("notification", notification.isChecked()).apply();
        notificationState = notification.isChecked();
        if (notificationState) setAlarm();
        else offAlarm();
    }

    /**
     * This method is enabling alarm for notifications.
     */
    private void setAlarm() {
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

    /**
     * This method is disabling alarm for notifications.
     */
    private void offAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, NotificationsReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getService(MainActivity.this, 0, intent, 0);

        if (pendingIntent != null && alarmManager != null) alarmManager.cancel(pendingIntent);
    }

    /**
     * This method is executing after clicking on acceleration switch.
     * If acceleration switch value is false, will be setting default rotation for main components.
     *
     * @param view View object.
     */
    public void clickAcceleration(View view) {
        setAccelerationState(acceleration.isChecked());
        if (!acceleration.isChecked()) {
            setDefaultRotation(label);
            setDefaultRotation(counter);
            setDefaultRotation(button);
        }
    }

    /**
     * This method is setting accelerationState value.
     *
     * @param state State value.
     */
    private void setAccelerationState(boolean state) {
        sharedPreferences.edit().putBoolean("acceleration", state).apply();
        accelerationState = state;
    }

    /**
     * This method is setting default rotation for view.
     *
     * @param view View component.
     */
    private void setDefaultRotation(View view) {
        defineRotation(0, 0, view);
    }

    /**
     * This method is executing after clicking on Options Button.
     *
     * @param view View object.
     * @see android.widget.Button
     */
    public void clickOptions(View view) {
        makeVibration(1);
        FragmentManager fm = getSupportFragmentManager();
        OptionsFragment optionsFragment = OptionsFragment.newInstance();
        optionsFragment.show(fm, "options_fragment");
    }

    /**
     * This method is executing after clicking on Reset Button.
     *
     * @see android.widget.Button
     */
    public void clickReset() {
        makeVibration(1);
        FragmentManager fm = getSupportFragmentManager();
        ConfirmFragment confirmFragment = ConfirmFragment.newInstance();
        confirmFragment.show(fm, "confirm_fragment");
    }

    /**
     * This method is executing after clicking on ScoreShare Button.
     *
     * @see android.widget.Button
     */
    public void clickScoreShare() {
        makeVibration(1);
        FragmentManager fm = getSupportFragmentManager();
        ScoreShareFragment scoreShareFragment = ScoreShareFragment.newInstance(String.valueOf(getScore()));
        scoreShareFragment.show(fm, "score_share_fragment");
    }

    /**
     * This method is executing after clicking on ChangeIcon Button.
     *
     * @see android.widget.Button
     */
    public void clickChangeIcon() {
        makeVibration(1);
        FragmentManager fm = getSupportFragmentManager();
        ChangeIconFragment changeIconFragment = ChangeIconFragment.newInstance();
        changeIconFragment.show(fm, "change_icon_fragment");
    }

    /**
     * This method returns application icon name from sharedPreference.
     *
     * @return Application icon name.
     * @see SharedPreferences
     */
    public String getApplicationIcon() {
        return sharedPreferences.getString("icon", "default");
    }


    /**
     * This method is removing ToastFragment Fragment Layout.
     *
     * @see ToastFragment
     * @see android.widget.FrameLayout
     */
    public void removeToast() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.setReorderingAllowed(true);

        transaction.remove(fragmentManager.findFragmentById(R.id.toast_fragment));
        transaction.commit();
    }

    /**
     * This method is changing application icon value in sharedPreference.
     *
     * @param icon Icon name.
     */
    public void changeIcon(String icon) {
        sharedPreferences.edit().putString("icon", icon).apply();
    }
}
