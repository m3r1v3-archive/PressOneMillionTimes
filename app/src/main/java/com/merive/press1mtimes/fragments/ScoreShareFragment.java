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

    /**
     * ScoreShareFragment constructor.
     */
    public ScoreShareFragment() {
    }

    /**
     * This method returns ScoreShareFragment object.
     *
     * @return ScoreShareFragment object.
     */
    public static ScoreShareFragment newInstance(String score) {
        ScoreShareFragment frag = new ScoreShareFragment();
        Bundle args = new Bundle();
        args.putString("score", score);
        frag.setArguments(args);
        return frag;
    }

    /**
     * This method executes when ScoreShareFragment is creating.
     *
     * @param inflater           Needs for getting Fragment View.
     * @param parent             Argument of inflater.inflate().
     * @param savedInstanceState Save Fragment Values.
     * @return Fragment View.
     * @see View
     * @see Bundle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.score_share_fragment, parent);
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
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        initVariables();

        code.setImageBitmap(makeQRCode(getArguments().getString("score")));
        scan.setOnClickListener(v -> clickScan());
    }

    /**
     * This method initializes layout variables.
     *
     * @see View
     */
    private void initVariables() {
        code = getView().findViewById(R.id.score_share_qr_code);
        scan = getView().findViewById(R.id.score_share_scan_button);
    }

    /**
     * This method generates QR-Code by score value.
     *
     * @param score Score value.
     * @return QR-Code Bitmap image.
     * @see Bitmap
     */
    private Bitmap makeQRCode(String score) {
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(encryptScore(score), BarcodeFormat.QR_CODE, 512, 512);
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
     * This method executes after click on Scan button.
     * The method makes vibration and opens QR scanner.
     */
    private void clickScan() {
        ((MainActivity) getActivity()).makeVibration(1);
        openScanner();
        dismiss();
    }

    /**
     * This method opens QR scanner.
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
     * This method encrypts Score value.
     *
     * @param score Score value.
     * @return Encrypted score value.
     */
    @SuppressLint("DefaultLocale")
    private String encryptScore(String score) {
        return "P1MT:" + "(" + String.format("%06d", Integer.parseInt(score)).substring(0, 3) + ")" + "(" +
                String.format("%06d", Integer.parseInt(score)).substring(3) + ")";
    }
}

