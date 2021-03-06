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
import java.util.Locale;
import java.util.Map;


public class complaintStatus extends Fragment {

    FragmentComplaintStatusBinding binding;
    adapter adapter;
    ArrayList<ModelComplaint> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComplaintStatusBinding.inflate(inflater, container, false);


        data = new ArrayList<>();
        adapter = new adapter(data, getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.complaintRec.setLayoutManager(linearLayoutManager);
        binding.complaintRec.setAdapter(adapter);


        getComplaintStatus();

        if(data.size()<=0)
        {
            binding.complaintSs.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.complaintSs.setVisibility(View.INVISIBLE);
        }


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
                    String proof=map.get("proof");
                    String username=map.get("username");
                    String url=null;
                    if (map.get("proofurl")!=null)
                        url=map.get("proofurl");


                    ModelComplaint ModelComplaint = new ModelComplaint();
                    ModelComplaint.setComplaintTitle(title);
                    ModelComplaint.setStatus(status);
                    ModelComplaint.setDatetime(date);
                    ModelComplaint.setComplainId(id);
                    ModelComplaint.setProof(proof);
                    ModelComplaint.setProofurl(url);
                    ModelComplaint.setUsername(username);
                    data.add(ModelComplaint);
                    if(ModelComplaint !=null)
                        binding.complaintSs.setVisibility(View.INVISIBLE);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void filter(String text){

        ArrayList<ModelComplaint> filteredList=new ArrayList<>();

        for(ModelComplaint item:data){

            if(item.getComplaintTitle().toLowerCase().contains(text.toLowerCase(Locale.ROOT))){
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);

    }
}