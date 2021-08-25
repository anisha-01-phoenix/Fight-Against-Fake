package com.example.fightagainstfake.admin_package;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fightagainstfake.R;
import com.example.fightagainstfake.complaints.adapter;
import com.example.fightagainstfake.complaints.model;
import com.example.fightagainstfake.databinding.FragmentAddComplaintBinding;
import com.example.fightagainstfake.databinding.FragmentComplaintHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class complaint_home extends Fragment {

 FragmentComplaintHomeBinding binding;
admin_recv_adapter adapter;
    ArrayList<model_admin_recv> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentComplaintHomeBinding.inflate(inflater,container,false);
        data = new ArrayList<>();
        adapter = new admin_recv_adapter(data, getContext());
        binding.recv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recv.setAdapter(adapter);
        getData();

        return binding.getRoot();
    }

    private void getData() {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("totalcomplaints");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Map<String, String> map = (Map<String, String>) s.getValue();

                    String date = map.get("datetime");
                    String title = map.get("complaintTitle");
                    String status = map.get("status");
                    String id = map.get("complainId");
                    String uid=map.get("uid");
                 // Toast.makeText(getContext(), uid, Toast.LENGTH_SHORT).show();
                    String username=map.get("username");
                  //  Toast.makeText(getContext(), username, Toast.LENGTH_SHORT).show();
                    model_admin_recv model = new model_admin_recv();
                    model.setComplaintId(id);
                    model.setStatus(status);
                    model.setTitle(title);
                    model.setDate(date);
                    model.setUsername(username);
                    model.setUid(uid);
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