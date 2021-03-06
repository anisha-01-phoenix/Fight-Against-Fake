package com.example.fightagainstfake.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fightagainstfake.MainActivity;
import com.example.fightagainstfake.Posts.Adapters.ViewPagerAdapter;
import com.example.fightagainstfake.Posts.Fragments.AdvertisementPostsFragment;
import com.example.fightagainstfake.Posts.Fragments.NormalPostsFragment;
import com.example.fightagainstfake.admin_package.dashboard;
import com.example.fightagainstfake.databinding.ActivityAddPostsBinding;

public class AddPosts extends AppCompatActivity {
    ActivityAddPostsBinding activityAddPostsBinding;
    int check;

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
        check = getIntent().getIntExtra("check", 0);
    }

    public void goBack(View view) {
        if (check == 0) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("check", check);

            startActivity(intent);
            // finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), dashboard.class);
            intent.putExtra("check", check);

            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (check == 0) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("check", check);

            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), dashboard.class);
            intent.putExtra("check", check);

            startActivity(intent);
        }

    }

}