package com.example.fightagainstfake.authentication;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fightagainstfake.MainActivity;
import com.example.fightagainstfake.UserModel;
import com.example.fightagainstfake.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class signUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signUp.this, loginScreen.class));
            }
        });



    }


    public void next(View view) {
        auth = FirebaseAuth.getInstance();
        String sname = binding.name.getText().toString().trim();
        String sUsername = binding.username.getText().toString().trim();
        String sEmail = binding.email.getText().toString().trim();
        String sPassword=binding.pass.getText().toString().trim();
        String sPhoneNo=binding.phoneNo.getText().toString().trim();


        if (sname.isEmpty()) {
            binding.name.setError("Field can't be empty");
            binding.name.requestFocus();
            return;
        }

        if (sUsername.isEmpty()) {
            binding.username.setError("Username can't be empty");
            binding.username.requestFocus();
            return;
        }

        if (sEmail.isEmpty()) {
            binding.email.setError("Email can't be empty");
            binding.email.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            binding.email.setError("Email ID is invalid!");
            binding.email.requestFocus();
            return;
        }

        if (sPassword.isEmpty()) {
            binding.pass.setError("Field can't be empty");
            binding.pass.requestFocus();
            return;
        }
        else
            if(sPassword.length()<6)
            {
                binding.pass.setError("Minimum 6 characters!");
                binding.pass.requestFocus();
                return;
            }

        if (sPhoneNo.isEmpty()) {
            binding.phoneNo.setError("Field can't be empty");
            binding.phoneNo.requestFocus();
            return;
        }


        Intent intent = new Intent(getApplicationContext(), otpScreen.class);


        String phoneNos=binding.cpp.getSelectedCountryCodeWithPlus()+sPhoneNo;

        intent.putExtra("name",sname);
        intent.putExtra("username",sUsername);
        intent.putExtra("phone",phoneNos);
        intent.putExtra("email",sEmail);
        intent.putExtra("password",sPassword);

        startActivity(intent);

    }



}