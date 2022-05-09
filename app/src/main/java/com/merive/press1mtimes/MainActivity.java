package com.merive.press1mtimes;

import static java.util.Calendar.YEAR;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.merive.press1mtimes.fragments.IconsFragment;
import com.merive.press1mtimes.fragments.OptionsFragment;
import com.merive.press1mtimes.fragments.ResetFragment;
import com.merive.press1mtimes.fragments.ScoreShareFragment;
import com.merive.press1mtimes.fragments.SettingsFragment;
import com.merive.press1mtimes.fragments.SplashMessageFragment;
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


public class MainActivity extends AppCompatActivity {

    public static LinkedList<String> toastMessages = new LinkedList<>();

    static SharedPreferences sharedPreferences;

    static Boolean vibrationState, animationState, notificationState, splashState;
    TextView titleText, counterText, splashText;
    ImageButton pressButton;


    int HOUR = 12, MINUTE = 0;

    /**
     * Called by the system when the service is first created
     *
     * @param savedInstanceState Using by super.onCreate method
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Press1MTimes);

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.breath_in, R.anim.breath_out);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());

        initLayoutVariables();
        initSettingsFragment();

        setScoreToCounter();
        setStateValues();

        createNotificationChannel();

        checkVersion();
        checkSplashState();

        try {
            checkPatternQR(getIntent().getData().toString());
        } catch (NullPointerException ignored) {
        }
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed
     * Needs for setting notification alarm when application is closing
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (notificationState) setAlarm();
    }

    /**
     * Executes when QR Code was scanned and was opened MainActivity
     * Checks QR Code result pattern
     *
     * @param requestCode The code what was requested
     * @param resultCode  The code what was returned
     * @param intent      Intent object
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
     * Checks QR Code result pattern
     * If result corresponds to the pattern, executes setScoreByQRResult method
     *
     * @param result QR result.
     * @see Pattern
     */
    private void checkPatternQR(String result) {
        Pattern pattern = Pattern.compile("press1mtimes://[0-9A-F]{1,6}");
        if (pattern.matcher(result).find()) setScoreByQRResult(result);
        else makeToast(getResources().getString(R.string.qr_error));
    }

    /**
     * Sets score by QR Code Result value
     *
     * @param result Score value from QR Code result
     */
    private void setScoreByQRResult(String result) {
        setScoreToSharedPreference(Integer.parseInt(result.replace("press1mtimes://", ""), 16));
        setScoreToCounter();
        makeVibration(1);
        makeToast(getResources().getString(R.string.score_updated));
    }

    /**
     * Sets score value to SharedPreferences memory
     *
     * @param score Score value
     * @see SharedPreferences
     */
    @SuppressLint("DefaultLocale")
    private void setScoreToSharedPreference(int score) {
        sharedPreferences.edit().putString("score", String.format("%06d", score)).apply();
    }

    /**
     * Makes vibration on device
     *
     * @param times Vibration times (Using for make vibration longer)
     */
    public void makeVibration(int times) {
        if (vibrationState)
            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(75L * times);
    }

