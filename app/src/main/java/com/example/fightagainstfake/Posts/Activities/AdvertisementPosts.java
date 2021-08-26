package com.example.fightagainstfake.Posts.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.fightagainstfake.ModelClass;
import com.example.fightagainstfake.Posts.AddPosts;
import com.example.fightagainstfake.Posts.Fragments.AdvertisementPostsFragment;
import com.example.fightagainstfake.databinding.ActivityAdvertisementPostsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class AdvertisementPosts extends AppCompatActivity {

    ActivityAdvertisementPostsBinding activityAdvertisementPostsBinding;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    Uri filepath;
    Bitmap bitmap;
    StorageReference storageReference;
    Boolean isUploaded=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdvertisementPostsBinding = ActivityAdvertisementPostsBinding.inflate(getLayoutInflater());
        setContentView(activityAdvertisementPostsBinding.getRoot());
        getSupportActionBar().hide();
        String complainId = getrandomstring(6);
        activityAdvertisementPostsBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select image"), 2);
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
        activityAdvertisementPostsBinding.btnAddAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = activityAdvertisementPostsBinding.addAdvertisePostEditText.getEditText().getText().toString();
                if (post.isEmpty()) {
                    activityAdvertisementPostsBinding.addAdvertisePostEditText.setError("Add The Post");
                    activityAdvertisementPostsBinding.addAdvertisePostEditText.requestFocus();
                    return;
                } else {
                    reference = FirebaseDatabase.getInstance().getReference("AdvertisementPosts");
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    storageReference= FirebaseStorage.getInstance().getReference();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy  HH:mm");
                    String time = dateFormat.format(calendar.getTime());
                    if (isUploaded) {
                        final ProgressDialog progressDialog = new ProgressDialog(AdvertisementPosts.this);
                        progressDialog.setTitle("File Uploader");
                        progressDialog.show();
                        final StorageReference uploader = storageReference.child("Advertisement_docs/" + "img" + System.currentTimeMillis());
                        uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String complainId = getrandomstring(6);
                                        Toast.makeText(getApplicationContext(), complainId, Toast.LENGTH_SHORT).show();
                                        ModelClass modelClass = new ModelClass(firebaseUser.getUid(), complainId, time, post, uri.toString());
                                        reference.push().setValue(modelClass);
                                        Toast.makeText(AdvertisementPosts.this, "Post Added", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        startActivity(new Intent(AdvertisementPosts.this, AddPosts.class));
                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                float percent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploading : " + (int) percent + "%");
                            }
                        });
                    }
                    else
                    {
                        ModelClass modelClass = new ModelClass(firebaseUser.getUid(), complainId, time, post, "not uploaded");
                        reference.push().setValue(modelClass);
                        Toast.makeText(AdvertisementPosts.this, "Post Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdvertisementPosts.this, AddPosts.class));
                    }

                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            filepath = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                activityAdvertisementPostsBinding.imgUploadedFiles.setImageBitmap(bitmap);
                isUploaded=true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getrandomstring(int i) {
        final String chaaracters = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJklMNOPQRSTUV";
        StringBuilder result = new StringBuilder();
        while (i > 0) {
            Random rand = new Random();
            result.append(chaaracters.charAt(rand.nextInt(chaaracters.length())));
            i--;
        }
        return result.toString();
    }

}