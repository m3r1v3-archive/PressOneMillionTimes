package com.merive.press1mtimes.activities;

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

import com.merive.press1mtimes.R;

public class FinishActivity extends AppCompatActivity {

    ImageButton close;
    TextView title, text, afterword;
    boolean clicked = false;


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
            if (MainActivity.preferencesManager.getAnimation())
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
        if (MainActivity.preferencesManager.getVibration()) {
            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(75L);
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