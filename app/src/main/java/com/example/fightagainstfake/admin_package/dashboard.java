package com.example.fightagainstfake.admin_package;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.fightagainstfake.MainActivity;
import com.example.fightagainstfake.Posts.AddPosts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.fightagainstfake.R;
import com.example.fightagainstfake.authentication.Startscreen;
import com.example.fightagainstfake.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;


public class dashboard extends AppCompatActivity {
    ActivityDashboardBinding binding;

    ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        setSupportActionBar(binding.toolbar);


        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer, new complaint_home()).commit();
        binding.logo00.setText("COMPLAINTS");
        toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close);

        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        binding.navmenu1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.complaints:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer, new complaint_home()).commit();
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.advert_admin:

                        Intent intent=new Intent(dashboard.this,AddPosts.class);
                        intent.putExtra("check",1);
                        startActivity(intent);
                        /*binding.logo00.setText("ADVRTISEMENTS");
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,new post_home()).commit();*/
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout2:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(dashboard.this, Startscreen.class));
                        finish();

                        
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;


                }
                return true;
            }
        });
    }


}