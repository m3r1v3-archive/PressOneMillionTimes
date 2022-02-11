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

    /**
     * OptionsFragment constructor.
     */
    public UpdateFragment() {
    }

    /**
     * This method returns OptionsFragment object with necessary arguments.
     *
     * @return OptionsFragment object.
     */
    public static UpdateFragment newInstance(String oldVersion, String newVersion) {
        UpdateFragment frag = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString("oldVersion", oldVersion);
        args.putString("newVersion", newVersion);
        frag.setArguments(args);
        return frag;
    }

    /**
     * This method executes when OptionsFragment is creating.
     *
     * @param inflater           Needs for getting Fragment View.
     * @param parent             Argument of inflater.inflate().
     * @param savedInstanceState Save Fragment Values.
     * @return Fragment View.
     * @see View
     * @see Bundle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.update_fragment, parent);
    }

    /**
     * This method executes after Fragment View has been created.
     *
     * @param view               Fragment View Value.
     * @param savedInstanceState Saving Fragment Values.
     * @see View
     * @see Bundle
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        initVariables();
        setVersion();

        download.setOnClickListener(v -> clickDownload());
    }

    /**
     * This method initializes layout variables.
     *
     * @see View
     */
    private void initVariables() {
        version = getView().findViewById(R.id.update_version_title);
        download = getView().findViewById(R.id.update_button);
    }

    /**
     * This method sets text to version TextView.
     */
    private void setVersion() {
        version.setText(("Download: " + getArguments().getString("oldVersion") + " → " +
                getArguments().getString("newVersion")));
    }

    /**
     * This method executes after click on Download button.
     * The method makes vibration and opens P1MT page in browser.
     */
    private void clickDownload() {
        ((MainActivity) getActivity()).makeVibration(1);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.link))));
        dismiss();
    }
}

