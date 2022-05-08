package com.merive.press1mtimes.fragments;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class IconsFragment extends Fragment {

    ConstraintLayout defaultIconButton, P1MTIconButton, classicIconButton;
    Button cancelButton;

    /**
     * Called to have the fragment instantiate its user interface view
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param parent             If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return Return the View for the fragment's UI, or null
     * @see View
     * @see Bundle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.icons_fragment, parent, false);
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

        initVariables();
        setListeners();
    }

    /**
     * Initializes basic layout components
     *
     * @see View
     */
    private void initVariables() {
        defaultIconButton = getView().findViewById(R.id.default_icon);
        P1MTIconButton = getView().findViewById(R.id.short_icon);
        classicIconButton = getView().findViewById(R.id.classic_icon);
        cancelButton = getView().findViewById(R.id.icons_cancel_button);
    }

    /**
     * This method sets click listeners for OptionsFragment buttons
     */
    private void setListeners() {
        defaultIconButton.setOnClickListener(v -> clickDefaultIcon());
        P1MTIconButton.setOnClickListener(v -> clickP1MTIcon());
        classicIconButton.setOnClickListener(v -> clickClassicIcon());
        cancelButton.setOnClickListener(v -> clickCancel());
    }

    /**
     * Executes after click on defaultIconButton
     * Makes vibration and updates application icon to default icon
     */
    private void clickDefaultIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        setIcon("default");
        ((MainActivity) getActivity()).initSettingsFragment();
    }

    /**
     * Executes after click on P1MTIconButton
     * Makes vibration and updates application icon to P1MT icon
     */
    private void clickP1MTIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        setIcon("short");
        ((MainActivity) getActivity()).initSettingsFragment();
    }

    /**
     * Executes after click on ClassicIconButton
     * Makes vibration and updates application icon to Classic icon
     **/
    private void clickClassicIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        setIcon("classic");
        ((MainActivity) getActivity()).initSettingsFragment();
    }

    /**
     * Executes when clicking on cancelButton
     * Makes vibration effect and closes IconsFragment
     */
    private void clickCancel() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).initSettingsFragment();
    }

    /**
     * Sets application icon
     * Disables old icon, sets new icon, changes icon name in MainActivity and makes toast
     *
     * @param iconName Name of icon.
     */
    private void setIcon(String iconName) {
        disablePreviousIcon();
        setNewIcon(iconName);
        ((MainActivity) getActivity()).changeIcon(iconName);
        ((MainActivity) getActivity()).makeToast(getResources().getString(R.string.icon_changed));
    }

    /**
     * Disables previous icon
     */
    private void disablePreviousIcon() {
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
     * Sets new application icon by icon name
     *
     * @param iconName Name of icon
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
