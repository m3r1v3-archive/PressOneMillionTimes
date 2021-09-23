package com.merive.press1mtimes.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;
import com.merive.press1mtimes.MainActivity;
import com.merive.press1mtimes.R;
import com.merive.press1mtimes.ScannerActivity;


public class ScoreShareFragment extends DialogFragment {

    ImageView code;
    Button scan;


    public ScoreShareFragment() {
        /* Empty constructor (Needs) */
    }

    public static com.merive.press1mtimes.fragments.ScoreShareFragment newInstance(String score) {
        /* newInstance method */
        com.merive.press1mtimes.fragments.ScoreShareFragment frag = new com.merive.press1mtimes.fragments.ScoreShareFragment();
        Bundle args = new Bundle();
        args.putString("score", score);
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
        return inflater.inflate(R.layout.score_share_fragment, container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVariables(view);

        makeQRCode(getArguments().getString("score", "0"));

        scan.setOnClickListener(v -> {
            clickScan();
        });
    }

    /* ************ */
    /* Init methods */
    /* ************ */

    public void initVariables(View view) {
        /* Init main variables */
        code = view.findViewById(R.id.QRCode);
        scan = view.findViewById(R.id.scan);
    }

    /* ************ */
    /* Make methods */
    /* ************ */

    public void makeQRCode(String score) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(encryptScore(score), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? 0xFF2C2C2C : Color.TRANSPARENT;
                }
            }
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
            code.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /* ************* */
    /* Click methods */
    /* ************* */

    public void clickScan() {
        /* Click Scan Button */
        ((MainActivity) getActivity()).makeVibration(1);
        openScanner();
        dismiss();
    }

    /* *************** */
    /* Another methods */
    /* *************** */

    public void openScanner() {
        /* Open ScannerActivity */
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setBarcodeImageEnabled(false);
        integrator.setPrompt("Find P1MT QR-Code and Scan him");
        integrator.setCameraId(0);
        integrator.setCaptureActivity(ScannerActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setBeepEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @SuppressLint("DefaultLocale")
    public String encryptScore(String result) {
        /* Encrypt data from QR result */
        return "P1MT:" + "(" + String.format("%06d", Integer.parseInt(result)).substring(0, 3) + ")" + "(" +
                String.format("%06d", Integer.parseInt(result)).substring(3) + ")";
    }
}

