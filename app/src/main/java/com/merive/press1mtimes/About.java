package com.merive.press1mtimes;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class About extends AppCompatActivity {

    Button exit;
    ImageView easter;
    Handler handler;
    Handler.Callback callback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Init Activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* Init variables */
        exit = findViewById(R.id.exit);
        easter = findViewById(R.id.easter_egg);

        /* Init callback for Handler */
        callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                return false;
            }
        };
    }

    public void exitClick(View view) {
        /* Set visibility exit & pascal */
        exit.setVisibility(View.INVISIBLE);
        easter.setVisibility(View.VISIBLE);

        /* Start easter egg animation */
        easter.animate().translationY(-100f).setDuration(200L).start();
        handler = new Handler(Objects.requireNonNull(Looper.myLooper()), callback);
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