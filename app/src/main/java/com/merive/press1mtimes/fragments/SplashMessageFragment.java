package com.merive.press1mtimes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.merive.press1mtimes.R;
import com.merive.press1mtimes.activities.MainActivity;

public class SplashMessageFragment extends Fragment {

    ConstraintLayout leftButton, rightButton;
    Button cancel;


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
        return inflater.inflate(R.layout.fragment_splash_message, parent, false);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view
     * There initializes basic variables, sets click listeners for buttons
     *
     * @param view               The View returned by onCreateView
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @see View
     * @see Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVariables();
        setListeners();
    }

    /**
     * Initializes basic layout components
     *
     * @see View
     */
    private void initVariables() {
        rightButton = getView().findViewById(R.id.splash_right_position_button);
        leftButton = getView().findViewById(R.id.splash_left_position_button);
        cancel = getView().findViewById(R.id.splash_message_cancel_button);
    }

    /**
     * This method sets click listeners for rightButton and leftButton
     *
     * @see ImageView
     */
    private void setListeners() {
        rightButton.setOnClickListener(v -> setPosition(0.98f, 0.98f));
        leftButton.setOnClickListener(v -> setPosition(0.02f, 0.98f));
        cancel.setOnClickListener(v -> clickCancel());
    }

    /**
     * Executes when clicking on cancelButton
     * Makes vibration effect and closes SplashMessageFragment
     */
    private void clickCancel() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).setFragment(new SettingsFragment());
    }

    /**
     * This methods saves Splash Message Position in sharedPreferences memory
     *
     * @param horizontal Horizontal float position value
     * @param vertical   Vertical float position value
     */
    private void setPosition(float horizontal, float vertical) {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).setSplashPosition(horizontal, vertical);
        ((MainActivity) getActivity()).makeToast(getResources().getString(R.string.splash_position_changed));
        ((MainActivity) getActivity()).setFragment(new SettingsFragment());
    }
}
