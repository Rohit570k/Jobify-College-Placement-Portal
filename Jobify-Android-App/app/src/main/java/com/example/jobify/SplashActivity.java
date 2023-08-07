package com.example.jobify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView background = findViewById(R.id.imageView);
        ImageView logo = findViewById(R.id.imageView3);

        background.animate().translationX(-1600).setDuration(1000).setStartDelay(1500);
        logo.animate().translationX(1400).setDuration(1000).setStartDelay(1000);


        //FullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        new Handler().postDelayed(()->{
            startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
            finish();
        },1500);
    }
}