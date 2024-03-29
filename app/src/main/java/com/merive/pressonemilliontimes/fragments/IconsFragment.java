package com.merive.pressonemilliontimes.fragments;

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

import com.merive.pressonemilliontimes.R;
import com.merive.pressonemilliontimes.activities.MainActivity;
import com.merive.pressonemilliontimes.utils.Icons;

public class IconsFragment extends Fragment {

    ConstraintLayout defaultIconButton, OneIconButton, millionIconButton;
    Button cancelButton;
    MainActivity mainActivity;

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
        OneIconButton = getView().findViewById(R.id.one_icon_button);
        millionIconButton = getView().findViewById(R.id.minimalistic_icon_button);
        cancelButton = getView().findViewById(R.id.icons_cancel_button);
        mainActivity = ((MainActivity) getActivity());
    }

    /**
     * This method sets click listeners for OptionsFragment buttons
     */
    private void setListeners() {
        defaultIconButton.setOnClickListener(v -> clickDefaultIcon());
        OneIconButton.setOnClickListener(v -> clickOneIcon());
        millionIconButton.setOnClickListener(v -> clickMinimalisticIcon());
        cancelButton.setOnClickListener(v -> clickCancel());
    }

    /**
     * Executes after click on defaultIconButton
     * Makes vibration and updates application icon to default icon
     */
    private void clickDefaultIcon() {
        mainActivity.makeVibration();
        setIcon(Icons.DEFAULT.getValue());
        mainActivity.setFragment(new SettingsFragment());
    }

    /**
     * Executes after click on OneIconButton
     * Makes vibration and updates application icon to One icon
     */
    private void clickOneIcon() {
        mainActivity.makeVibration();
        setIcon(Icons.ONE.getValue());
        mainActivity.setFragment(new SettingsFragment());
    }

    /**
     * Executes after click on MinimalisticIconButton
     * Makes vibration and updates application icon to Minimalistic icon
     **/
    private void clickMinimalisticIcon() {
        mainActivity.makeVibration();
        setIcon(Icons.MINIMALISTIC.getValue());
        mainActivity.setFragment(new SettingsFragment());
    }

    /**
     * Executes when clicking on cancelButton
     * Makes vibration effect and closes IconsFragment
     */
    private void clickCancel() {
        mainActivity.makeVibration();
        mainActivity.setFragment(new SettingsFragment());
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
        mainActivity.makeToast(getResources().getString(R.string.icon_changed));
    }

    /**
     * Gets current icon from SharedPreference and disables it
     */
    private void disablePreviousIcon() {
        if (MainActivity.preferencesManager.getIcon() == Icons.DEFAULT.getValue())
            disableIcon("com.merive.pressonemilliontimes.activities.SplashActivity");
        else if (MainActivity.preferencesManager.getIcon() == Icons.ONE.getValue())
            disableIcon("com.merive.pressonemilliontimes.One");
        else if (MainActivity.preferencesManager.getIcon() == Icons.MINIMALISTIC.getValue())
            disableIcon("com.merive.pressonemilliontimes.Minimalistic");
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
            enableIcon("com.merive.pressonemilliontimes.activities.SplashActivity");
        else if (iconName == Icons.ONE.getValue())
            enableIcon("com.merive.pressonemilliontimes.One");
        else if (iconName == Icons.MINIMALISTIC.getValue())
            enableIcon("com.merive.pressonemilliontimes.Minimalistic");
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
