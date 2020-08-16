package com.merive.press1mtimes;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView scoreTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreTV = findViewById(R.id.score);
    }

    public void buttonClick(View view) {
        int score = Integer.parseInt(String.valueOf(scoreTV.getText()));
        if (score == 999999) {
            scoreTV.setText("000000");
        } else {
            score += 1;
            String result = String.valueOf(score);
            while (result.length() != 6) {
                result = "0" + result;
            }
            scoreTV.setText(result);
        }
    }
}