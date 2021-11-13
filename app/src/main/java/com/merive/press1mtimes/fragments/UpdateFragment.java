package com.merive.press1mtimes.fragments;

import android.annotation.SuppressLint;
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

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;


public class UpdateFragment extends DialogFragment {

    TextView version;
    Button download;

    public UpdateFragment() {
        /* Empty constructor (Needs) */
    }

    public static UpdateFragment newInstance(String oldVersion, String newVersion) {
        /* newInstance method */
        UpdateFragment frag = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString("oldVersion", oldVersion);
        args.putString("newVersion", newVersion);
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
        return inflater.inflate(R.layout.update_fragment, container);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        initVariables(view);

        setVersion();

        download.setOnClickListener(v -> {
            clickDownload();
        });
    }

    /* ************ */
    /* Init methods */
    /* ************ */

    public void initVariables(View view) {
        /* Init main variables */
        version = view.findViewById(R.id.version);
        download = view.findViewById(R.id.download);
    }

    /* *********** */
    /* Set methods */
    /* *********** */

    public void setVersion() {
        version.setText(("Download: " + getArguments().getString("oldVersion") + " → " +
                getArguments().getString("newVersion")));
    }

    /* ************* */
    /* Click methods */
    /* ************* */

    public void clickDownload() {
        ((MainActivity) getActivity()).makeVibration(1);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.link)));
        startActivity(browserIntent);
        dismiss();
    }
}

