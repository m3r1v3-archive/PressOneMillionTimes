package com.merive.press1mtimes;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView scoreTV;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        scoreTV = findViewById(R.id.score);
        // Get score
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        String score = sharedPreferences.getString("score", "");
        // Add 0s for scoreTV
        while (score.length() != 6)
            score = "0" + score;
        // Start set scoreTV
        scoreTV.setText(score);
    }

    public void buttonClick(View view) {
        // Click red button
        int score = Integer.parseInt(String.valueOf(scoreTV.getText()));

        if (score == 999999) {
            Toast.makeText(MainActivity.this, "You press button 1M times...",
                    Toast.LENGTH_LONG).show();
            scoreTV.setText("000000");
        } else {
            score += 1;
            String result = String.valueOf(score);
            while (result.length() != 6) {
                result = "0" + result;
            }
            sharedPreferences.edit().putString("score", result).apply();
            scoreTV.setText(result);
        }
    }

}