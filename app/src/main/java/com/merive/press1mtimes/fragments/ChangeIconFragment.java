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

    /**
     * ChangeIcon constructor.
     */
    ChangeIconFragment() {
    }

    /**
     * This method returns ChangeIconFragment object.
     *
     * @return ChangeIconFragment object.
     */
    public static ChangeIconFragment newInstance() {
        return new ChangeIconFragment();
    }

    /**
     * This method executes when ChangeIconFragment is creating.
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
        return inflater.inflate(R.layout.change_icon_fragment, parent);
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

        defaultIcon.setOnClickListener(v -> clickDefaultIcon());
        P1MTIcon.setOnClickListener(v -> clickP1MTIcon());
        classicIcon.setOnClickListener(v -> clickClassicIcon());
    }

    /**
     * This method initializes layout variables.
     *
     * @see View
     */
    private void initVariables() {
        defaultIcon = getView().findViewById(R.id.default_icon_button);
        P1MTIcon = getView().findViewById(R.id.short_icon_button);
        classicIcon = getView().findViewById(R.id.classic_icon_button);
    }

    /**
     * This method executes after click on Default Icon Button.
     * The method makes vibration and sets default application icon.
     */
    private void clickDefaultIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        setIcon("default");
        dismiss();
    }

    /**
     * This method executes after click on P1MT Icon Button.
     * The method makes vibration and sets P1MT application icon.
     */
    private void clickP1MTIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        setIcon("short");
        dismiss();
    }

    /**
     * This method executes after clicking on Classic Icon Button.
     * The method makes vibration and sets Classic application icon.
     */
    private void clickClassicIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        setIcon("classic");
        dismiss();
    }

    /**
     * This method sets application icon.
     * The method disables old icon, sets new icon, changes icon name in MainActivity and makes Toast.
     *
     * @param iconName Name of icon.
     */
    private void setIcon(String iconName) {
        disableOldIcon();
        setNewIcon(iconName);
        ((MainActivity) getActivity()).changeIcon(iconName);
        ((MainActivity) getActivity()).makeToast(getResources().getString(R.string.icon_changed));
    }

    /**
     * This method disables old application icon.
     */
    private void disableOldIcon() {
        PackageManager packageManager = getActivity().getPackageManager();
        switch (((MainActivity) getActivity()).getApplicationIcon()) {
            case "default":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.SplashActivity"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                break;
            case "short":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.Short"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                break;
            case "classic":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.Classic"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                break;
        }
    }

    /**
     * This method sets new application icon by icon name.
     *
     * @param iconName Name of icon.
     */
    private void setNewIcon(String iconName) {
        PackageManager packageManager = getActivity().getPackageManager();
        switch (iconName) {
            case "default":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.SplashActivity"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                break;
            case "short":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.Short"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                break;
            case "classic":
                packageManager.setComponentEnabledSetting(new ComponentName(getContext(), "com.merive.press1mtimes.Classic"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                break;
        }
    }
}
