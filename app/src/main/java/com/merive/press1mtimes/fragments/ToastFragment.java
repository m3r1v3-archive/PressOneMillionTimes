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

    TextView text;

    public static ToastFragment newInstance(String message) {
        ToastFragment frag = new ToastFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.toast, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initVariables(view);
        setText();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            ((MainActivity) getActivity()).removeToast();
        }, 3500);
    }

    private void initVariables(View view) {
        text = view.findViewById(R.id.toast_text);
    }

    private void setText() {
        text.setText(getArguments().getString("message"));
    }
}
