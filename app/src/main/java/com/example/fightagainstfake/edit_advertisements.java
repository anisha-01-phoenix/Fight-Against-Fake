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
import java.util.Random;

public class edit_advertisements extends AppCompatActivity {
    ActivityEditAdvertisementsBinding binding;
    int x=0;
    String uid,imageurl,postid,post,time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEditAdvertisementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
       uid=intent.getStringExtra("uid");
        imageurl=intent.getStringExtra("imageurl");
        postid=intent.getStringExtra("postid");
       post=intent.getStringExtra("post");
         time=intent.getStringExtra("time");

        binding.addNormalPostEditText.getEditText().setText(post);

        binding.addNormalPostEditText.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

        binding.btnAddNP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                String posto=binding.addNormalPostEditText.getEditText().getText().toString().trim();
                if (posto.isEmpty())
                {
                    binding.addNormalPostEditText.setError("Add The Post");
                    binding.addNormalPostEditText.requestFocus();
                    return;
                }
                else
                {
x=1;
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("AdvertisementPosts");
                    new_model_advertisement model_advertisement=new new_model_advertisement();
                    String newrabdomid=getrandomstring(6);
                    model_advertisement.setImageUrl(imageurl);
                    model_advertisement.setPost(binding.addNormalPostEditText.getEditText().getText().toString());
                    model_advertisement.setPostID(newrabdomid);
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
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("AdvertisementPosts");
                reference2.addValueEventListener(new ValueEventListener() {
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

                Toast.makeText(getApplicationContext(), "Advertisement Deleted!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(edit_advertisements.this, AddPosts.class));
                finish();
            }
        });
}


    @Override
    public void onBackPressed() {
        super.onBackPressed();
if(x==0){
    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("AdvertisementPosts");
    new_model_advertisement model_advertisement=new new_model_advertisement();
    model_advertisement.setImageUrl(imageurl);
    model_advertisement.setPost(post);
    model_advertisement.setPostID(postid);
    model_advertisement.setUserID(uid);
    model_advertisement.setTime(time);
    reference1.push().setValue(model_advertisement);
}
        Intent intent = new Intent(getApplicationContext(), AddPosts.class);
        //intent.putExtra("check","0");

        startActivity(intent);

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