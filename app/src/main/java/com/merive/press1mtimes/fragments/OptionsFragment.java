package com.merive.press1mtimes.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class OptionsFragment extends DialogFragment {

    Button resetButton, scoreShareButton, iconsButton, splashMessageButton;

    /**
     * Creates new instance of OptionsFragment that will be initialized with the given arguments
     *
     * @return New instance of OptionsFragment with necessary arguments
     */
    public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }

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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.options_fragment, parent);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        initVariables();
        setListeners();
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
        splashMessageButton = getView().findViewById(R.id.splash_position_button);
    }

    /**
     * This method sets click listeners for rightButton and leftButton
     *
     * @see Button
     */
    private void setListeners() {
        resetButton.setOnClickListener(v -> clickReset());
        scoreShareButton.setOnClickListener(v -> clickScoreShare());
        iconsButton.setOnClickListener(v -> clickIcons());
        splashMessageButton.setOnClickListener(v -> clickSplash());
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
        dismiss();
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
        dismiss();
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
        dismiss();
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
        dismiss();
    }
}
