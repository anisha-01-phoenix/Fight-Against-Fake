package com.example.fightagainstfake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(new SlideFragmentBuilder()
                .title("Fight Against Fake")
                .buttonsColor(R.color.white)
                .image(R.drawable.logo)
                .backgroundColor(R.color.additionalColor)
                .build());

        addSlide(new SlideFragmentBuilder()
                .title("Fight Against Fake")
                .buttonsColor(R.color.white)
                .image(R.drawable.logo)
                .backgroundColor(R.color.themeColor)
                .build());

        addSlide(new SlideFragmentBuilder()
                .title("Fight Against Fake")
                .buttonsColor(R.color.white)
                .image(R.drawable.logo)
                .backgroundColor(R.color.white)
                .build());
    }
}