package com.example.fightagainstfake.admin_package;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fightagainstfake.ModelClass;
import com.example.fightagainstfake.Posts.Activities.NormalPosts;
import com.example.fightagainstfake.Posts.AddPosts;
import com.example.fightagainstfake.R;
import com.example.fightagainstfake.databinding.ActivityInfoCornerPostBinding;
import com.example.fightagainstfake.databinding.ActivityNormalPostsBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class info_corner_post extends AppCompatActivity {
ActivityInfoCornerPostBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityInfoCornerPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.btnAddNP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post=binding.addNormalPostEditText.getEditText().getText().toString();
                if (post.isEmpty())
                {
                    binding.addNormalPostEditText.setError("Add The Post");
                    binding.addNormalPostEditText.requestFocus();
                    return;
                }
                else
                {
                    DatabaseReference reference;
                    reference= FirebaseDatabase.getInstance().getReference("info_corner");
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMM-yyyy  HH:mm");
                    String time=dateFormat.format(calendar.getTime());
                  model_info_corner_fb model_info_corner=new model_info_corner_fb();
                  model_info_corner.setPostdata(post);
                  model_info_corner.setDate(time);
                  reference.push().setValue(model_info_corner);
                    Toast.makeText(info_corner_post.this, "Info Added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(info_corner_post.this, dashboard.class));
                    finish();
                }
            }
        });
    }
}