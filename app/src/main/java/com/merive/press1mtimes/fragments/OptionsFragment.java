package com.merive.press1mtimes.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class OptionsFragment extends DialogFragment {

    Button reset, scoreShare, changeIcon;
    ImageView cancel;


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

        reset = view.findViewById(R.id.reset);
        reset.setOnClickListener(v -> {
            ((MainActivity) getActivity()).clickReset();
            dismiss();
        });

        scoreShare = view.findViewById(R.id.scoreShare);
        scoreShare.setOnClickListener(v -> {
            ((MainActivity) getActivity()).clickScoreShare();
            dismiss();
        });

        /* OnClick Cancel */
        cancel = view.findViewById(R.id.closeOptions);
        cancel.setOnClickListener(v -> {
            dismiss();
        });
    }
}
