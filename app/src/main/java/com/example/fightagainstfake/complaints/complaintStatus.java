package com.example.fightagainstfake.complaints;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fightagainstfake.databinding.FragmentComplaintStatusBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class complaintStatus extends Fragment {

    FragmentComplaintStatusBinding binding;
    adapter adapter;
    ArrayList<model> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComplaintStatusBinding.inflate(inflater, container, false);

        data = new ArrayList<>();
        adapter = new adapter(data, getContext());
        binding.complaintRec.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.complaintRec.setAdapter(adapter);


        getComplaintStatus();

        return binding.getRoot();
    }

    private void getComplaintStatus() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("complaint").child(FirebaseAuth.getInstance().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                data.clear();
                for (DataSnapshot s : snapshot.getChildren()) {

                    Map<String, String> map = (Map<String, String>) s.getValue();

                    String date = map.get("datetime");
                    String title = map.get("complaintTitle");
                    String status = map.get("status");
                    String id = map.get("complainId");

                    model model = new model();
                    model.setComplaintTitle(title);
                    model.setStatus(status);
                    model.setDatetime(date);
                    model.setComplainId(id);
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