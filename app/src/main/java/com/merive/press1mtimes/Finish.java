package com.merive.press1mtimes;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Finish extends AppCompatActivity {

    ImageButton exit;
    ImageView easter;
    Handler handler;
    Handler.Callback callback;
    TextView title, label, footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        exit = findViewById(R.id.exit);
        easter = findViewById(R.id.easter_egg);
        title = findViewById(R.id.title);
        label = findViewById(R.id.label);
        footer = findViewById(R.id.footer);

        /* Init callback for Handler */
        callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                return false;
            }
        };
    }

    public void exitClick(View view) {
        exit.setVisibility(View.INVISIBLE);
        easter.setVisibility(View.VISIBLE);

        easterAnim(easter);

        /* Finish this layout */
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 500);

    }

    private void easterAnim(final View view) {
        view.animate().translationY(-100f).setDuration(200L).start();
        handler = new Handler(Objects.requireNonNull(Looper.myLooper()), callback);
        handler.postDelayed(new Runnable() {
            public void run() {
                view.animate().translationY(75f).setDuration(200L).start();
            }
        }, 300);
    }
}
