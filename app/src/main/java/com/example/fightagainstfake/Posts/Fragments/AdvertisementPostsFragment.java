package com.example.fightagainstfake.Posts.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.fightagainstfake.ModelClass;
import com.example.fightagainstfake.Posts.Activities.AdvertisementPosts;
import com.example.fightagainstfake.Posts.Adapters.AdvertiseAdapter;
import com.example.fightagainstfake.databinding.FragmentAdvertisementPostsBinding;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdvertisementPostsFragment extends Fragment implements SearchView.OnQueryTextListener {
    FragmentAdvertisementPostsBinding fragmentAdvertisementPostsBinding;
    private LinearLayoutManager layoutManager;
    private DatabaseReference reference;
    private ArrayList<ModelClass> list;
    private AdvertiseAdapter adapter;
    private ModelClass modelClass;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAdvertisementPostsBinding= FragmentAdvertisementPostsBinding.inflate(getLayoutInflater());
        fragmentAdvertisementPostsBinding.addAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdvertisementPosts.class));
            }
        });

        layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        reference= FirebaseDatabase.getInstance().getReference("AdvertisementPosts");
        fragmentAdvertisementPostsBinding.rvAdvertisementPosts.setLayoutManager(layoutManager);
        list=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    modelClass =dataSnapshot.getValue(ModelClass.class);
                    list.add(modelClass);
                }

                adapter=new AdvertiseAdapter(getContext(),list);
                fragmentAdvertisementPostsBinding.rvAdvertisementPosts.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        fragmentAdvertisementPostsBinding.searchView.setOnQueryTextListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              adapter.isApShimmer=false;
              adapter.notifyDataSetChanged();

            }
        },3000);



        return fragmentAdvertisementPostsBinding.getRoot();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
  // adapter.getFilter().flter(newText);
        return false;
    }





}