package com.example.fightagainstfake.Posts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fightagainstfake.Posts.Adapters.ViewPagerAdapter;
import com.example.fightagainstfake.Posts.Fragments.AdvertisementPostsFragment;
import com.example.fightagainstfake.Posts.Fragments.NormalPostsFragment;
import com.example.fightagainstfake.databinding.ActivityAddPostsBinding;

public class AddPosts extends AppCompatActivity {
    ActivityAddPostsBinding activityAddPostsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddPostsBinding = ActivityAddPostsBinding.inflate(getLayoutInflater());
        setContentView(activityAddPostsBinding.getRoot());
        getSupportActionBar().hide();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(new NormalPostsFragment(), "Posts");
        adapter.add(new AdvertisementPostsFragment(), "Advertisement");
        activityAddPostsBinding.viewPager.setAdapter(adapter);
        activityAddPostsBinding.tablayout.setupWithViewPager(activityAddPostsBinding.viewPager);

    }
}