package com.example.fightagainstfake;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.fightagainstfake.authentication.loginScreen;
import com.example.fightagainstfake.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
        ActivitySplashBinding activitySplashBinding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            activitySplashBinding=ActivitySplashBinding.inflate(getLayoutInflater());
            setContentView(activitySplashBinding.getRoot());
            getSupportActionBar().hide();

            ObjectAnimator objectAnimator=ObjectAnimator.ofPropertyValuesHolder(activitySplashBinding.logo,
                    PropertyValuesHolder.ofFloat("scaleX",1.2f),
                    PropertyValuesHolder.ofFloat("scaleY",1.2f));
            objectAnimator.setDuration(500);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.start();
            final Intent i=new Intent(SplashActivity.this, loginScreen.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    finish();

                }
            },2000);
        }


}