    /**
     * Makes ToastFragment
     *
     * @param message Toast message value
     * @see ToastFragment
     */
    public void makeToast(String message) {
        MainActivity.toastMessages.add(message);
        if (MainActivity.toastMessages.size() == 1) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.breath_in, R.anim.breath_out);
            transaction.setReorderingAllowed(true);
            transaction.replace(R.id.toast_fragment, new ToastFragment(), null);
            transaction.commit();
        }
    }

    /**
     * Initializes basic layout components
     */
    private void initLayoutVariables() {
        titleText = findViewById(R.id.main_title);
        counterText = findViewById(R.id.counter);
        pressButton = findViewById(R.id.button);
    }

    /**
     * Initializes SettingFragment
     */
    public void initSettingsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.setCustomAnimations(R.anim.breath_in, R.anim.breath_out);
        transaction.replace(R.id.settings_fragment, new SettingsFragment(), null);
        transaction.commit();
    }

    /**
     * Sets score value to counterText component
     */
    @SuppressLint("DefaultLocale")
    private void setScoreToCounter() {
        counterText.setText(String.format("%06d", getScore()));
    }

    /**
     * Gets score value from SharedPreference.
     *
     * @return Score value (default value is 000000)
     * @see SharedPreferences
     */
    public int getScore() {
        return Integer.parseInt(sharedPreferences.getString("score", "000000"));
    }

    /**
     * Returns vibration setting value from SharedPreferences memory (default value is false)
     *
     * @see SharedPreferences
     */
    public boolean getVibrationState() {
        return sharedPreferences.getBoolean("vibration", false);
    }

    /**
     * Returns notification setting value from SharedPreferences memory (default value is false)
     *
     * @see SharedPreferences
     */
    public boolean getNotificationState() {
        return notificationState = sharedPreferences.getBoolean("notification", false);
    }

    /**
     * Returns animation setting value from SharedPreferences memory (default value is false)
     *
     * @see SharedPreferences
     */
    public boolean getAnimationState() {
        return sharedPreferences.getBoolean("animation", false);
    }

    /**
     * Returns splash setting value from SharedPreferences memory (default value is false)
     *
     * @see SharedPreferences
     */
    public boolean getSplashState() {
        return sharedPreferences.getBoolean("splash", false);
    }

    /**
     * Sets settings values to state variables
     */
    private void setStateValues() {
        vibrationState = getVibrationState();
        notificationState = getNotificationState();
        animationState = getAnimationState();
        splashState = getSplashState();
    }

    /**
     * Returns project info string
     */
    public String getInfo() {
        return (("P1MT / " + BuildConfig.VERSION_NAME + "\nmerive_ inc. / MIT License, " + Calendar.getInstance().get(YEAR)));
    }

    /**
     * Checks season on winter (needs for exclusive SplashMessage)
     *
     * @return Return true if season is winter
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkWinter() {
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue() == 12 || localDate.getMonthValue() == 1 || localDate.getMonthValue() == 2;
    }

    /**
     * Creates notification channel (needs for notifications)
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
     * Checks installed version and compares it with actual version on website
     * If Press1MTimes have new version on website, will have opened UpdateFragment
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
     * Gets actual Press1MTimes version on official websites
     *
     * @return Actual application version string value
     * @throws IOException ignored
     * @see UpdateFragment
     */
    private String getActualVersion() throws IOException {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(new URL(getResources().getString(R.string.link)).openStream(), StandardCharsets.UTF_8));
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
     * Open UpdateFragment
     *
     * @param actualVersion Actual application version value
     */
    private void openUpdateFragment(String actualVersion) {
        UpdateFragment.newInstance(BuildConfig.VERSION_NAME, actualVersion).show(getSupportFragmentManager(), "update_fragment");
    }

    /**
     * Checks splashState value
     * If value is true, sets splash position and starts splash animation
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkSplashState() {
        if (splashState) {
            splashText = findViewById(R.id.splash);
            setSplashPosition();
            makeSplashAnimation();
        }
    }

    /**
     * Sets SplashMessage position (default 0.98f for horizontal and vertical)
     */
    private void setSplashPosition() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone((ConstraintLayout) findViewById(R.id.activity_main));
        constraintSet.setHorizontalBias(splashText.getId(), sharedPreferences.getFloat("splash_horizontal", 0.98f));
        constraintSet.setVerticalBias(splashText.getId(), sharedPreferences.getFloat("splash_vertical", 0.98f));
        constraintSet.applyTo(findViewById(R.id.activity_main));
    }

    /**
     * Makes SplashMessage animation, sets TextView value and makes it visible
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void makeSplashAnimation() {
        splashText.setText(("#" + SplashTexts.values()[new Random().nextInt(SplashTexts.values().length - (checkWinter() ? 0 : 1))]));
        splashText.setVisibility(View.VISIBLE);
        splashText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
    }

    /**
     * Executes after click on pressButton
     * It checks score value on maximum result, increase score value and make vibration effect for round score values
     * Also if make BreathAnimation for basic components if animationState is true
     *
     * @param view View object
     */
    public void clickButton(View view) {
        if (getScore() == 999999) {
            resetCounter();
            openFinish();
        } else setScoreToSharedPreference(getScore() + 1);
        setScoreToCounter();
        makeVibrationByScore(getScore());
        if (animationState) makeBreathAnimation(titleText, counterText, pressButton);
    }

    /**
     * Resets score value to default value (default score value is 000000)
     * Updates score value in SharedPreferences memory and sets default value to counter
     */
    public void resetCounter() {
        setScoreToSharedPreference(0);
        setScoreToCounter();
    }

    /**
     * Opens FinishActivity
     */
    private void openFinish() {
        startActivity(new Intent(this, FinishActivity.class));
        finish();
    }

    /**
     * Sets number of vibration times by round value of score.
     *
     * @param score Score value
     */
    private void makeVibrationByScore(int score) {
        if (score % 100000 == 0) makeVibration(3);
        else if (score % 10000 == 0) makeVibration(2);
        else if (score % 1000 == 0) makeVibration(1);
    }

    /**
     * Makes BreathAnimation for views
     *
     * @param views Views what will be animated
     */
    private void makeBreathAnimation(View... views) {
        for (View view : views)
            view.animate().scaleX(0.975f).scaleY(0.975f).setDuration(175).withEndAction(() -> view.animate().scaleX(1).scaleY(1).setDuration(175));
    }

    /**
     * Executes after click on VibrationSwitch in SettingsFragment
     * Sets current vibration switch value to SharedPreferences memory and update vibrationState variable value
     *
     * @param value Vibration switch value
     */
    public void clickVibration(boolean value) {
        sharedPreferences.edit().putBoolean("vibration", value).apply();
        vibrationState = value;
    }

    /**
     * Executes after click on notification switch in SettingsFragment
     * Sets notification switch value to SharedPreferences memory and update notificationState variable value
     * If notification switch is true, will be unable alarm for notifications, else will be disabled
     *
     * @param value Notifications switch value
     */
    public void clickNotification(boolean value) {
        sharedPreferences.edit().putBoolean("notification", value).apply();
        notificationState = value;
        if (notificationState) setAlarm();
        else offAlarm();
    }

    /**
     * Enables notification alarm, sets current score value to intent extras
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
     * Disables notification alarm
     */
    private void offAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getService(MainActivity.this, 0, new Intent(MainActivity.this, NotificationsReceiver.class), 0);
        if (pendingIntent != null && alarmManager != null) alarmManager.cancel(pendingIntent);
    }

    /**
     * Executes after click on animation switch
     * Sets animation switch value to SharedPreferences memory and update animationState value
     *
     * @param value Animation switch value
     */
    public void clickAnimation(boolean value) {
        sharedPreferences.edit().putBoolean("animation", value).apply();
        animationState = value;
    }

    /**
     * Executes after click on splash switch
     * Sets splash switch value to SharedPreferences memory and update splashState value
     *
     * @param value Splash switch value
     */
    public void clickSplash(boolean value) {
        sharedPreferences.edit().putBoolean("splash", value).apply();
        splashState = value;
    }

    /**
     * Opens OptionsFragment
     *
     * @see android.widget.Button
     */
    public void clickOptions() {
        makeVibration(1);
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.breath_in, R.anim.breath_out)
                .replace(R.id.settings_fragment, new OptionsFragment(), null)
                .commit();
    }

    /**
     * Opens ResetFragment
     *
     * @see android.widget.Button
     */
    public void clickReset() {
        makeVibration(1);
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.breath_in, R.anim.breath_out)
                .replace(R.id.settings_fragment, new ResetFragment(), null)
                .commit();
    }

    /**
     * Opens ScoreShareFragment
     *
     * @see android.widget.Button
     */
    public void clickScoreShare() {
        makeVibration(1);
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.breath_in, R.anim.breath_out)
                .replace(R.id.settings_fragment, new ScoreShareFragment(), null)
                .commit();
    }

    /**
     * Opens IconsFragment
     *
     * @see android.widget.Button
     */
    public void clickIcons() {
        makeVibration(1);
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.breath_in, R.anim.breath_out)
                .replace(R.id.settings_fragment, new IconsFragment(), null)
                .commit();
    }

    /**
     * Returns current application icon name from SharedPreferences memory.
     *
     * @return Current application icon name
     * @see SharedPreferences
     */
    public String getApplicationIcon() {
        return sharedPreferences.getString("icon", "default");
    }

    /**
     * Opens SplashMessageFragment
     *
     * @see android.widget.Button
     * @see SplashMessageFragment
     */
    public void clickSplashMessage() {
        SplashMessageFragment.newInstance().show(getSupportFragmentManager(), "splash_message_fragment");
    }

    /**
     * Sets SplashMessage Position to SharedPreferences memory
     *
     * @param horizontal Horizontal float value
     * @param vertical   Vertical float value
     * @see SharedPreferences
     */
    public void setSplashPosition(float horizontal, float vertical) {
        sharedPreferences.edit().putFloat("splash_horizontal", horizontal).putFloat("splash_vertical", vertical).apply();
    }

    /**
     * Removes ToastFragment from the screen
     *
     * @see ToastFragment
     * @see android.widget.FrameLayout
     */
    public void removeToast() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.breath_in, R.anim.breath_out);
        transaction.setReorderingAllowed(true);

        transaction.remove(getSupportFragmentManager().findFragmentById(R.id.toast_fragment));
        transaction.commit();
    }

    /**
     * Changes application icon value in SharedPreference memory
     *
     * @param icon Icon name value
     */
    public void changeIcon(String icon) {
        sharedPreferences.edit().putString("icon", icon).apply();
    }
}
