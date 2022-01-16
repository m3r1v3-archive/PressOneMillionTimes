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
     * SplashPosition empty constructor.
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
     * This method is creating SplashPositionFragment.
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
        setListeners();
    }

    /**
     * This method is initializing layout variables.
     *
     * @see View
     */
    private void initVariables() {
        rightTop = getView().findViewById(R.id.right_top_button);
        rightBottom = getView().findViewById(R.id.right_bottom_button);
        leftTop = getView().findViewById(R.id.left_top_button);
        leftBottom = getView().findViewById(R.id.left_bottom_button);
    }

    private void setListeners() {
        rightTop.setOnClickListener(v -> setPosition(0.98f, 0.02f));
        rightBottom.setOnClickListener(v -> setPosition(0.98f, 0.98f));
        leftTop.setOnClickListener(v -> setPosition(0.02f, 0.02f));
        leftBottom.setOnClickListener(v -> setPosition(0.02f, 0.98f));
    }

    private void setPosition(float horizontal, float vertical) {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).setSplashPosition(horizontal, vertical);
        ((MainActivity) getActivity()).makeToast("Updated Splash Position");
        dismiss();
    }
}


