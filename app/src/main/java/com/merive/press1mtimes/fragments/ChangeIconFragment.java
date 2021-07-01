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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class ChangeIconFragment extends DialogFragment {

    ImageView defaultIcon, P1MTIcon, classicIcon;

    ChangeIconFragment() {
        /* Empty constructor (Needs) */
    }

    public static ChangeIconFragment newInstance() {
        /* newInstance method */
        ChangeIconFragment frag = new ChangeIconFragment();
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
        return inflater.inflate(R.layout.change_icon_fragment, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVariables(view);

        defaultIcon.setOnClickListener(v -> {
            clickDefaultIcon();
        });

        P1MTIcon.setOnClickListener(v -> {
            clickP1MTIcon();
        });

        classicIcon.setOnClickListener(v -> {
            clickClassicIcon();
        });
    }

    /* ************ */
    /* Init methods */
    /* ************ */

    public void initVariables(View view) {
        /* Init main variables */
        defaultIcon = view.findViewById(R.id.QRCode);
        P1MTIcon = view.findViewById(R.id.P1MTIcon);
        classicIcon = view.findViewById(R.id.classicIcon);
    }

    /* ************* */
    /* Click methods */
    /* ************* */

    public void clickDefaultIcon() {
        /* Click Default Icon */
        setIcon("default");
        dismiss();
    }

    public void clickP1MTIcon() {
        /* Click P1MT Icon */
        setIcon("P1MT");
        dismiss();
    }

    public void clickClassicIcon() {
        /* Click Classic Icon */
        setIcon("Classic");
        dismiss();
    }

    /* *************** */
    /* Another methods */
    /* *************** */

    public void setIcon(String iconName) {
        disableOldIcon();
        turnOnIcon(iconName);
        ((MainActivity) getActivity()).changeIcon(iconName);
        ((MainActivity) getActivity()).makeToast("Icon was changed.");
    }

    public void disableOldIcon() {
        /* Disable old application icon */
        PackageManager packageManager = getActivity().getPackageManager();
        switch (((MainActivity) getActivity()).getApplicationIcon()) {
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
        /* Turn on new Icon */
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
}
