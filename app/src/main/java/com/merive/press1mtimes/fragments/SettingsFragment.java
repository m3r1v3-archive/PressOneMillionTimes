package com.merive.press1mtimes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class SettingsFragment extends Fragment {

    TextView info;
    SwitchCompat vibration, notification, acceleration;

    /**
     * This method is creating SettingsFragment.
     *
     * @param inflater           Needs for getting Fragment View.
     * @param parent             Argument of inflater.inflate().
     * @param savedInstanceState Save Fragment Values.
     * @return Fragment View.
     * @see View
     * @see Bundle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, parent, false);
    }

    /**
     * This method is executing after Fragment View was created.
     *
     * @param view               Fragment View Value.
     * @param savedInstanceState Saving Fragment Values.
     * @see View
     * @see Bundle
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initVariables();
        setSwitchStates();
        setInfo();
        setSwitchClickListener();
    }

    /**
     * This method is initializing layout variables.
     *
     * @see View
     */
    private void initVariables() {
        vibration = getView().findViewById(R.id.vibration_switch);
        notification = getView().findViewById(R.id.notification_switch);
        acceleration = getView().findViewById(R.id.acceleration_switch);
        info = getView().findViewById(R.id.info_text);
    }

    private void setSwitchStates() {
        vibration.setChecked(((MainActivity) getActivity()).getVibrationState());
        notification.setChecked(((MainActivity) getActivity()).getNotificationState());
        acceleration.setChecked(((MainActivity) getActivity()).getAccelerationState());
    }

    private void setInfo() {
        info.setText(((MainActivity) getActivity()).getInfo());
    }

    private void setSwitchClickListener() {
        vibration.setOnClickListener(v -> ((MainActivity) getActivity()).clickVibration(vibration.isChecked()));
        notification.setOnClickListener(v -> ((MainActivity) getActivity()).clickNotification(notification.isChecked()));
        acceleration.setOnClickListener(v -> ((MainActivity) getActivity()).clickAcceleration(acceleration.isChecked()));
    }
}
