package com.example.fightagainstfake.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fightagainstfake.databinding.ActivitySignUpBinding;

public class signUp extends AppCompatActivity {

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void next(View view) {

        String sname=binding.name.getText().toString().trim();
        String sUsername=binding.username.getText().toString().trim();
        String sEmail=binding.email.getText().toString().trim();
        String sPassword=binding.password.getText().toString().trim();


        if(sname.isEmpty()){
            binding.name.setError("Field can't be empty");
            binding.name.requestFocus();
            return;
        }

        if(sUsername.isEmpty()){
            binding.name.setError("Username can't be empty");
            binding.name.requestFocus();
            return;
        }

        if(sEmail.isEmpty()){
            binding.name.setError("Email can't be empty");
            binding.name.requestFocus();
            return;
        }

        if(sPassword.isEmpty() || sPassword.length()<6){
            binding.name.setError("INvalid format");
            binding.name.requestFocus();
            return;
        }


        Intent intent = new Intent(getApplicationContext(), otpScreen.class);

        intent.putExtra("name",sname);
        intent.putExtra("email",sEmail);
        intent.putExtra("password",sPassword);
        intent.putExtra("username",sUsername);

        startActivity(intent);


    }
}