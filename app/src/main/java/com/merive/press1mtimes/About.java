package com.merive.press1mtimes;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {

    Button exit;
    ImageView easter;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Init Activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* Init variables */
        exit = findViewById(R.id.exit);
        easter = findViewById(R.id.pascal);
    }

    public void exitClick(View view) {
        /* Set visibility exit & pascal */
        exit.setVisibility(View.INVISIBLE);
        easter.setVisibility(View.VISIBLE);

        /* Start easter egg animation */
        easter.animate().translationY(-100f).setDuration(200L).start();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                easter.animate().translationY(100f).setDuration(200L).start();
            }
        }, 300);

        /* Finish layout */
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 500);
    }
}