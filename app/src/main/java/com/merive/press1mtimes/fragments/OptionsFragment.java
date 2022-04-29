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
     * OptionsFragment constructor.
     */
    public OptionsFragment() {
    }

    /**
     * This method returns OptionsFragment object.
     *
     * @return OptionsFragment object.
     */
    public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }

    /**
     * This method executes when OptionsFragment is creating.
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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.options_fragment, parent);
    }

    /**
     * This method executes after Fragment View was created.
     *
     * @param view               Fragment View Value.
     * @param savedInstanceState Saving Fragment Values.
     * @see View
     * @see Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        initVariables();

        resetButton.setOnClickListener(v -> clickReset());
        scoreShareButton.setOnClickListener(v -> clickScoreShare());
        iconsButton.setOnClickListener(v -> clickIcons());
        splashMessageButton.setOnClickListener(v -> clickSplashPosition());
    }

    /**
     * This method initializes layout variables.
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
     * This method executes after clicking on reset button.
     * The method makes vibration and opens ConfirmFragment for confirming reset.
     *
     * @see ResetFragment
     */
    private void clickReset() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickReset();
        dismiss();
    }

    /**
     * This method executes after clicking on ScoreShare button.
     * The method makes vibration and opens ScoreShareFragment.
     *
     * @see ScoreShareFragment
     */
    private void clickScoreShare() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickScoreShare();
        dismiss();
    }

    /**
     * This method executes after clicking on iconsButton.
     * The method makes vibration and opens IconsFragment.
     *
     * @see IconsFragment
     */
    private void clickIcons() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickIcons();
        dismiss();
    }

    /**
     * This method executes after click Splash Position button.
     *
     * @see SplashMessageFragment
     */
    private void clickSplashPosition() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickSplashMessage();
        dismiss();
    }
}
