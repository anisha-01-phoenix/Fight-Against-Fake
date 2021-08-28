package com.example.fightagainstfake;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.fightagainstfake.authentication.Startscreen;
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
            changeColor(R.color.additionalColor);

            ObjectAnimator objectAnimator=ObjectAnimator.ofPropertyValuesHolder(activitySplashBinding.logo,
                    PropertyValuesHolder.ofFloat("scaleX",1.2f),
                    PropertyValuesHolder.ofFloat("scaleY",1.2f));
            objectAnimator.setDuration(500);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.start();
            final Intent i=new Intent(SplashActivity.this, Startscreen.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    finish();

                }
            },2000);





        }


    public void changeColor(int resourcecolor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), resourcecolor));
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(resourcecolor)));

    }
}