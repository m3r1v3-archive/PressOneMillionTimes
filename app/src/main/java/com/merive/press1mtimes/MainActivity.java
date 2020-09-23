package com.merive.press1mtimes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView counter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Init Activity
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Init scoreTV
        counter = findViewById(R.id.counter);
        /* Get score in storage */
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        String score = sharedPreferences.getString("score", "");
        // Add 0s for scoreTV
        while (score.length() != 6)
            score = "0" + score;
        // Start set scoreTV
        counter.setText(score);
    }

    public void buttonClick(View view) {
        /** Click red button **/
        int score = Integer.parseInt(String.valueOf(counter.getText()));
        /* If score == 999999, them scoreTV = "000000" & make Toast */
        if (score == 999999) {
            /* Fix bug #1 (Check GitHub Issues) */
            sharedPreferences.edit().putString("score", "000000").apply();
            counter.setText("000000");
            /* Switch activity */
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        } else {
            // Update score
            score += 1;
            // Format score
            String result = String.format("%06d", score);
            // Edit score in storage
            sharedPreferences.edit().putString("score", result).apply();
            /* Set score in scoreTV */
            counter.setText(result);
        }
    }

}