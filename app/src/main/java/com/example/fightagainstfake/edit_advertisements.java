package com.example.fightagainstfake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fightagainstfake.Posts.Activities.NormalPosts;
import com.example.fightagainstfake.Posts.AddPosts;
import com.example.fightagainstfake.databinding.ActivityEditAdvertisementsBinding;
import com.example.fightagainstfake.databinding.FragmentNormalPostsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class edit_advertisements extends AppCompatActivity {
    ActivityEditAdvertisementsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEditAdvertisementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        String uid=intent.getStringExtra("uid");
        String imageurl=intent.getStringExtra("imageurl");
        String postid=intent.getStringExtra("postid");
        String post=intent.getStringExtra("post");
        String time=intent.getStringExtra("time");
        new_model_advertisement model_advertisement=new new_model_advertisement();
        binding.addNormalPostEditText.getEditText().setText(post);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AdvertisementPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelClass modelClass1 = dataSnapshot.getValue(ModelClass.class);
                    if (modelClass1.getPostID().equals(postid)) {
                        dataSnapshot.getRef().removeValue();

                        break;
                    } else continue;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.btnAddNP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String posto=binding.addNormalPostEditText.getEditText().getText().toString().trim();
                if (posto.isEmpty())
                {
                    binding.addNormalPostEditText.setError("Add The Post");
                    binding.addNormalPostEditText.requestFocus();
                    return;
                }
                else
                {

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("AdvertisementPosts");
                    model_advertisement.setImageUrl(imageurl);
                    model_advertisement.setPost(binding.addNormalPostEditText.getEditText().getText().toString());
                    model_advertisement.setPostID(postid);
                    model_advertisement.setUserID(uid);
                    model_advertisement.setTime(time);
                    reference1.push().setValue(model_advertisement);
                    Toast.makeText(edit_advertisements.this, "Advertisement Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(edit_advertisements.this, AddPosts.class));
                    finish();
                }




                //Toast.makeText(getApplicationContext(), "Advertisement Updated!!", Toast.LENGTH_SHORT).show();



            }



        });
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.addNormalPostEditText.getEditText().setText("");
                Toast.makeText(getApplicationContext(), "Advertisement Deleted!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(edit_advertisements.this, AddPosts.class));
                finish();
            }
        });
}}