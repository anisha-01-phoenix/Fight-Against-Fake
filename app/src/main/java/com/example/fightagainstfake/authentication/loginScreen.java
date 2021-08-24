package com.example.fightagainstfake.authentication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fightagainstfake.MainActivity;
import com.example.fightagainstfake.Posts.AddPosts;
import com.example.fightagainstfake.databinding.ActivityLoginScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginScreen extends AppCompatActivity {

    ActivityLoginScreenBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(loginScreen.this, MainActivity.class);
            intent.putExtra("check","0");

            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();



        binding.bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.lemail.getEditText().getText().toString().trim();
                String password = binding.lpassword.getEditText().getText().toString().trim();

                if (email.isEmpty()) {
                    binding.lemail.setError("Field can't be empty");
                    binding.lemail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    binding.lpassword.setError("Field can't be empty");
                    binding.lpassword.requestFocus();
                    return;
                }


                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("check","0");

                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });


            }
        });


    }


    public void goToSignUp(View view) {

        Intent intent = new Intent(getApplicationContext(), signUp.class);

        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(binding.mainText, "mainText");
        pairs[1] = new Pair<View, String>(binding.linearLayout, "ll");
        pairs[2] = new Pair<View, String>(binding.bttn, "bttn");
        pairs[3] = new Pair<View, String>(binding.nextScreen, "back");

        ActivityOptions optionsCompat = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(intent, optionsCompat.toBundle());
    }

    public void forgot(View view) {
        startActivity(new Intent(loginScreen.this,Forgot_Password.class));
    }
}