package com.merive.press1mtimes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {

    TextView github, twitter, reddit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setLinks();
    }

    @SuppressLint("ResourceAsColor")
    private void setLinks() {
        /* Set hyperlinks */
        /* Github */
        github = findViewById(R.id.github);
        github.setLinkTextColor(R.color.colorPrimaryDark);
        github.setMovementMethod(LinkMovementMethod.getInstance());
        /* Twitter */
        twitter = findViewById(R.id.twitter);
        twitter.setLinkTextColor(R.color.colorPrimaryDark);
        twitter.setMovementMethod(LinkMovementMethod.getInstance());
        /* Reddit */
        reddit = findViewById(R.id.reddit);
        reddit.setLinkTextColor(R.color.colorPrimaryDark);
        reddit.setMovementMethod(LinkMovementMethod.getInstance());
    }
}