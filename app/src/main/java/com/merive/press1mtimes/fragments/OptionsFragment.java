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

    Button reset, scoreShare, changeIcon, splashPosition;

    /**
     * OptionsFragment empty constructor.
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
     * This method is creating OptionsFragment.
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
     * This method is executing after Fragment View was created.
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

        reset.setOnClickListener(v -> clickReset());
        scoreShare.setOnClickListener(v -> clickScoreShare());
        changeIcon.setOnClickListener(v -> clickChangeIcon());
        splashPosition.setOnClickListener(v -> clickSplashPosition());
    }

    /**
     * This method is initializing layout variables.
     *
     * @see View
     */
    private void initVariables() {
        reset = getView().findViewById(R.id.reset_button);
        scoreShare = getView().findViewById(R.id.score_share_button);
        changeIcon = getView().findViewById(R.id.change_icon_button);
        splashPosition = getView().findViewById(R.id.splash_position_button);
    }

    /**
     * This method is executing after clicking on reset button.
     * The method is making vibration and open ConfirmFragment for confirming reset.
     *
     * @see ConfirmFragment
     */
    private void clickReset() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickReset();
        dismiss();
    }

    /**
     * This method is executing after clicking on ScoreShare button.
     * The method is making vibration and open ScoreShareFragment.
     *
     * @see ScoreShareFragment
     */
    private void clickScoreShare() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickScoreShare();
        dismiss();
    }

    /**
     * This method is executing after clicking on Change Icon button.
     * The method is making vibration and open ChangeIconFragment.
     *
     * @see ChangeIconFragment
     */
    private void clickChangeIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickChangeIcon();
        dismiss();
    }

    private void clickSplashPosition() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickSplashPosition();
        dismiss();
    }
}
