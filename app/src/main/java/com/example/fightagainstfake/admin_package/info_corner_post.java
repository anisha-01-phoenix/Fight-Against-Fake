package com.example.fightagainstfake.admin_package;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fightagainstfake.ModelClass;
import com.example.fightagainstfake.Posts.Activities.NormalPosts;
import com.example.fightagainstfake.Posts.AddPosts;
import com.example.fightagainstfake.R;
import com.example.fightagainstfake.databinding.ActivityInfoCornerPostBinding;
import com.example.fightagainstfake.databinding.ActivityNormalPostsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class info_corner_post extends AppCompatActivity {
ActivityInfoCornerPostBinding binding;
Boolean isUpload=false;
    private StorageReference storageReference;
    private Uri filepath;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoCornerPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.uploadInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select image"), 10);
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
        binding.btnAddNP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = binding.addNormalPostEditText.getEditText().getText().toString();
                if (post.isEmpty()) {
                    binding.addNormalPostEditText.setError("Add Updates!");
                    binding.addNormalPostEditText.requestFocus();
                    return;
                } else {
                    DatabaseReference reference;
                    reference = FirebaseDatabase.getInstance().getReference("info_corner");
                    storageReference = FirebaseStorage.getInstance().getReference();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy  HH:mm");
                    String time = dateFormat.format(calendar.getTime());

                    if (isUpload) {
                        final ProgressDialog progressDialog = new ProgressDialog(info_corner_post.this);
                        progressDialog.setTitle("File Uploader");
                        progressDialog.show();
                        final StorageReference uploader = storageReference.child("Info_docs/" + "img" + System.currentTimeMillis());
                        uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        model_info_corner_fb model = new model_info_corner_fb();
                                        model.setPostdata(post);
                                        model.setDate(time);
                                        model.setImgUrl(uri.toString());
                                        reference.push().setValue(model);
                                        Toast.makeText(info_corner_post.this, "Info Added!", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        startActivity(new Intent(info_corner_post.this, dashboard.class));
                                        finish();
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
                    } else {
                        model_info_corner_fb model = new model_info_corner_fb();
                        model.setPostdata(post);
                        model.setDate(time);
                        reference.push().setValue(model);
                        Toast.makeText(info_corner_post.this, "Info Added!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(info_corner_post.this, dashboard.class));
                        finish();
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            filepath = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                binding.showInfoPic.setImageBitmap(bitmap);
                isUpload=true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}