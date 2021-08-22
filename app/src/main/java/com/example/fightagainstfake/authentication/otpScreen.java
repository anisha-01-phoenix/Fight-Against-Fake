package com.example.fightagainstfake.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fightagainstfake.MainActivity;
import com.example.fightagainstfake.databinding.ActivityOtpScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class otpScreen extends AppCompatActivity {

    private String sname, semail, spassword, susername;
    String phone;
    ActivityOtpScreenBinding binding;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        sname = intent.getStringExtra("name");
        semail = intent.getStringExtra("email");
        spassword = intent.getStringExtra("password");
        susername = intent.getStringExtra("username");
        phone = binding.phoneNo.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();


        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (phone.isEmpty()) {
                    binding.phoneNo.setError("Can't be Empty");
                    binding.phoneNo.requestFocus();
                    return;
                }


                signIn();

            }
        });


    }

    private void signIn() {


        mAuth.createUserWithEmailAndPassword(semail, spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userList").child(uid);
                    Map<String, String> map = new HashMap<>();
                    map.put("name", sname);
                    map.put("username", susername);
                    map.put("password", spassword);
                    map.put("email", semail);
                    map.put("mobile", phone);
                    map.put("uid", uid);
                    map.put("phoneVerified", "no");
                    map.put("emailVerified", "no");
                    reference.setValue(map);


                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
                    finish();
                }

            }
        });


    }
}
