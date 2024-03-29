package com.merive.pressonemilliontimes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.merive.pressonemilliontimes.R;
import com.merive.pressonemilliontimes.activities.MainActivity;

public class ResetFragment extends Fragment {

    TextView titleText;
    Button cancelButton, confirmButton;
    MainActivity mainActivity;


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
        return inflater.inflate(R.layout.fragment_reset, parent, false);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view
     * There initializes basic variables, sets button listeners
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
        titleText = getView().findViewById(R.id.reset_title);
        cancelButton = getView().findViewById(R.id.reset_cancel_button);
        confirmButton = getView().findViewById(R.id.reset_confirm_button);
        mainActivity = ((MainActivity) getActivity());
    }

    /**
     * Sets button click listeners
     *
     * @see Button
     */
    private void setListeners() {
        cancelButton.setOnClickListener(v -> clickCancel());
        confirmButton.setOnClickListener(v -> clickConfirm());
    }

    /**
     * Executes when clicking on cancelButton
     * Makes vibration effect and closes ResetFragment
     */
    private void clickCancel() {
        mainActivity.makeVibration();
        mainActivity.setFragment(new SettingsFragment());
    }

    /**
     * Executes when clicking on confirmButton
     * Makes vibration, resets counter value (sets default value (default value is 000000)) and makes toast message
     */
    private void clickConfirm() {
        mainActivity.makeVibration();
        mainActivity.resetScore();
        mainActivity.setScoreToCounter();
        mainActivity.makeToast("The Score has been reset");
        mainActivity.setFragment(new SettingsFragment());
    }
}
