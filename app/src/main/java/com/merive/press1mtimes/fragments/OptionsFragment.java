package com.merive.press1mtimes.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class OptionsFragment extends Fragment {

    Button resetButton, scoreShareButton, iconsButton, splashMessageButton, cancelButton;
    TextView tipText;
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
        return inflater.inflate(R.layout.options_fragment, parent, false);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
        splashMessageButton = getView().findViewById(R.id.splash_button);
        cancelButton = getView().findViewById(R.id.options_cancel_button);
        tipText = getView().findViewById(R.id.options_tip_text);
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
        splashMessageButton.setOnClickListener(v -> clickSplash());
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
        splashMessageButton.setOnLongClickListener(v -> {
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
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickReset();
    }

    /**
     * Executes when clicking scoreShareButton
     * Makes vibration and opens ScoreShareFragment
     *
     * @see ScoreShareFragment
     */
    private void clickScoreShare() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickScoreShare();
    }

    /**
     * Executes when clicking iconsButton
     * The method makes vibration and opens IconsFragment
     *
     * @see IconsFragment
     */
    private void clickIcons() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickIcons();
    }

    /**
     * Executes when clicking splashButton
     * Makes vibration and opens SplashMessagesFragment
     *
     * @see SplashMessageFragment
     */
    private void clickSplash() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickSplashMessage();
    }

    /**
     * Executes when clicking cancelButton
     * Makes vibration and returns to SettingsFragment
     *
     * @see SplashMessageFragment
     */
    private void clickCancel() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).initSettingsFragment();
    }

    /**
     * Executes when making long click on resetButton
     * Change tipText text value
     */
    private void longClickReset() {
        setFadeText(tipText, getResources().getString(R.string.options_tip_reset));
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
            new Handler().postDelayed(() -> setFadeText(tipText, getResources().getString(R.string.options_tip)), 2500);
            showTip = false;
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
