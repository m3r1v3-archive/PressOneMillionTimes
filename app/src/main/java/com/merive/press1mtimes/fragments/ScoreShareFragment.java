package com.merive.press1mtimes.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;
import com.merive.press1mtimes.R;
import com.merive.press1mtimes.ScannerActivity;


public class ScoreShareFragment extends DialogFragment {

    ImageView code, scan;


    public ScoreShareFragment() {
    }

    public static com.merive.press1mtimes.fragments.ScoreShareFragment newInstance(String score) {
        com.merive.press1mtimes.fragments.ScoreShareFragment frag = new com.merive.press1mtimes.fragments.ScoreShareFragment();
        Bundle args = new Bundle();
        args.putString("score", score);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.score_share_fragment, container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        code = view.findViewById(R.id.QRCode);
        makeQRCode(getArguments().getString("score", "0"));

        scan = view.findViewById(R.id.scan);
        scan.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setBarcodeImageEnabled(false);
            integrator.setPrompt("Find P1MT QR-Code and Scan him");
            integrator.setCameraId(0);
            integrator.setCaptureActivity(ScannerActivity.class);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setBeepEnabled(false);
            integrator.setOrientationLocked(true);
            integrator.initiateScan();

            dismiss();
        });
    }

    public void makeQRCode(String score) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(score, BarcodeFormat.QR_CODE, 1024, 1024);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF202020 : Color.TRANSPARENT);
                }
            }
            code.setImageBitmap(bmp);
            code.setBackgroundResource(R.drawable.qr_code_background);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}

