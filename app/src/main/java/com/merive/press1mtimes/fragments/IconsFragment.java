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

import com.merive.press1mtimes.R;
import com.merive.press1mtimes.activities.MainActivity;
import com.merive.press1mtimes.utils.Icons;

public class IconsFragment extends Fragment {

    ConstraintLayout defaultIconButton, ShortIconButton, millionIconButton;
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
        return inflater.inflate(R.layout.fragment_icons, parent, false);
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
        defaultIconButton = getView().findViewById(R.id.default_icon_button);
        ShortIconButton = getView().findViewById(R.id.short_icon_button);
        millionIconButton = getView().findViewById(R.id.million_icon_button);
        cancelButton = getView().findViewById(R.id.icons_cancel_button);
    }

    /**
     * This method sets click listeners for OptionsFragment buttons
     */
    private void setListeners() {
        defaultIconButton.setOnClickListener(v -> clickDefaultIcon());
        ShortIconButton.setOnClickListener(v -> clickShortIcon());
        millionIconButton.setOnClickListener(v -> clickMillionIcon());
        cancelButton.setOnClickListener(v -> clickCancel());
    }

    /**
     * Executes after click on defaultIconButton
     * Makes vibration and updates application icon to default icon
     */
    private void clickDefaultIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        setIcon(Icons.DEFAULT.getValue());
        ((MainActivity) getActivity()).setFragment(new SettingsFragment());
    }

    /**
     * Executes after click on ShortIconButton
     * Makes vibration and updates application icon to Short icon
     */
    private void clickShortIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        setIcon(Icons.SHORT.getValue());
        ((MainActivity) getActivity()).setFragment(new SettingsFragment());
    }

    /**
     * Executes after click on MillionIconButton
     * Makes vibration and updates application icon to Million icon
     **/
    private void clickMillionIcon() {
        ((MainActivity) getActivity()).makeVibration(1);
        setIcon(Icons.MILLION.getValue());
        ((MainActivity) getActivity()).setFragment(new SettingsFragment());
    }

    /**
     * Executes when clicking on cancelButton
     * Makes vibration effect and closes IconsFragment
     */
    private void clickCancel() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).setFragment(new SettingsFragment());
    }

    /**
     * Sets application icon
     * Disables old icon, sets new icon, changes icon name in MainActivity and makes toast
     *
     * @param iconName Name of icon.
     */
    private void setIcon(int iconName) {
        disablePreviousIcon();
        setNewIcon(iconName);
        MainActivity.preferencesManager.setIcon(iconName);
        ((MainActivity) getActivity()).makeToast(getResources().getString(R.string.icon_changed));
    }

    /**
     * Gets current icon from SharedPreference and disables it
     */
    private void disablePreviousIcon() {
        if (MainActivity.preferencesManager.getIcon() == Icons.DEFAULT.getValue())
            disableIcon("com.merive.press1mtimes.activities.SplashActivity");
        else if (MainActivity.preferencesManager.getIcon() == Icons.SHORT.getValue())
            disableIcon("com.merive.press1mtimes.Short");
        else if (MainActivity.preferencesManager.getIcon() == Icons.MILLION.getValue())
            disableIcon("com.merive.press1mtimes.Million");
    }

    /**
     * Disables previous icon using package name
     *
     * @param cls Icon package name
     */
    private void disableIcon(String cls) {
        getActivity().getPackageManager().setComponentEnabledSetting(new ComponentName(getContext(), cls), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * Gets new icon package name and enable it
     *
     * @param iconName Name of icon
     */
    private void setNewIcon(int iconName) {
        if (iconName == Icons.DEFAULT.getValue())
            enableIcon("com.merive.press1mtimes.activities.SplashActivity");
        else if (iconName == Icons.SHORT.getValue())
            enableIcon("com.merive.press1mtimes.Short");
        else if (iconName == Icons.MILLION.getValue())
            enableIcon("com.merive.press1mtimes.Million");
    }

    /**
     * Enables new icon using package name
     *
     * @param cls Icon package name
     */
    private void enableIcon(String cls) {
        getActivity().getPackageManager().setComponentEnabledSetting(new ComponentName(getContext(), cls), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
