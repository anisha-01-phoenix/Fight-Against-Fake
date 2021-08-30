package com.example.fightagainstfake.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fightagainstfake.UserModel;
import com.example.fightagainstfake.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class signUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    DatabaseReference reference;


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
        reference = FirebaseDatabase.getInstance().getReference("Users");
        String sname = binding.name.getText().toString().trim();
        String sUsername = binding.username.getText().toString().trim();
        String sEmail = binding.email.getText().toString().trim();
        String sPassword = binding.pass.getText().toString().trim();
        String confirm = binding.confirmPass.getText().toString().trim();


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
        } else if (sPassword.length() < 6) {
            binding.pass.setError("Minimum 6 characters!");
            binding.pass.requestFocus();
            return;
        }
        if (confirm.isEmpty()) {
            binding.confirmPass.setError("Field can't be empty!");
            binding.confirmPass.requestFocus();
            return;
        } else if (confirm.length() < 6) {
            binding.confirmPass.setError("Minimum 6 characters!");
            binding.confirmPass.requestFocus();
            return;
        } else if (!confirm.equals(sPassword)) {
            binding.confirmPass.setError("Check your Password!");
            binding.confirmPass.requestFocus();
            return;
        }

        Query query1 = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").equalTo(sUsername);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    binding.username.setError("Username already exists.Use some other username");
                    binding.username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.pb.setVisibility(View.VISIBLE);

        SignUp(sUsername, sEmail, sname, sPassword);

    }

    private void SignUp(String sUsername, String sEmail, String sname, String sPassword) {

        auth.createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(signUp.this, "User Registered Successfully! Check your email and verify it", Toast.LENGTH_SHORT).show();
                                binding.pb.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(signUp.this, "User Registered Successfully. But failed to send verification Link to Email.\\n Error : \" + task.getException().getMessage()", Toast.LENGTH_SHORT).show();
                                binding.pb.setVisibility(View.INVISIBLE);
                            }
                            FirebaseAuth.getInstance().signOut();
                        }
                    });
                    FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {

                            UserModel model = new UserModel(auth.getCurrentUser().getUid(), sname, sUsername, s);
                            reference.child(auth.getCurrentUser().getUid()).setValue(model);
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid()).child("dt");
                            reference.setValue(s);
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("user for rc").child(auth.getCurrentUser().getUid());
                            Map<String, String> map = new HashMap<>();
                            map.put("name", sname);
                            map.put("username", sUsername);
                            reference1.setValue(map);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}


