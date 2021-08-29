package com.example.fightagainstfake;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.example.fightagainstfake.Posts.AddPosts;
import com.example.fightagainstfake.admin_package.info_for_customer.customer_info_corner;
import com.example.fightagainstfake.authentication.Startscreen;
import com.example.fightagainstfake.complaints.ModelComplaint;
import com.example.fightagainstfake.complaints.addComplaint;
import com.example.fightagainstfake.complaints.complaintStatus;
import com.example.fightagainstfake.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding activityMainBinding;
    FirebaseUser firebaseUser;
    ImageView navUserDp, editDp;
    private DatabaseReference reference;


    ArrayList<ModelComplaint> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(activityMainBinding.toolBar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Toast.makeText(MainActivity.this, user.getUid(), Toast.LENGTH_LONG).show();
        updateStatus("offline");
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {

                String currentuserId = user.getUid();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentuserId).child("dt");
                reference.setValue(s);


            }
        });

        changeColor(R.color.themeColor);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, activityMainBinding.drawerLayout, activityMainBinding.toolBar, R.string.open, R.string.close);
        activityMainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        activityMainBinding.navView.setNavigationItemSelectedListener(this);
        update_nav_header();

        data = new ArrayList<>();
        Intent intent = getIntent();
        int check = intent.getIntExtra("check", 0);

        if (check == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.complaintContainer, new complaintStatus()).commit();

        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.complaintContainer, new addComplaint()).commit();
        }


    }


    public void changeColor(int resourcecolor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), resourcecolor));
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(resourcecolor)));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_posts:
                Intent i = new Intent(MainActivity.this, AddPosts.class);
                startActivity(i);
                break;

            case R.id.recent_chats:
                Intent i1 = new Intent(MainActivity.this, recentChats.class);
                startActivity(i1);
                break;

            case R.id.nav_info:
                activityMainBinding.textView10.setText("INFO UPDATES");
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.complaintContainer, new customer_info_corner()).commit();
                break;

            case R.id.nav_register_complain:
                activityMainBinding.textView10.setText("COMPLAIN");
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.complaintContainer, new addComplaint()).commit();
                break;
            case R.id.nav_complaint_status:
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.complaintContainer, new complaintStatus()).commit();
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
            case R.id.logout:


                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Startscreen.class));
                finish();
                break;
        }
        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void update_nav_header() {
        View headerView = activityMainBinding.navView.getHeaderView(0);
        TextView navName = headerView.findViewById(R.id.header_name);
        TextView navUserName = headerView.findViewById(R.id.header_username);


        navUserDp = headerView.findViewById(R.id.fraud_dp);
        editDp = headerView.findViewById(R.id.update_pic);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel model = snapshot.getValue(UserModel.class);
                navName.setText(model.getName());
                navUserName.setText(model.getUsername());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void updateStatus(String status) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        reference.child("status").setValue(status);

    }

}