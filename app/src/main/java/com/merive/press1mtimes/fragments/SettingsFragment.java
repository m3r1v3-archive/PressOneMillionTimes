package com.merive.press1mtimes.fragments;

import static java.util.Calendar.YEAR;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.merive.press1mtimes.BuildConfig;
import com.merive.press1mtimes.R;
import com.merive.press1mtimes.activities.MainActivity;

import java.util.Calendar;

public class SettingsFragment extends Fragment {

    TextView infoText;
    SwitchCompat vibrationSwitch, notificationSwitch, accelerationSwitch, splashSwitch;
    Button optionsButton;

    /**
     * Called to have the fragment instantiate its user interface view
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param parent             If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return Return the View for the fragment's UI, or null
     * @see View
     * @see Bundle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, parent, false);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view
     * There initializes basic variables, sets switch states, sets switch click listeners and sets information about application to info TextView
     *
     * @param view               The View returned by onCreateView
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @see View
     * @see Bundle
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initVariables();
        setSwitchStates();
        setSwitchClickListener();
        setOptionsListener();
        setInfo();
    }

    /**
     * Initializes basic layout components
     *
     * @see View
     */
    private void initVariables() {
        vibrationSwitch = getView().findViewById(R.id.vibration_switch);
        notificationSwitch = getView().findViewById(R.id.notification_switch);
        accelerationSwitch = getView().findViewById(R.id.animation_switch);
        splashSwitch = getView().findViewById(R.id.splash_switch);

        optionsButton = getView().findViewById(R.id.options_button);

        infoText = getView().findViewById(R.id.info_text);
    }

    /**
     * Sets switch states values
     *
     * @see SwitchCompat
     */
    private void setSwitchStates() {
        vibrationSwitch.setChecked(MainActivity.preferencesManager.getVibration());
        notificationSwitch.setChecked(MainActivity.preferencesManager.getNotification());
        accelerationSwitch.setChecked(MainActivity.preferencesManager.getAnimation());
        splashSwitch.setChecked(MainActivity.preferencesManager.getSplash());
    }

    /**
     * Sets click listeners for switches
     *
     * @see SwitchCompat
     */
    private void setSwitchClickListener() {
        vibrationSwitch.setOnClickListener(v -> MainActivity.preferencesManager.setVibration(vibrationSwitch.isChecked()));
        notificationSwitch.setOnClickListener(v -> clickNotification(notificationSwitch.isChecked()));
        accelerationSwitch.setOnClickListener(v -> MainActivity.preferencesManager.setAnimation(accelerationSwitch.isChecked()));
        splashSwitch.setOnClickListener(v -> MainActivity.preferencesManager.setSplash(splashSwitch.isChecked()));
    }

    /**
     * Sets click listener for optionsButton
     *
     * @see Button
     * @see MainActivity
     */
    private void setOptionsListener() {
        optionsButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setFragment(new OptionsFragment());
            ((MainActivity) getActivity()).makeVibration(1);
        });
    }

    /**
     * Executes after click on notification switch in SettingsFragment
     * Sets notification switch value to SharedPreferences memory and update notificationState variable value
     * If notification switch is true, will be unable alarm for notifications, else will be disabled
     *
     * @param value Notifications switch value
     */
    private void clickNotification(boolean value) {
        MainActivity.preferencesManager.setNotification(notificationSwitch.isChecked());
        if (MainActivity.preferencesManager.getNotification())
            ((MainActivity) getActivity()).setAlarm();
        else ((MainActivity) getActivity()).offAlarm();
    }

    /**
     * Sets information info from getInfo() to infoText TextView
     *
     * @see TextView
     * @see MainActivity
     */
    private void setInfo() {
        infoText.setText(getInfo());
    }

    /**
     * Returns project info string
     */
    private String getInfo() {
        return ("P1MT / " + BuildConfig.VERSION_NAME + "\nmerive_ inc. / MIT License, " + Calendar.getInstance().get(YEAR));
    }
}
