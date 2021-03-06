package com.example.fightagainstfake.authentication;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fightagainstfake.MainActivity;
import com.example.fightagainstfake.UserModel;
import com.example.fightagainstfake.databinding.ActivityOtpScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class otpScreen extends AppCompatActivity {
    ActivityOtpScreenBinding binding;
    FirebaseAuth mAuth;

    private String sname="", susername="", sphone="",smail="",sPassword="", mVerificationId;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.mob,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();

        Intent intent = getIntent();
        sname = intent.getStringExtra("name");
        susername = intent.getStringExtra("username");
        sphone = intent.getStringExtra("phone");

        smail=intent.getStringExtra("email");
        sPassword=intent.getStringExtra("password");
        mAuth=FirebaseAuth.getInstance();
        mAuth.setLanguageCode("fr");

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


      
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("OTP failed!",e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                forceResendingToken = token;
                binding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(otpScreen.this, "OTP has been sent...", Toast.LENGTH_SHORT).show();
            }
        };

        startPhoneNumberVerification(sphone);

        binding.resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(sphone, forceResendingToken);
            }
        });

        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = binding.otpNo.getText().toString().trim();
                if (otp.isEmpty()) {
                    binding.otpNo.setError("Enter your verification code");
                    binding.otpNo.requestFocus();
                    return;
                }
                verifyPhoneNo(mVerificationId, otp);
            }
        });


    }

    private void verifyPhoneNo(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signIn(credential);
    }

    private void startPhoneNumberVerification(String sphone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(sphone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode(String sphone, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(sphone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void signIn(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    binding.progressBar.setVisibility(View.INVISIBLE);

                    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(smail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                        }
                    });

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {

                         //   String currentuserId = user.getUid();
                            token = s;


                        }
                    });


                    String uid = user.getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                    /*Map<String, String> map = new HashMap<>();
                    map.put("name", sname);
                    map.put("username", susername);
                    map.put("id", uid);
                    map.put("imageurl", null);
                    map.put("phoneNo", sphone);
                    map.put("DeviceToken",token);
                    reference.setValue(map);
*/

                    UserModel model=new UserModel();
                    model.setId(user.getUid());
                    model.setName(sname);
                    model.setUsername(susername);
                    model.setDeviceToken(token);
                    reference.setValue(model);

                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);

                    intent1.putExtra("phone", sphone);

                    intent1.putExtra("check", 0);


                    startActivity(intent1);
                    finish();
                } else
                    Toast.makeText(otpScreen.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
