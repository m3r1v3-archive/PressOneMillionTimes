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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class SplashMessageFragment extends DialogFragment {

    ConstraintLayout leftButton, rightButton;


    /**
     * Creates new instance of SplashMessageFragment that will be initialized with the given arguments
     *
     * @return New instance of SplashMessageFragment with necessary arguments
     */
    public static SplashMessageFragment newInstance() {
        return new SplashMessageFragment();
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
        return inflater.inflate(R.layout.splash_message_fragment, parent);
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
        rightButton = getView().findViewById(R.id.right_position_button);
        leftButton = getView().findViewById(R.id.left_position_button);
    }

    /**
     * This method sets click listeners for rightButton and leftButton
     *
     * @see ImageView
     */
    private void setListeners() {
        rightButton.setOnClickListener(v -> setPosition(0.98f, 0.98f));
        leftButton.setOnClickListener(v -> setPosition(0.02f, 0.98f));
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
        ((MainActivity) getActivity()).makeToast(getResources().getString(R.string.splash_position_updated));
        dismiss();
    }
}


