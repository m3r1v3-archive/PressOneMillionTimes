package com.merive.press1mtimes.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;
import com.merive.press1mtimes.R;
import com.merive.press1mtimes.activities.MainActivity;
import com.merive.press1mtimes.activities.ScannerActivity;


public class ScoreShareFragment extends Fragment {

    ImageView QRCodeImage;
    Button scanButton, cancel;

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
        return inflater.inflate(R.layout.fragment_score_share, parent, false);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view
     * There initializes basic variables, sets button listeners and sets QRCode
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
        setQRCode();
    }

    /**
     * Initializes basic layout components
     *
     * @see View
     */
    private void initVariables() {
        QRCodeImage = getView().findViewById(R.id.score_share_qr_code);
        scanButton = getView().findViewById(R.id.score_share_scan_button);
        cancel = getView().findViewById(R.id.score_share_cancel_button);
    }

    /**
     * Sets button click listeners
     *
     * @see Button
     */
    private void setListeners() {
        scanButton.setOnClickListener(v -> clickScan());
        cancel.setOnClickListener(v -> clickCancel());
    }

    /**
     * Sets QR Code Bitmap image to QRCodeImage ImageView
     *
     * @see Bitmap
     * @see ImageView
     */
    private void setQRCode() {
        QRCodeImage.setImageBitmap(makeQRCode(String.valueOf(MainActivity.preferencesManager.getScore())));
    }

    /**
     * Generates QR Code by score value
     *
     * @param score Score string value
     * @return QR Code Bitmap image
     * @see Bitmap
     */
    private Bitmap makeQRCode(String score) {
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(getQRString(score), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                    pixels[y * width + x] = bitMatrix.get(x, y) ? 0xFF2C2C2C : Color.TRANSPARENT;
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
            return bmp;
        } catch (WriterException ignored) {
            return null;
        }
    }

    /**
     * Executes when clicking on scanButton
     * Makes vibration effect and opens QR Code scanner
     */
    private void clickScan() {
        ((MainActivity) getActivity()).makeVibration(1);
        openScanner();
        ((MainActivity) getActivity()).setFragment(new SettingsFragment());
    }

    /**
     * Executes when clicking on cancelButton
     * Makes vibration effect and closes ScoreShareFragment
     */
    private void clickCancel() {
        ((MainActivity) getActivity()).makeVibration(1);
        ((MainActivity) getActivity()).setFragment(new SettingsFragment());
    }

    /**
     * Opens QR Code scanner
     */
    private void openScanner() {
        new IntentIntegrator(getActivity())
                .setBarcodeImageEnabled(false)
                .setPrompt("Find and Scan Press1MTimes QR-Code")
                .setCameraId(0)
                .setCaptureActivity(ScannerActivity.class)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setBeepEnabled(false)
                .setOrientationLocked(true)
                .initiateScan();
    }

    /**
     * Makes necessary QR Code message
     *
     * @param score Score value for QR Code message
     * @return Necessary QR Code message
     */
    @SuppressLint("DefaultLocale")
    private String getQRString(String score) {
        return "press1mtimes://" + Integer.toHexString(Integer.parseInt(score));
    }

}

