package com.merive.press1mtimes.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class ResetFragment extends DialogFragment {

    TextView title;
    Button cancel, confirm;

    /**
     * ResetFragment constructor.
     */
    public ResetFragment() {
    }

    /**
     * This method returns ResetFragment object.
     *
     * @return ResetFragment object.
     */
    public static ResetFragment newInstance() {
        return new ResetFragment();
    }

    /**
     * This method executes when ResetFragment is creating.
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
        return inflater.inflate(R.layout.reset_fragment, parent);
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

        cancel.setOnClickListener(v -> clickCancel());
        confirm.setOnClickListener(v -> clickConfirm());
    }

    /**
     * This method initializes layout variables.
     *
     * @see View
     */
    private void initVariables() {
        title = getView().findViewById(R.id.reset_title);
        cancel = getView().findViewById(R.id.cancel_reset_button);
        confirm = getView().findViewById(R.id.confirm_reset_button);
    }

    /**
     * This method executes after click on Cancel Button.
     */
    private void clickCancel() {
        ((MainActivity) getActivity()).makeVibration(1);
        dismiss();
    }

    /**
     * This method executes after click on Confirm Button.
     * The method makes vibration and resets counter.
     */
    private void clickConfirm() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).resetCounter();
        ((MainActivity) getActivity()).makeToast("The Score has been reset");
        dismiss();
    }
}
