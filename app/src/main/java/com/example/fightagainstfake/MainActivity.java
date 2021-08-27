package com.example.fightagainstfake;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.fightagainstfake.Maps.MapsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.example.fightagainstfake.Posts.AddPosts;
import com.example.fightagainstfake.admin_package.info_for_customer.customer_info_corner;
import com.example.fightagainstfake.authentication.Startscreen;
import com.example.fightagainstfake.complaints.addComplaint;
import com.example.fightagainstfake.complaints.complaintStatus;
import com.example.fightagainstfake.complaints.model;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding activityMainBinding;
    FirebaseUser firebaseUser;
    ImageView navUserDp, editDp;
    String phone;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private Uri filepath;
    private Bitmap bitmap;

    ArrayList<model> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(activityMainBinding.toolBar);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {

                                    String currentuserId=user.getUid();

                                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(currentuserId).child("DeviceToken");
                                    reference.setValue(s);


                                }
                            });

        changeColor(R.color.themeColor);
        phone = getIntent().getStringExtra("phone");
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
                Intent i=new Intent(MainActivity.this, AddPosts.class);
                i.putExtra("check",0);
                startActivity(i);
                break;
            case R.id.nav_info:
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.complaintContainer, new customer_info_corner()).commit();
                break;
            case R.id.nav_maps:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
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
                FirebaseMessaging.getInstance().deleteToken();


                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("DeviceToken");
                reference.setValue("NotSet");


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
      /*  TextView navName = headerView.findViewById(R.id.header_name);
        TextView navUserName = headerView.findViewById(R.id.header_username);
*/

        navUserDp = headerView.findViewById(R.id.fraud_dp);
        editDp = headerView.findViewById(R.id.update_pic);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
       /* reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserModel model = snapshot.getValue(UserModel.class);
                            navName.setText(model.getName());
                            navUserName.setText(model.getUsername());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        storageReference = FirebaseStorage.getInstance().getReference();
        navUserDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDp.setVisibility(View.VISIBLE);
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("profilePic/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select image"), 50);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }
        });

        editDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    updateFirebase();
                    editDp.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "Upload Photo!", Toast.LENGTH_SHORT).show();
                    editDp.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50 && resultCode == RESULT_OK) {
            filepath = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                navUserDp.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    public void updateFirebase() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File Uploader");
        progressDialog.show();
        final StorageReference uploader = storageReference.child("profileimages/" + "img" + System.currentTimeMillis());
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Map<String, Object> map = new HashMap<>();
                                map.put("imageurl", uri.toString());

                                reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists())
                                            reference.child(firebaseUser.getUid()).updateChildren(map);
                                        else
                                            reference.child(firebaseUser.getUid()).setValue(map);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                progressDialog.dismiss();
                                navUserDp.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnProgressListener(snapshot -> {
            float percent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
            progressDialog.setMessage("Uploaded : " + (int) percent + "%");
        });
    }

}