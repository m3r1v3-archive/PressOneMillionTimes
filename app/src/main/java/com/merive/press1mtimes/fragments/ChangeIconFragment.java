package com.merive.press1mtimes.fragments;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class ChangeIconFragment extends DialogFragment {

    ImageView defaultIcon, P1MTIcon, classicIcon;

    ChangeIconFragment() {
    }

    public static ChangeIconFragment newInstance() {
        ChangeIconFragment frag = new ChangeIconFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.change_icon_fragment, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        defaultIcon = view.findViewById(R.id.defaultIcon);
        defaultIcon.setOnClickListener(v -> {
            disableOldIcon();
            turnOnIcon("default");
            ((MainActivity) getActivity()).changeIcon("default");
            showIconChangeToast();
            dismiss();
        });

        P1MTIcon = view.findViewById(R.id.P1MTIcon);
        P1MTIcon.setOnClickListener(v -> {
            disableOldIcon();
            turnOnIcon("P1MT");
            ((MainActivity) getActivity()).changeIcon("P1MT");
            showIconChangeToast();
            dismiss();
        });

        classicIcon = view.findViewById(R.id.classicIcon);
        classicIcon.setOnClickListener(v -> {
            disableOldIcon();
            turnOnIcon("Classic");
            ((MainActivity) getActivity()).changeIcon("Classic");
            showIconChangeToast();
            dismiss();
        });
    }

    public void disableOldIcon() {
        PackageManager packageManager = getActivity().getPackageManager();
        switch (((MainActivity) getActivity()).getIcon()) {
            case "default":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.SplashActivity"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                break;
            case "P1MT":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.P1MT"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                break;
            case "Classic":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.Classic"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                break;
        }
    }

    public void turnOnIcon(String selected) {
        PackageManager packageManager = getActivity().getPackageManager();
        switch (selected) {
            case "default":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.SplashActivity"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                break;
            case "P1MT":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.P1MT"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                break;
            case "Classic":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.Classic"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                break;
        }
    }

    public void showIconChangeToast() {
        Toast.makeText(getContext(), "Icon was changed.", Toast.LENGTH_SHORT).show();
    }
}
