package com.merive.press1mtimes.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class ConfirmFragment extends DialogFragment {

    TextView title;
    ImageView cancel, confirm;

    public ConfirmFragment() {
    }

    public static ConfirmFragment newInstance() {
        ConfirmFragment frag = new ConfirmFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.confirm_fragment, container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.confirmTitle);

        /* OnClick Cancel */
        cancel = view.findViewById(R.id.cancelConfirm);
        cancel.setOnClickListener(v -> {
            clickCancel();
        });

        /* OnClick OkayConfirm */
        confirm = view.findViewById(R.id.okayConfirm);
        confirm.setOnClickListener(v -> {
            clickConfirm();
        });
    }

    public void clickCancel() {
        dismiss();
    }

    public void clickConfirm() {
        ((MainActivity) getActivity()).resetCounter();
        dismiss();
    }
}
