package com.merive.pressonemilliontimes.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.merive.pressonemilliontimes.R;
import com.merive.pressonemilliontimes.activities.MainActivity;


public class UpdateFragment extends DialogFragment {

    EditText changelogText;
    Button downloadButton;
    MainActivity mainActivity;

    /**
     * Creates new instance of UpdateFragment that will be initialized with the given arguments
     *
     * @return New instance of UpdateFragment with necessary arguments
     */
    public static UpdateFragment newInstance(String oldVersion, String newVersion, String changelog, String link) {
        UpdateFragment frag = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString("old_version", oldVersion);
        args.putString("new_version", newVersion);
        args.putString("changelog", changelog);
        args.putString("link", link);
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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        getDialog().getWindow().getAttributes().windowAnimations = R.style.Theme_PressOneMillionTimes_Animation;

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
        mainActivity = ((MainActivity) getActivity());
    }

    /**
     * Sets button click listeners
     *
     * @see Button
     */
    private void setListeners() {
        downloadButton.setOnClickListener(v -> clickDownload());
    }

    /**
     * Sets text to versionText TextView
     */
    private void setChangelog() {
        changelogText.setText(String.format("Changelog (%s)\n%s", getArguments().getString("new_version"),
                getArguments().getString("changelog").replace("\\n", "\n")));
    }

    /**
     * Executes when clicking on downloadButton
     * Makes vibration and opens P1MT web page in browser
     */
    private void clickDownload() {
        mainActivity.makeVibration();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getArguments().getString("link"))));
        dismiss();
    }
}

