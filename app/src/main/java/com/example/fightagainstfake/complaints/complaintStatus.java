package com.example.fightagainstfake.complaints;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fightagainstfake.R;
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
    ArrayList<model> data;

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


                    model model = new model();
                    model.setComplaintTitle(title);
                    model.setStatus(status);
                    model.setDatetime(date);
                    model.setComplainId(id);
                    model.setProof(proof);
                    model.setProofurl(url);
                    model.setUsername(username);
                    data.add(model);
                    if(model!=null)
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

        ArrayList<model> filteredList=new ArrayList<>();

        for(model item:data){

            if(item.getComplaintTitle().toLowerCase().contains(text.toLowerCase(Locale.ROOT))){
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);

    }
}