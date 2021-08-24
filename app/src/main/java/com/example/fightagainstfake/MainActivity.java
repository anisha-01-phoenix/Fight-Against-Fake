package com.example.fightagainstfake;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.fightagainstfake.Posts.AddPosts;
import com.example.fightagainstfake.authentication.loginScreen;
import com.example.fightagainstfake.complaints.adapter;
import com.example.fightagainstfake.complaints.addComplaint;
import com.example.fightagainstfake.complaints.complaintStatus;
import com.example.fightagainstfake.complaints.model;
import com.example.fightagainstfake.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding activityMainBinding;

    ArrayList<model>data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(activityMainBinding.toolBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, activityMainBinding.drawerLayout, activityMainBinding.toolBar, R.string.open, R.string.close);
        activityMainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        activityMainBinding.navView.setNavigationItemSelectedListener(this);

data=new ArrayList<>();
        Intent intent = getIntent();
        String  check = intent.getStringExtra("check");

        if (check.equals("1")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.complaintContainer, new complaintStatus()).commit();

        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.complaintContainer, new addComplaint()).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_posts:
                startActivity(new Intent(MainActivity.this, AddPosts.class));
                break;
            case R.id.nav_info:
                break;
            case R.id.nav_maps:
                break;
            case R.id.nav_register_complain:
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.complaintContainer, new addComplaint()).commit();
                break;
            case R.id.nav_complaint_status:
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.complaintContainer, new complaintStatus()).commit();
                break;
            case R.id.nav_contact:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, "sceptre112358@gmail.com");
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