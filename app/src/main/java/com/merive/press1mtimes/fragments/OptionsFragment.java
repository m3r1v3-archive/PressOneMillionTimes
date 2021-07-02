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

    Button reset, scoreShare, changeIcon;

    public OptionsFragment() {
        /* Empty constructor (Needs) */
    }

    public static OptionsFragment newInstance() {
        /* newInstance method */
        OptionsFragment frag = new OptionsFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    /* **************** */
    /* Override methods */
    /* **************** */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.options_fragment, container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVariables(view);

        reset.setOnClickListener(v -> {
            clickReset();
        });

        scoreShare.setOnClickListener(v -> {
            clickScoreShare();
        });

        changeIcon.setOnClickListener(v -> {
            clickChangeIcon();
        });
    }

    /* ************ */
    /* Init methods */
    /* ************ */

    public void initVariables(View view) {
        /* Init main variables */
        reset = view.findViewById(R.id.reset);
        scoreShare = view.findViewById(R.id.scoreShare);
        changeIcon = view.findViewById(R.id.changeIcon);
    }

    /* ************* */
    /* Click methods */
    /* ************* */

    public void clickReset() {
        /* Click Reset Button */
        ((MainActivity) getActivity()).clickReset();
        dismiss();
    }

    public void clickScoreShare() {
        /* Click ScoreShare Button */
        ((MainActivity) getActivity()).clickScoreShare();
        dismiss();
    }

    public void clickChangeIcon() {
        /* Click ChangeIcon Button */
        ((MainActivity) getActivity()).clickChangeIcon();
        dismiss();
    }
}
