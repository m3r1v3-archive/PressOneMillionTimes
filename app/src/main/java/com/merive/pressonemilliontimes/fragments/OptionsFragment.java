package com.merive.pressonemilliontimes.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.merive.pressonemilliontimes.R;
import com.merive.pressonemilliontimes.activities.MainActivity;

public class OptionsFragment extends Fragment {

    Button resetButton, scoreShareButton, iconsButton, splashButton, cancelButton;
    TextView tipText;
    MainActivity mainActivity;
    boolean showTip = false;

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
        return inflater.inflate(R.layout.fragment_options, parent, false);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view
     * There initializes basic variables, sets click listeners for buttons
     *
     * @param view               Fragment View Value
     * @param savedInstanceState Saving Fragment Values
     * @see View
     * @see Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initVariables();
        setListeners();
        setLongListeners();
    }

    /**
     * Initializes basic layout components
     *
     * @see View
     */
    private void initVariables() {
        resetButton = getView().findViewById(R.id.reset_button);
        scoreShareButton = getView().findViewById(R.id.score_share_button);
        iconsButton = getView().findViewById(R.id.icons_button);
        splashButton = getView().findViewById(R.id.splash_button);
        cancelButton = getView().findViewById(R.id.options_cancel_button);
        tipText = getView().findViewById(R.id.options_tip_text);
        mainActivity = ((MainActivity) getActivity());
    }

    /**
     * This method sets click listeners for OptionsFragment buttons
     *
     * @see Button
     */
    private void setListeners() {
        resetButton.setOnClickListener(v -> clickReset());
        scoreShareButton.setOnClickListener(v -> clickScoreShare());
        iconsButton.setOnClickListener(v -> clickIcons());
        splashButton.setOnClickListener(v -> clickSplash());
        cancelButton.setOnClickListener(v -> clickCancel());
    }

    /**
     * This method sets long click listeners for OptionsFragment buttons
     *
     * @see Button
     */
    private void setLongListeners() {
        resetButton.setOnLongClickListener(v -> {
            longClickReset();
            resetTip();
            return true;
        });
        scoreShareButton.setOnLongClickListener(v -> {
            longClickScoreShare();
            resetTip();
            return true;
        });
        iconsButton.setOnLongClickListener(v -> {
            longClickIcons();
            resetTip();
            return true;
        });
        splashButton.setOnLongClickListener(v -> {
            longClickSplash();
            resetTip();
            return true;
        });
    }

    /**
     * This method executes after clicking on resetButton
     * The method makes vibration and opens ConfirmFragment for confirming reset
     *
     * @see ResetFragment
     */
    private void clickReset() {
        mainActivity.makeVibration();
        mainActivity.setFragment(new ResetFragment());
    }

    /**
     * Executes when clicking scoreShareButton
     * Makes vibration and opens ScoreShareFragment
     *
     * @see ScoreShareFragment
     */
    private void clickScoreShare() {
        mainActivity.makeVibration();
        mainActivity.setFragment(new ScoreShareFragment());
    }

    /**
     * Executes when clicking iconsButton
     * The method makes vibration and opens IconsFragment
     *
     * @see IconsFragment
     */
    private void clickIcons() {
        mainActivity.makeVibration();
        mainActivity.setFragment(new IconsFragment());
    }

    /**
     * Executes when clicking splashButton
     * Makes vibration and opens SplashMessagesFragment
     *
     * @see SplashMessageFragment
     */
    private void clickSplash() {
        mainActivity.makeVibration();
        mainActivity.setFragment(new SplashMessageFragment());
    }

    /**
     * Executes when clicking cancelButton
     * Makes vibration and returns to SettingsFragment
     *
     * @see SplashMessageFragment
     */
    private void clickCancel() {
        mainActivity.makeVibration();
        mainActivity.setFragment(new SettingsFragment());
        showTip = false;
    }

    /**
     * Executes when making long click on resetButton
     * Change tipText text value
     */
    private void longClickReset() {
        if (!showTip) {
            setFadeText(tipText, getResources().getString(R.string.options_tip_reset));
            showTip = true;
        }
    }

    /**
     * Executes when making long click on scoreShareButton
     * Change tipText text value
     */
    private void longClickScoreShare() {
        if (!showTip) {
            setFadeText(tipText, getResources().getString(R.string.options_tip_score_share));
            showTip = true;
        }
    }

    /**
     * Executes when making long click on iconsButton
     * Change tipText text value
     */
    private void longClickIcons() {
        if (!showTip) {
            setFadeText(tipText, getResources().getString(R.string.options_tip_icons));
            showTip = true;
        }
    }

    /**
     * Executes when making long click on splashButton
     * Change tipText text value
     */
    private void longClickSplash() {
        if (!showTip) {
            setFadeText(tipText, getResources().getString(R.string.options_tip_splash));
            showTip = true;
        }
    }

    /**
     * Resets tipText text to default value
     */
    private void resetTip() {
        if (showTip) {
            new Handler().postDelayed(() -> {
                if (showTip) setFadeText(tipText, getResources().getString(R.string.options_tip));
                showTip = false;
            }, 2500);
        }
    }

    /**
     * Sets text to TextView with fade animation
     *
     * @param view TextView component
     * @param text Future TextView text value
     */
    private void setFadeText(TextView view, String text) {
        view.animate().alpha(1.0f).setDuration(625).withEndAction(() -> view.animate().alpha(0.1f).setDuration(625));
        new Handler().postDelayed(() -> view.setText(text), 625);
        view.animate().alpha(0.1f).setDuration(625).withEndAction(() -> view.animate().alpha(1.0f).setDuration(625));
    }
}
