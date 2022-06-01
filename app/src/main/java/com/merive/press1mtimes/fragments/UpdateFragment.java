package com.merive.press1mtimes.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.merive.press1mtimes.R;
import com.merive.press1mtimes.activities.MainActivity;


public class UpdateFragment extends DialogFragment {

    TextView versionText;
    Button downloadButton;

    /**
     * Creates new instance of UpdateFragment that will be initialized with the given arguments
     *
     * @return New instance of UpdateFragment with necessary arguments
     */
    public static UpdateFragment newInstance(String oldVersion, String newVersion) {
        UpdateFragment frag = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString("old_version", oldVersion);
        args.putString("new_version", newVersion);
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
        getDialog().getWindow().getAttributes().windowAnimations = R.style.Theme_Press1MTimes_Animation;

        initVariables();
        setListeners();
        setVersion();
    }

    /**
     * Initializes basic layout components
     *
     * @see View
     */
    private void initVariables() {
        versionText = getView().findViewById(R.id.update_version_title);
        downloadButton = getView().findViewById(R.id.update_download_button);
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
    private void setVersion() {
        versionText.setText(("Installed - " + getArguments().getString("old_version") + " / Actual - " + getArguments().getString("new_version")));
    }

    /**
     * Executes when clicking on downloadButton
     * Makes vibration and opens P1MT web page in browser
     */
    private void clickDownload() {
        ((MainActivity) getActivity()).makeVibration(1);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.link))));
        dismiss();
    }
}

