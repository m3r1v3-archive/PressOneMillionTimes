package com.merive.press1mtimes.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class SplashPositionFragment extends DialogFragment {

    ImageView leftTop, leftBottom, rightTop, rightBottom;

    /**
     * SplashPosition constructor.
     */
    SplashPositionFragment() {
    }

    /**
     * This method returns SplashPositionFragment object.
     *
     * @return ChangeIconFragment object.
     */
    public static SplashPositionFragment newInstance() {
        return new SplashPositionFragment();
    }

    /**
     * This method executes when SplashPositionFragment is creating.
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
        return inflater.inflate(R.layout.splash_position_fragment, parent);
    }

    /**
     * This method executes after Fragment View has been created.
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
        setListeners();
    }

    /**
     * This method initializes layout variables.
     *
     * @see View
     */
    private void initVariables() {
        rightTop = getView().findViewById(R.id.right_top_button);
        rightBottom = getView().findViewById(R.id.right_bottom_button);
        leftTop = getView().findViewById(R.id.left_top_button);
        leftBottom = getView().findViewById(R.id.left_bottom_button);
    }

    /**
     * This method sets click listeners for ImageView.
     *
     * @see ImageView
     */
    private void setListeners() {
        rightTop.setOnClickListener(v -> setPosition(0.98f, 0.02f));
        rightBottom.setOnClickListener(v -> setPosition(0.98f, 0.98f));
        leftTop.setOnClickListener(v -> setPosition(0.02f, 0.02f));
        leftBottom.setOnClickListener(v -> setPosition(0.02f, 0.98f));
    }

    /**
     * This methods saves Splash Message Position in sharedPreference.
     *
     * @param horizontal Horizontal float position.
     * @param vertical Vertical float position.
     */
    private void setPosition(float horizontal, float vertical) {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).setSplashPosition(horizontal, vertical);
        ((MainActivity) getActivity()).makeToast(getResources().getString(R.string.splash_position_updated));
        dismiss();
    }
}


