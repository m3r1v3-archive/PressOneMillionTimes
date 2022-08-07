package com.merive.press1mtimes.activities;

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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.merive.press1mtimes.BuildConfig;
import com.merive.press1mtimes.R;
import com.merive.press1mtimes.fragments.SettingsFragment;
import com.merive.press1mtimes.fragments.UpdateFragment;
import com.merive.press1mtimes.preferences.PreferencesManager;
import com.merive.press1mtimes.receivers.NotificationReceiver;
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
import java.util.Random;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    public static PreferencesManager preferencesManager;

    private TextView titleText, counterText, splashText;
    private ImageButton pressButton;

    /**
     * Called by the system when the service is first created
     *
     * @param savedInstanceState Using by super.onCreate method
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.breath_in, R.anim.breath_out);

        preferencesManager = new PreferencesManager(this.getBaseContext());

        initLayoutVariables();

        setFragment(new SettingsFragment());
        setScoreToCounter();

        createNotificationChannel();

        checkVersion();
        checkSplashMessage();
        checkAnimation();

        try {
            checkQRPattern(getIntent().getData().toString());
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
        if (preferencesManager.getNotification()) setAlarm();
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
        if (IntentIntegrator.parseActivityResult(requestCode, resultCode, intent) != null)
            try {
                checkQRPattern(intent.getStringExtra("SCAN_RESULT"));
            } catch (NullPointerException ignore) {
            }
    }

    /**
     * Checks QR Code result pattern
     * If result corresponds to the pattern, executes setScoreByQRResult method
     *
     * @param result QR result.
     * @see Pattern
     */
    private void checkQRPattern(String result) {
        if (Pattern.compile("press1mtimes://[0-9A-F]{1,6}").matcher(result).find())
            setScoreByQRResult(result);
        else makeToast(getResources().getString(R.string.qr_error));
    }

    /**
     * Sets score by QR Code Result value
     *
     * @param result Score value from QR Code result
     */
    private void setScoreByQRResult(String result) {
        preferencesManager.setScore(Integer.parseInt(result.replace("press1mtimes://", ""), 16));
        setScoreToCounter();
        makeVibration(1);
        makeToast(getResources().getString(R.string.score_changed));
    }

    /**
     * Makes vibration on device
     *
     * @param times Vibration times (Using for make vibration longer)
     */
    public void makeVibration(int times) {
        if (preferencesManager.getVibration())
            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(75L * times);
    }

    /**
     * Makes Toast message
     *
     * @param text Toast text value
     */
    public void makeToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
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
     * Sets fragment to pad_fragment component using fragment parameter
     *
     * @param fragment Future fragment in pad_fragment component
     */
    public void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.breath_in, R.anim.breath_out)
                .setReorderingAllowed(true)
                .replace(R.id.pad_fragment, fragment, null).commit();
    }

    /**
     * Sets score value to counterText component
     */
    public void setScoreToCounter() {
        counterText.setText(String.format("%06d", preferencesManager.getScore()));
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
            NotificationChannel channel = new NotificationChannel("notifyPress1MTimes", "Press1MTimesChannel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for Press1MTimes");
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
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
    private void checkSplashMessage() {
        if (preferencesManager.getSplash()) {
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
        constraintSet.setHorizontalBias(splashText.getId(), preferencesManager.getSplashPositionHorizontal());
        constraintSet.setVerticalBias(splashText.getId(), preferencesManager.getSplashPositionVertical());
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
        if (preferencesManager.getScore() == 999999) {
            resetScore();
            openFinish();
        } else preferencesManager.setScore(preferencesManager.getScore() + 1);
        setScoreToCounter();
        makeVibrationByScore(preferencesManager.getScore());
        if (preferencesManager.getAnimation())
            makeBreathAnimation(titleText, findViewById(R.id.counter_layout), pressButton);
    }

    /**
     * Disable coin falling animation if animation shared preference is false.
     * Else enable it
     */
    public void checkAnimation() {
        if (preferencesManager.getAnimation()) findViewById(R.id.coins).setVisibility(View.VISIBLE);
        else findViewById(R.id.coins).setVisibility(View.INVISIBLE);
    }

    /**
     * Resets score value to default value (default score value is 000000)
     * Updates score value in SharedPreferences memory and sets default value to counter
     */
    public void resetScore() {
        preferencesManager.setScore(0);
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
        makeVibration(score % 100000 == 0 ? 3 : score % 10000 == 0 ? 2 : 1);
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
     * Enables notification alarm, sets current score value to intent extras
     */
    public void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 12) calendar.add(Calendar.DATE, 1);

        ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(getBaseContext(), NotificationReceiver.class).putExtra("score", String.valueOf(preferencesManager.getScore())), PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE));
    }

    /**
     * Disables notification alarm
     */
    public void offAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, new Intent(MainActivity.this, NotificationReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent != null && alarmManager != null) alarmManager.cancel(pendingIntent);
    }

    /**
     * Sets SplashMessage Position to SharedPreferences memory
     *
     * @param horizontal Horizontal float value
     * @param vertical   Vertical float value
     * @see SharedPreferences
     */
    public void setSplashPosition(float horizontal, float vertical) {
        preferencesManager.setSplashPosition(horizontal, vertical);
    }
}
