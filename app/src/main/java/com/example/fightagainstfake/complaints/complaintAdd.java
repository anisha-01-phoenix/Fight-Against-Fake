package com.example.fightagainstfake.complaints;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fightagainstfake.MainActivity;
import com.example.fightagainstfake.databinding.ActivityComplaintAddBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class complaintAdd extends AppCompatActivity {

    ActivityComplaintAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
            binding.addNormalPostEditText.setError("Add The Post");
            binding.addNormalPostEditText.requestFocus();
            return;
        } else {

            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            String complainId=getrandomstring(6);
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("complaint").child(user.getUid()).child(complainId);

            String complaintTitle=binding.addNormalPostEditText.getEditText().getText().toString().trim();
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy  HH:mm");
            String datetime=dateFormat.format(calendar.getTime());

            String proof="noProof";
            String status="successfully registered";
            String uid=user.getUid();

            Map<String ,String > map=new HashMap<>();
            map.put("complaintTitle",complaintTitle);
            map.put("datetime",datetime);
            map.put("proof",proof);
            map.put("status",status);
            map.put("uid",uid);
            map.put("complainId",complainId);

            reference.setValue(map);



            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("totalcomplaints");
            reference1.push().setValue(map);

            binding.addNormalPostEditText.getEditText().setText("");

            Toasty.success(getApplicationContext(),"Successfully Registered").show();

            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("check","1");
            startActivity(intent);





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