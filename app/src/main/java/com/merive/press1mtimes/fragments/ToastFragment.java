package com.merive.press1mtimes.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class ToastFragment extends Fragment {

    TextView message;

    /**
     * This method returns ToastFragment object.
     *
     * @return ToastFragment object.
     */
    public static ToastFragment newInstance(String message) {
        ToastFragment frag = new ToastFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    /**
     * This method is creating ToastFragment.
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
        return inflater.inflate(R.layout.toast, parent, false);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initVariables();
        setText();
        new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) getActivity()).removeToast(), 3500);
    }

    /**
     * This method is initializing layout variables.
     *
     * @see View
     */
    private void initVariables() {
        message = getView().findViewById(R.id.toast_message);
    }

    /**
     * This method is setting text value.
     */
    private void setText() {
        message.setText(getArguments().getString("message"));
    }
}
