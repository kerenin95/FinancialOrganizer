package com.example.financialorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {

    private static int START = 3000;
    private ImageView imageView;
    private TextView appName;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.start_screen);

        animation = AnimationUtils.loadAnimation(this, R.anim.animation);

        imageView = findViewById(R.id.imageView);
        appName = findViewById(R.id.appName);

        imageView.setAnimation(animation);
        appName.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, START);
    }
}