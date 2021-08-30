package com.example.fightagainstfake.Posts.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fightagainstfake.ModelClass;
import com.example.fightagainstfake.Posts.Activities.NormalPosts;
import com.example.fightagainstfake.Posts.Adapters.NormalAdapter;
import com.example.fightagainstfake.databinding.FragmentNormalPostsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NormalPostsFragment extends Fragment {

    FragmentNormalPostsBinding fragmentNormalPostsBinding;
    private LinearLayoutManager layoutManager;
    private DatabaseReference reference;
    private ArrayList<ModelClass> list;
    private NormalAdapter adapter;
    private ModelClass modelClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentNormalPostsBinding = FragmentNormalPostsBinding.inflate(getLayoutInflater());
        fragmentNormalPostsBinding.addNormalPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NormalPosts.class));
            }
        });
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        reference = FirebaseDatabase.getInstance().getReference("NormalPosts");
        fragmentNormalPostsBinding.rvNormalPosts.setLayoutManager(layoutManager);
        list = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    modelClass = dataSnapshot.getValue(ModelClass.class);
                    list.add(modelClass);
                }

                adapter = new NormalAdapter(getContext(), list);

                fragmentNormalPostsBinding.rvNormalPosts.setAdapter(adapter);
                adapter.shimmering=false;
                adapter.notifyDataSetChanged();
              //  adapter.shimmering=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.shimmering = false;
                adapter.notifyDataSetChanged();

            }
        }, 3000);
        return fragmentNormalPostsBinding.getRoot();
    }


}