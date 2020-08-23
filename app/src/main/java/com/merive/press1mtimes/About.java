package com.merive.press1mtimes;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Init Activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void exitClick(View view) {
        /* Finish Activity */
        finish();
    }
}