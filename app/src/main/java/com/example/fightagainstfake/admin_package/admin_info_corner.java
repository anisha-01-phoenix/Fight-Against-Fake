package com.example.fightagainstfake.admin_package;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fightagainstfake.Posts.Activities.NormalPosts;
import com.example.fightagainstfake.R;
import com.example.fightagainstfake.databinding.FragmentAdminInfoCornerBinding;
import com.example.fightagainstfake.databinding.FragmentComplaintHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class admin_info_corner extends Fragment {

    FragmentAdminInfoCornerBinding binding;
    admin_info_corner_adapter adapter;
    ArrayList<model_info_corner> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAdminInfoCornerBinding.inflate(inflater,container,false);
        data = new ArrayList<>();
        adapter = new admin_info_corner_adapter(data, getContext());
        binding.recv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recv.setAdapter(adapter);
        getData();
        binding.addNormalPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), info_corner_post.class));
            }
        });
        return  binding.getRoot();
    }

    private void getData() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("info_corner");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Map<String, String> map = (Map<String, String>) s.getValue();

                    String date = map.get("date");
                    String postdata = map.get("postdata");
                    model_info_corner model = new model_info_corner();
                    model.setPostdata(postdata);
                    model.setDate(date);
                    data.add(model);


                }
                adapter.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}