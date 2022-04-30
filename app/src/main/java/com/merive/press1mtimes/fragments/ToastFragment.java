package com.merive.press1mtimes.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;

public class ToastFragment extends Fragment {

    TextView messageText;

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
        return inflater.inflate(R.layout.toast_fragment, parent, false);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view
     * Initializes basic components and shows toast messages
     *
     * @param view               The View returned by onCreateView
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @see View
     * @see Bundle
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initVariables();
        showToast();
    }

    /**
     * Initializes basic layout components
     *
     * @see View
     */
    private void initVariables() {
        messageText = getView().findViewById(R.id.toast_message);
    }

    /**
     * Sets text value to messageText TextView
     */
    private void setText(String text) {
        messageText.setText(text);
    }

    /**
     * Shows toast messages if messages contains in toastMessages LinkedList in MainActivity
     *
     * @see MainActivity
     */
    private void showToast() {
        try {
            if (MainActivity.toastMessages.isEmpty()) {
                ((MainActivity) getActivity()).removeToast();
            } else {
                setText(MainActivity.toastMessages.getFirst());
                MainActivity.toastMessages.removeFirst();
                new Handler().postDelayed(() -> showToast(), 3000);
            }
        } catch (NullPointerException ignored) {
        }
    }
}
