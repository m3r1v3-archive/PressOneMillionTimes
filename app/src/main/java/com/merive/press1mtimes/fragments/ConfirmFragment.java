package com.merive.press1mtimes.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class ConfirmFragment extends DialogFragment {

    TextView title;
    Button cancel, confirm;

    public ConfirmFragment() {
        /* Empty constructor (Needs) */
    }

    public static ConfirmFragment newInstance() {
        /* newInstance method */
        ConfirmFragment frag = new ConfirmFragment();
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
        return inflater.inflate(R.layout.confirm_fragment, container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVariables(view);

        cancel.setOnClickListener(v -> {
            clickCancel();
        });

        confirm.setOnClickListener(v -> {
            clickConfirm();
        });
    }

    /* ************ */
    /* Init methods */
    /* ************ */

    public void initVariables(View view) {
        /* Init main variables */
        title = view.findViewById(R.id.confirmTitle);
        cancel = view.findViewById(R.id.cancelConfirm);
        confirm = view.findViewById(R.id.okayConfirm);
    }

    /* ************* */
    /* Click methods */
    /* ************* */

    public void clickCancel() {
        /* Click Cancel Button */
        dismiss();
    }

    public void clickConfirm() {
        /* Click Confirm Button */
        ((MainActivity) getActivity()).resetCounter();
        dismiss();
    }
}
