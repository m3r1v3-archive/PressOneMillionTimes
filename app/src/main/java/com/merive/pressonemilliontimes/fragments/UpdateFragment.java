package com.merive.pressonemilliontimes.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.merive.pressonemilliontimes.R;
import com.merive.pressonemilliontimes.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateFragment extends Fragment {

    EditText changelogText;
    Button downloadButton, cancelButton;
    MainActivity mainActivity;

    /**
     * Creates new instance of UpdateFragment that will be initialized with the given arguments
     *
     * @return New instance of UpdateFragment with necessary arguments
     */
    public static UpdateFragment newInstance(JSONObject jsonObject) throws JSONException {
        UpdateFragment frag = new UpdateFragment();

        Bundle args = new Bundle();
        args.putString("version", (String) jsonObject.get("version"));
        args.putString("changelog", (String) jsonObject.get("changelog"));
        args.putString("link", (String) jsonObject.get("link"));

        frag.setArguments(args);
        return frag;
    }

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
        return inflater.inflate(R.layout.fragment_update, parent);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view
     *
     * @param view               The View returned by onCreateView
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @see View
     * @see Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVariables();
        setListeners();
        setChangelog();
    }

    /**
     * Initializes basic layout components
     *
     * @see View
     */
    private void initVariables() {
        changelogText = getView().findViewById(R.id.update_changelog);
        downloadButton = getView().findViewById(R.id.update_download_button);
        cancelButton = getView().findViewById(R.id.update_cancel_button);
        mainActivity = ((MainActivity) getActivity());
    }

    /**
     * Sets button click listeners
     *
     * @see Button
     */
    private void setListeners() {
        downloadButton.setOnClickListener(v -> clickDownload());
        cancelButton.setOnClickListener(v -> clickCancel());
    }

    /**
     * Sets text to versionText TextView
     */
    private void setChangelog() {
        changelogText.setText(String.format("Changelog (%s)\n%s", getArguments().getString("version"),
                getArguments().getString("changelog").replace("\\n", "\n")));
    }

    /**
     * Executes when clicking on downloadButton
     * Makes vibration and opens P1MT web page in browser
     */
    private void clickDownload() {
        mainActivity.makeVibration();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getArguments().getString("link"))));
    }

    /**
     * Executes when clicking on cancelButton
     * Makes vibration effect and closes SplashMessageFragment
     */
    private void clickCancel() {
        mainActivity.makeVibration();
        mainActivity.setFragment(new SettingsFragment());
    }
}

