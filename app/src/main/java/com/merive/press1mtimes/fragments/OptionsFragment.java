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
    }

    public static OptionsFragment newInstance() {
        OptionsFragment frag = new OptionsFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.options_fragment, container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

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

    public void initVariables(View view) {
        reset = view.findViewById(R.id.reset);
        scoreShare = view.findViewById(R.id.scoreShare);
        changeIcon = view.findViewById(R.id.changeIcon);
    }

    public void clickReset() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickReset();
        dismiss();
    }

    public void clickScoreShare() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickScoreShare();
        dismiss();
    }

    public void clickChangeIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).clickChangeIcon();
        dismiss();
    }
}
