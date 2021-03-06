package com.example.fightagainstfake.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.fightagainstfake.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ActivityForgotPasswordBinding passwordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordBinding=ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(passwordBinding.getRoot());
        getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();
        passwordBinding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    private void resetPassword()
    {
        String email=passwordBinding.emailFP.getText().toString().trim();
        if(email.isEmpty())
        {
            passwordBinding.emailFP.setError("Email is Required!");
            passwordBinding.emailFP.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            passwordBinding.emailFP.setError("Email ID is Invalid!");
            passwordBinding.emailFP.requestFocus();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(Forgot_Password.this, loginScreen.class));
                    Toast.makeText(Forgot_Password.this, "Check your Email to reset your Password!",Toast.LENGTH_LONG ).show();
                    return;
                }
                else
                {
                    Toast.makeText(Forgot_Password.this, "Try Again!",Toast.LENGTH_LONG ).show();
                    return;
                }
            }
        });


    }
}