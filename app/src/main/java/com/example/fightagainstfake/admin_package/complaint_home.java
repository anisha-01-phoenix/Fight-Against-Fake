package com.example.fightagainstfake.admin_package;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fightagainstfake.complaints.ModelComplaint;
import com.example.fightagainstfake.databinding.FragmentComplaintHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class complaint_home extends Fragment {

    FragmentComplaintHomeBinding binding;
    admin_recv_adapter adapter;
    ArrayList<ModelComplaint> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentComplaintHomeBinding.inflate(inflater, container, false);
        data = new ArrayList<>();
        adapter = new admin_recv_adapter(data, getContext());
        binding.recv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recv.setAdapter(adapter);
        getData();

        return binding.getRoot();
    }

    private void getData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("totalcomplaints");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot s : snapshot.getChildren()) {
                    ModelComplaint model = s.getValue(ModelComplaint.class);
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