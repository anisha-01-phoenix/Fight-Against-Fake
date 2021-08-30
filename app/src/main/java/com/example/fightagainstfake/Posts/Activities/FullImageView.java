package com.example.fightagainstfake.Posts.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fightagainstfake.R;
import com.github.chrisbanes.photoview.PhotoView;

public class FullImageView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view);
        getSupportActionBar().hide();
        String image = getIntent().getStringExtra("zoom");
        PhotoView photoView = findViewById(R.id.photo_view);
        Glide.with(this).load(image).into(photoView);

    }
}