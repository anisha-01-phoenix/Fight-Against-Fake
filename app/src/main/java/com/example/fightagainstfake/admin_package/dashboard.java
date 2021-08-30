package com.example.fightagainstfake.admin_package;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.example.fightagainstfake.R;
import com.example.fightagainstfake.authentication.Startscreen;
import com.example.fightagainstfake.databinding.ActivityDashboardBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


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

        changeColor(R.color.themeColor);
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


                    case R.id.admin_info:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer, new admin_info_corner()).commit();
                        binding.logo00.setText("INFO CORNER");
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.logout2:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(dashboard.this, Startscreen.class));
                        finish();


                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_contact:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, "sceptre112358@gmail.com");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Mail us at \"sceptre112358@gmail.com\"");
                        intent.setType("message/*");
                        Intent chooser = Intent.createChooser(intent, "Send Email");
                        startActivity(chooser);
                        break;


                }
                return true;
            }
        });
    }

    public void changeColor(int resourcecolor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), resourcecolor));
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(resourcecolor)));
    }


}