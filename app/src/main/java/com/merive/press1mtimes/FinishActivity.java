package com.merive.press1mtimes;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class FinishActivity extends AppCompatActivity {

    ImageButton close;
    Handler.Callback callback;
    TextView title, text, afterword;
    Boolean animationState, vibrationState, clicked = false;


    /**
     * This method is the start point at the FinishActivity.
     *
     * @param savedInstanceState Used by super.onCreate method.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_finish);

        initLayoutVariables();

        callback = message -> false;

        setStates();
    }

    /**
     * This method initializes layout components.
     */
    private void initLayoutVariables() {
        title = findViewById(R.id.finish_title);
        text = findViewById(R.id.congratulation_text);
        afterword = findViewById(R.id.afterword);
        close = findViewById(R.id.close);
    }

    /**
     * This method gets states in MainActivity and assigns to variables in FinishActivity.
     */
    private void setStates() {
        animationState = MainActivity.animationState;
        vibrationState = MainActivity.vibrationState;
    }

    /**
     * This method is setting visibility for Coins effect.
     *
     * @see com.jetradarmobile.snowfall.SnowfallView
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCoinsVisibility() {
        findViewById(R.id.coins).setVisibility(View.VISIBLE);
    }

    /**
     * This method executes after click on CloseButton.
     *
     * @param view View object.
     * @see android.widget.Button
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void clickClose(View view) {
        if (!clicked) {
            clicked = true;
            makeVibration();
            makeBreezeAnimation(title, text, afterword, close);
            setCoinsVisibility();
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }, 5000);
        }
    }

    /**
     * This method makes vibration.
     */
    private void makeVibration() {
        if (vibrationState) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(150L);
        }
    }

    private void makeBreezeAnimation(View... views) {
        for (View view : views)
            view.animate().scaleX(0.975f).scaleY(0.975f).setDuration(175).withEndAction(() -> view.animate().scaleX(1).scaleY(1).setDuration(175));
    }
}