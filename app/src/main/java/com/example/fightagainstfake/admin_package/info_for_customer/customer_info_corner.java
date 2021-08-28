package com.example.fightagainstfake.admin_package.info_for_customer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fightagainstfake.admin_package.admin_info_corner_adapter;
import com.example.fightagainstfake.admin_package.info_corner_post;
import com.example.fightagainstfake.admin_package.model_info_corner;
import com.example.fightagainstfake.databinding.FragmentAdminInfoCornerBinding;
import com.example.fightagainstfake.databinding.FragmentCustomerInfoCornerBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class customer_info_corner extends Fragment {

    FragmentCustomerInfoCornerBinding binding;
    admin_info_corner_adapter adapter;
    ArrayList<model_info_corner> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentCustomerInfoCornerBinding.inflate(inflater,container,false);
        data = new ArrayList<>();
        adapter = new admin_info_corner_adapter(data, getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        binding.recv.setLayoutManager(layoutManager);
        binding.recv.setAdapter(adapter);
        getData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.info_shimmer=false;
                adapter.notifyDataSetChanged();

            }
        },3000);

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
                    String imgUrl=null;
                    if (map.get("imgUrl")!=null) {
                        imgUrl = map.get("imgUrl");
                    }
                    model_info_corner model = new model_info_corner();
                    model.setPostdata(postdata);
                    if (imgUrl!=null)
                        model.setImgUrl(imgUrl);
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