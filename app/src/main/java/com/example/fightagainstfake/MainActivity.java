package com.example.fightagainstfake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.fightagainstfake.Posts.AddPosts;
import com.example.fightagainstfake.authentication.loginScreen;
import com.example.fightagainstfake.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(activityMainBinding.toolBar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,activityMainBinding.drawerLayout,activityMainBinding.toolBar,R.string.open,R.string.close);
        activityMainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        activityMainBinding.navView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_posts:
                startActivity(new Intent(MainActivity.this, AddPosts.class));
                break;
            case R.id.nav_info:
                break;
            case R.id.nav_maps:
                break;
            case R.id.nav_register_complain:
                break;
            case R.id.nav_complaint_status:
                break;
            case R.id.nav_contact:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL,"sceptre112358@gmail.com");
                intent.setType("message/*");
                Intent chooser = Intent.createChooser(intent, "Send Email");
                startActivity(chooser);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, loginScreen.class));
                finish();
                break;
        }
        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}