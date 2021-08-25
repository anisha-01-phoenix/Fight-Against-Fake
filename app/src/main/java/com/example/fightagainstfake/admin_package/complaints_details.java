package com.example.fightagainstfake.admin_package;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fightagainstfake.R;
import com.example.fightagainstfake.databinding.ActivityComplaintsDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class complaints_details extends AppCompatActivity {
ActivityComplaintsDetailsBinding binding;
    String status,complaintId,title,post_date,username,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityComplaintsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
       post_date=intent.getStringExtra("date");
       //status=intent.getStringExtra("status");
       complaintId=intent.getStringExtra("compid");
       title=intent.getStringExtra("title");
       username=intent.getStringExtra("username");
       uid=intent.getStringExtra("uid");
       binding.dateCA.setText(post_date);
       binding.idCA.setText(complaintId);
       binding.titleCA.setText(title);
       binding.usernameCA.setText(username);

DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("complaint").child(uid).child(complaintId);
ref1.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
       Map<String, String> map = (Map<String, String>) snapshot.getValue();
        binding.proofCA.setText(map.get("proof"));
        binding.statusCA.setText(map.get("status"));

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
        DatabaseReference ref2= FirebaseDatabase.getInstance().getReference("Users").child(uid);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> map = (Map<String, String>) snapshot.getValue();
                binding.nameCA.setText(map.get("name"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

binding.statusChange.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        bundle.putString("uid",uid);
        bundle.putString("complaintId",complaintId);
        bundle.putString("status1",status);
status_change statusChange=new status_change();
statusChange.setArguments(bundle);
statusChange.show(getSupportFragmentManager(),statusChange.getTag());
    }
});
    }

    public void goBack(View view) {
        startActivity(new Intent(this,dashboard.class));

    }
}