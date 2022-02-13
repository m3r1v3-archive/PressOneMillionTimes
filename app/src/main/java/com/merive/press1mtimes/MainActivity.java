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
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.merive.press1mtimes.fragments.ChangeIconFragment;
import com.merive.press1mtimes.fragments.ResetFragment;
import com.merive.press1mtimes.fragments.OptionsFragment;
import com.merive.press1mtimes.fragments.ScoreShareFragment;
import com.merive.press1mtimes.fragments.SettingsFragment;
import com.merive.press1mtimes.fragments.SplashPositionFragment;
import com.merive.press1mtimes.fragments.ToastFragment;
import com.merive.press1mtimes.fragments.UpdateFragment;
import com.merive.press1mtimes.utils.SplashTexts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static LinkedList<String> toastMessages = new LinkedList<>();

    static SharedPreferences sharedPreferences;

    static Boolean vibrationState, accelerationState, notificationState, splashState;
    TextView label, counter, splash;
    ImageButton button;


    SensorManager sensorManager;
    Sensor accelerometer;
    float[] axisData = new float[3];

    int HOUR = 12, MINUTE = 0;

    /**
     * This method is the start point at the MainActivity.
     *
     * @param savedInstanceState Use by super.onCreate method.
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

        initSettingsFragment();

        setScoreToCounter();
        setStateValues();

        setSnowFallingVisibility();

        setSensors();

        createNotificationChannel();

        checkVersion();
        checkSplashState();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (notificationState) setAlarm();
    }

    /**
     * This overridden method registers accelerator changes.
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
     * This method checks result of QR scanning.
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
                makeToast(getResources().getString(R.string.error));
            }
        }
    }

    /**
     * This method checks QR result on pattern.
     *
     * @param result QR result.
     * @see Pattern
     */
    private void checkPatternQR(String result) {
        Pattern pattern = Pattern.compile("P1MT:[(][0-9][0-9][0-9][)][(][0-9][0-9][0-9][)]");
        if (pattern.matcher(result).find()) setScoreByQRResult(result);
        else makeToast(getResources().getString(R.string.qr_error));
    }

    /**
     * This method sets score by QR result.
     *
     * @param result Score value from QR-Code.
     */
    private void setScoreByQRResult(String result) {
        setScoreToSharePreference(Integer.parseInt(result.replace("P1MT:", "").
                replace("(", "").replace(")", "")));
        setScoreToCounter();
        makeVibration(1);
        makeToast(getResources().getString(R.string.score_updated));
    }

    /**
     * This method sets new score value to sharedPreference.
     *
     * @param score Score value.
     * @see SharedPreferences
     */
    @SuppressLint("DefaultLocale")
    private void setScoreToSharePreference(int score) {
        sharedPreferences.edit().putString("score", String.format("%06d", score)).apply();
    }

    /**
     * This method makes vibration.
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
     * This method opens ToastFragment.
     *
     * @param message Toast message.
     * @see ToastFragment
     */
    public void makeToast(String message) {
        MainActivity.toastMessages.add(message);
        if (MainActivity.toastMessages.size() == 1) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.setReorderingAllowed(true);
            transaction.replace(R.id.toast_fragment, new ToastFragment(), null);
            transaction.commit();
        }
    }

    /**
     * This method initializes main layout components.
     */
    private void initLayoutVariables() {
        counter = findViewById(R.id.counter);
        label = findViewById(R.id.title);
        button = findViewById(R.id.button);
    }

    /**
     * This method sets SettingsFragment.
     */
    private void initSettingsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.settings_fragment, new SettingsFragment(), null);
        transaction.commit();
    }

    /**
     * This method sets score to counter.
     */
    @SuppressLint("DefaultLocale")
    private void setScoreToCounter() {
        counter.setText(String.format("%06d", getScore()));
    }

    /**
     * This method gets score from SharedPreference.
     *
     * @return Score Value.
     * @see SharedPreferences
     */
    private int getScore() {
        return Integer.parseInt(sharedPreferences.getString("score", "000000"));
    }

    /**
     * This method returns Vibration state value from sharedPreference.
     *
     * @see SharedPreferences
     */
    public boolean getVibrationState() {
        return sharedPreferences.getBoolean("vibration", false);
    }

    /**
     * This method returns Notification state value from sharedPreference.
     *
     * @see SharedPreferences
     */
    public boolean getNotificationState() {
        return notificationState = sharedPreferences.getBoolean("notification", false);
    }

    /**
     * This method returns Acceleration state value from sharedPreference.
     *
     * @see SharedPreferences
     */
    public boolean getAccelerationState() {
        return sharedPreferences.getBoolean("acceleration", false);
    }

    /**
     * This method returns Splash state value from sharedPreference.
     *
     * @see SharedPreferences
     */
    public boolean getSplashState() {
        return sharedPreferences.getBoolean("splash", false);
    }

    /**
     * This method sets state values to variables.
     */
    private void setStateValues() {
        vibrationState = getVibrationState();
        notificationState = getNotificationState();
        accelerationState = getAccelerationState();
        splashState = getSplashState();
    }

    /**
     * This method sets info to settings.
     */
    public String getInfo() {
        return (("P1MT / " + BuildConfig.VERSION_NAME + "\nmerive-studios / MIT License, " + Calendar.getInstance().get(YEAR)));
    }

    /**
     * This method sets visibility for Snow Falling effect if now is winter.
     *
     * @see com.jetradarmobile.snowfall.SnowfallView
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setSnowFallingVisibility() {
        if (checkWinter()) findViewById(R.id.coins).setVisibility(View.VISIBLE);
    }

    /**
     * This method checks season on winter.
     *
     * @return True if season is winter.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkWinter() {
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue() == 12 || localDate.getMonthValue() == 1 || localDate.getMonthValue() == 2;
    }

    /**
     * This method sets sensors that using by application.
     */
    private void setSensors() {
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * This method creates notification channel.
     */
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

    /**
     * This method checks actual application version.
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
     * This method gets actual application version on website.
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
     * This method opens UpdateFragment.
     *
     * @param actualVersion Actual application version.
     */
    private void openUpdateFragment(String actualVersion) {
        FragmentManager fm = getSupportFragmentManager();
        UpdateFragment updateFragment = UpdateFragment.newInstance(BuildConfig.VERSION_NAME, actualVersion);
        updateFragment.show(fm, "update_fragment");
    }

    /**
     * This method checks splashState value and makes Splash Message.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkSplashState() {
        if (splashState) {
            splash = findViewById(R.id.splash);
            setSplashPosition();
            setSplashAnimation();
        }
    }

    /**
     * This method sets Splash Message position.
     */
    private void setSplashPosition() {
        ConstraintSet constraintSet = new ConstraintSet();
        ConstraintLayout constraintLayout = findViewById(R.id.activity_main);
        constraintSet.clone(constraintLayout);
        constraintSet.setHorizontalBias(splash.getId(), sharedPreferences.getFloat("splash_horizontal", (float) 0.98));
        constraintSet.setVerticalBias(splash.getId(), sharedPreferences.getFloat("splash_vertical", (float) 0.98));
        constraintSet.applyTo(constraintLayout);
    }

    /**
     * This method sets Splash Message animation.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setSplashAnimation() {
        splash.setText(("#" + SplashTexts.values()[new Random().nextInt(SplashTexts.values().length - (checkWinter() ? 0 : 1))]));
        splash.setVisibility(View.VISIBLE);
        splash.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash));
    }

    /**
     * This method executes after click on button.
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
     * This method resets counter value to default value.
     */
    public void resetCounter() {
        setScoreToSharePreference(0);
        setScoreToCounter();
    }

    /**
     * This method opens FinishActivity.
     */
    private void openFinish() {
        startActivity(new Intent(this, FinishActivity.class));
        finish();
    }

    /**
     * This method sets number of vibration times by score.
     *
     * @param score Score value.
     */
    private void setVibrationTimes(int score) {
        if (score % 100000 == 0) makeVibration(3);
        else if (score % 10000 == 0) makeVibration(2);
        else if (score % 1000 == 0) makeVibration(1);
    }

    /**
     * This method executes after click on vibration switch.
     * The method sets vibration switch value to sharedPreference and vibrationState variable.
     *
     * @param value Switch value.
     */
    public void clickVibration(boolean value) {
        sharedPreferences.edit().putBoolean("vibration", value).apply();
        vibrationState = value;
    }

    /**
     * This method is executes after click on notification switch.
     * The method sets notification switch value to sharedPreference and notificationState variable.
     * If notification switch is true, will be unable alarm for notification.
     * Else will be disabled.
     *
     * @param value Switch value.
     */
    public void clickNotification(boolean value) {
        sharedPreferences.edit().putBoolean("notification", value).apply();
        notificationState = value;
        if (notificationState) setAlarm();
        else offAlarm();
    }

    /**
     * This method enables alarm for notifications.
     */
    private void setAlarm() {
        Intent intent = new Intent(getBaseContext(), NotificationsReceiver.class);
        intent.putExtra("score", String.valueOf(getScore()));

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if (calendar.get(Calendar.HOUR_OF_DAY) >= HOUR) calendar.add(Calendar.DATE, 1);

        calendar.set(Calendar.HOUR_OF_DAY, HOUR);
        calendar.set(Calendar.MINUTE, MINUTE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * This method disables alarm for notifications.
     */
    private void offAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, NotificationsReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getService(MainActivity.this, 0, intent, 0);

        if (pendingIntent != null && alarmManager != null) alarmManager.cancel(pendingIntent);
    }

    /**
     * This method executes after click on acceleration switch.
     * If acceleration switch value is false, will set default rotation for main components.
     *
     * @param value Switch value.
     */
    public void clickAcceleration(boolean value) {
        sharedPreferences.edit().putBoolean("acceleration", value).apply();
        accelerationState = value;
        if (!value) {
            setDefaultRotation(label);
            setDefaultRotation(counter);
            setDefaultRotation(button);
        }
    }

    /**
     * This method sets default rotation for view.
     *
     * @param view View component.
     */
    private void setDefaultRotation(View view) {
        defineRotation(0, 0, view);
    }

    public void clickSplash(boolean value) {
        sharedPreferences.edit().putBoolean("splash", value).apply();
        splashState = value;
    }

    /**
     * This method executes after click on Options Button.
     *
     * @see android.widget.Button
     */
    public void clickOptions() {
        makeVibration(1);
        FragmentManager fm = getSupportFragmentManager();
        OptionsFragment optionsFragment = OptionsFragment.newInstance();
        optionsFragment.show(fm, "options_fragment");
    }

    /**
     * This method executes after click on Reset Button.
     *
     * @see android.widget.Button
     */
    public void clickReset() {
        FragmentManager fm = getSupportFragmentManager();
        ResetFragment confirmFragment = ResetFragment.newInstance();
        confirmFragment.show(fm, "confirm_fragment");
    }

    /**
     * This method executes after click on ScoreShare Button.
     *
     * @see android.widget.Button
     */
    public void clickScoreShare() {
        FragmentManager fm = getSupportFragmentManager();
        ScoreShareFragment scoreShareFragment = ScoreShareFragment.newInstance(String.valueOf(getScore()));
        scoreShareFragment.show(fm, "score_share_fragment");
    }

    /**
     * This method executes after click on ChangeIcon Button.
     *
     * @see android.widget.Button
     */
    public void clickChangeIcon() {
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
     * This method executes after click on Splash Position Option button.
     * The method opens SplashPosition Fragment.
     *
     * @see android.widget.Button
     * @see SplashPositionFragment
     */
    public void clickSplashPosition() {
        FragmentManager fm = getSupportFragmentManager();
        SplashPositionFragment splashPositionFragment = SplashPositionFragment.newInstance();
        splashPositionFragment.show(fm, "splash_position_fragment");
    }

    /**
     * This method sets Splash Position in sharedPreferences.
     *
     * @param horizontal Horizontal float value.
     * @param vertical Vertical float value.
     * @see SharedPreferences
     */
    public void setSplashPosition(float horizontal, float vertical) {
        sharedPreferences.edit().putFloat("splash_horizontal", horizontal).putFloat("splash_vertical", vertical).apply();
    }

    /**
     * This method removes ToastFragment from screen.
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
     * This method changes application icon value in sharedPreference.
     *
     * @param icon Icon name.
     */
    public void changeIcon(String icon) {
        sharedPreferences.edit().putString("icon", icon).apply();
    }
}
