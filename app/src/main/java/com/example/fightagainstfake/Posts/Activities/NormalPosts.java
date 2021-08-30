package com.example.fightagainstfake.Posts.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fightagainstfake.ModelClass;
import com.example.fightagainstfake.Posts.Adapters.NormalAdapter;
import com.example.fightagainstfake.Posts.AddPosts;
import com.example.fightagainstfake.databinding.ActivityNormalPostsBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class NormalPosts extends AppCompatActivity {

    ActivityNormalPostsBinding activityNormalPostsBinding;
    private DatabaseReference reference;
    NormalAdapter normalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNormalPostsBinding = ActivityNormalPostsBinding.inflate(getLayoutInflater());
        setContentView(activityNormalPostsBinding.getRoot());
        getSupportActionBar().hide();
        activityNormalPostsBinding.btnAddNP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = activityNormalPostsBinding.addNormalPostEditText.getEditText().getText().toString();
                if (post.isEmpty()) {
                    activityNormalPostsBinding.addNormalPostEditText.setError("Add The Post");
                    activityNormalPostsBinding.addNormalPostEditText.requestFocus();
                    return;
                } else {

                    reference = FirebaseDatabase.getInstance().getReference("NormalPosts");
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy  HH:mm");
                    String time = dateFormat.format(calendar.getTime());
                    ModelClass modelClass = new ModelClass(reference.push().getKey(), time, post);
                    reference.push().setValue(modelClass);
                    Toasty.success(getApplicationContext(), "Post Added").show();
                    startActivity(new Intent(NormalPosts.this, AddPosts.class));
                }
            }
        });
    }
}