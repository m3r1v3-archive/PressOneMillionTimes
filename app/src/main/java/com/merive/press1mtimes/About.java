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
    ImageView pascal;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Init Activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* Init variables */
        exit = findViewById(R.id.exit);
        pascal = findViewById(R.id.pascal);
    }

    public void exitClick(View view) {
        /* Set visibility exit & pascal */
        exit.setVisibility(View.INVISIBLE);
        pascal.setVisibility(View.VISIBLE);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 10);
    }
}