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
     * Called by the system when the service is first created
     *
     * @param savedInstanceState Using by super.onCreate method
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.breath_in, R.anim.breath_out);
        setContentView(R.layout.activity_finish);

        initLayoutVariables();

        callback = message -> false;

        setStates();
    }

    /**
     * Initializes basic layout components
     */
    private void initLayoutVariables() {
        title = findViewById(R.id.finish_title);
        text = findViewById(R.id.congratulation_text);
        afterword = findViewById(R.id.afterword);
        close = findViewById(R.id.close);
    }

    /**
     * Gets settings states values from MainActivity
     * animationState needs for making animation after close button clicking
     * vibrationState needs for making vibration after close button clicking
     */
    private void setStates() {
        animationState = MainActivity.animationState;
        vibrationState = MainActivity.vibrationState;
    }

    /**
     * Makes visible coins rain effect
     *
     * @see com.jetradarmobile.snowfall.SnowfallView
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCoinsVisibility() {
        findViewById(R.id.coins).setVisibility(View.VISIBLE);
    }

    /**
     * Executes after close button clicking
     * Sets the click state (required to remove the finish() multiple execution bug)
     * Makes final animation, closes FinishActivity and starts MainActivity
     *
     * @param view View object.
     * @see android.widget.Button
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void clickClose(View view) {
        if (!clicked) {
            clicked = true;
            makeVibration();
            makeBreathAnimation(title, text, afterword, close);
            setCoinsVisibility();
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }, 5000);
        }
    }

    /**
     * Makes vibration effect
     */
    private void makeVibration() {
        if (vibrationState) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(150L);
        }
    }

    /**
     * Makes BreathAnimation for views
     *
     * @param views Views what will be animated
     */
    private void makeBreathAnimation(View... views) {
        for (View view : views)
            view.animate().scaleX(0.975f).scaleY(0.975f).setDuration(175).withEndAction(() -> view.animate().scaleX(1).scaleY(1).setDuration(175));
    }
}