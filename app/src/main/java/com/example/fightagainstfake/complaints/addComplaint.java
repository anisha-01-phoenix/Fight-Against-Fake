package com.example.fightagainstfake.complaints;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fightagainstfake.R;
import com.example.fightagainstfake.databinding.FragmentAddComplaintBinding;

public class addComplaint extends Fragment {


    FragmentAddComplaintBinding binding;
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding=FragmentAddComplaintBinding.inflate(inflater,container,false);


       binding.addComplaint.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getContext(),complaintAdd.class);
               startActivity(intent);

           }
       });




        return binding.getRoot();
    }
}