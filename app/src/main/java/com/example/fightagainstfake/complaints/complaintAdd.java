package com.example.fightagainstfake.complaints;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fightagainstfake.MainActivity;
import com.example.fightagainstfake.databinding.ActivityComplaintAddBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class complaintAdd extends AppCompatActivity {

    ActivityComplaintAddBinding binding;
    Boolean upload=false;
    private StorageReference storageReference;
    private Uri filepath;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.uploadComplainProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select image"), 7);
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

                addPost();
            }
        });


    }

    private void addPost() {

        String post = binding.addNormalPostEditText.getEditText().getText().toString();
        if (post.isEmpty()) {
            binding.addNormalPostEditText.setError("Enter your Complaints here");
            binding.addNormalPostEditText.requestFocus();
            return;
        }

            storageReference= FirebaseStorage.getInstance().getReference();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("username");

            reference.addValueEventListener(new ValueEventListener() {
                String proofurl=null;

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String username = snapshot.getValue(String.class);


                    String complainId = getrandomstring(4);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("complaint").child(user.getUid()).child(complainId);

                    String complaintTitle = binding.addNormalPostEditText.getEditText().getText().toString().trim();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy  HH:mm");
                    String datetime = dateFormat.format(calendar.getTime());

                    String proof = "No Proof!";
                    String status = "Successfully Registered";

                    if (upload)
                    {
                        proof="Not Verified Yet!";
                        final ProgressDialog progressDialog = new ProgressDialog(complaintAdd.this);
                        progressDialog.setTitle("File Uploader");
                        progressDialog.show();
                        final StorageReference uploader = storageReference.child("Proofs/" + "img" + System.currentTimeMillis());
                        uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Toast.makeText(getApplicationContext(), complainId, Toast.LENGTH_SHORT).show();
                                        proofurl=uri.toString();
                                        String id=getrandomstring(4);
                                        model map=new model(id,complaintTitle,datetime,"Not Verified Yet!",status,user.getUid(),proofurl,username);
                                        reference.setValue(map);


                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("totalcomplaints");
                                        reference1.child(complainId).setValue(map);

                                        progressDialog.dismiss();
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
                        model map=new model(complainId,complaintTitle,datetime,proof,status,user.getUid(),null,username);
                        reference.setValue(map);


                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("totalcomplaints");
                        reference1.child(complainId).setValue(map);

                    }



                  /*  Map<String, String> map = new HashMap<>();
                    map.put("complaintTitle", complaintTitle);
                    map.put("datetime", datetime);
                    map.put("proof", proof);
                    map.put("proofurl",proofurl);
                    map.put("status", status);
                    map.put("uid", uid);
                    map.put("complainId", complainId);
                    map.put("username", username);

                    reference.setValue(map);*/



                    binding.addNormalPostEditText.getEditText().setText("");

                    //  Toasty.success(getApplicationContext(), "Successfully Registered").show();
                    Toast.makeText(complaintAdd.this, "Succesfully Registered!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("check", 1);
                    startActivity(intent);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            filepath = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                binding.showComplainPic.setImageBitmap(bitmap);
                upload=true;
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