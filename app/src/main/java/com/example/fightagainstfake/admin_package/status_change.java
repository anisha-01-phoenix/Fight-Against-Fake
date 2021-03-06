package com.example.fightagainstfake.admin_package;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.fightagainstfake.R;
import com.example.fightagainstfake.complaints.ModelComplaint;
import com.example.fightagainstfake.databinding.ActivityComplaintsDetailsBinding;
import com.example.fightagainstfake.databinding.FragmentStatusChangeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class status_change extends BottomSheetDialogFragment {
    FragmentStatusChangeBinding binding;
    String status, uid, complaintId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatusChangeBinding.inflate(inflater, container, false);
        uid = getArguments().getString("uid");

        complaintId = getArguments().getString("complaintId");
        binding.seen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    binding.Completed.setChecked(false);
                    binding.rejected.setChecked(false);
                    binding.inProgress.setChecked(false);
                    status = "Accepted";
                } else if (!buttonView.isChecked()) {
                    status = "No Proof";
                }
            }
        });

        binding.inProgress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    binding.Completed.setChecked(false);
                    binding.rejected.setChecked(false);
                    binding.seen.setChecked(false);
                    status = "In Progress";
                } else if (!buttonView.isChecked()) {
                    status = "No Proof";
                }
            }
        });

        binding.rejected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    binding.Completed.setChecked(false);
                    binding.seen.setChecked(false);
                    binding.inProgress.setChecked(false);
                    status = "Rejected";
                } else if (!buttonView.isChecked()) {
                    status = "No Proof";
                }
            }
        });

        binding.Completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    binding.seen.setChecked(false);
                    binding.rejected.setChecked(false);
                    binding.inProgress.setChecked(false);
                    status = "Completed";
                } else if (!buttonView.isChecked()) {
                    status = "No Proof";

                }
            }
        });

        binding.statusChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference db = FirebaseDatabase.getInstance().getReference("complaint").child(uid).child(complaintId);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ModelComplaint model = snapshot.getValue(ModelComplaint.class);
                        String username = model.getUsername();
                        String proofurl = model.getProofurl();
                        String titlee = model.getComplaintTitle();
                        String proof = model.getProof();
                        String datetime = model.getDatetime();

                        // DatabaseReference db2= FirebaseDatabase.getInstance().getReference("complaint").child(uid).child(complaintId);
                        //    db2.removeValue();
                        DatabaseReference db3 = FirebaseDatabase.getInstance().getReference("complaint").child(uid).child(complaintId);
                        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("totalcomplaints").child(complaintId);
                        ModelComplaint complaintModel = new ModelComplaint();
                        complaintModel.setComplaintTitle(titlee);
                        complaintModel.setComplainId(complaintId);
                        complaintModel.setProof(proof);
                        complaintModel.setDatetime(datetime);
                        complaintModel.setUsername(username);
                        complaintModel.setProofurl(proofurl);
                        complaintModel.setUid(uid);


                        Log.v("status", status);
                        complaintModel.setStatus(status);
                        db3.setValue(complaintModel);
                        db1.setValue(complaintModel);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dismiss();
            }
        });


        return binding.getRoot();
    }
}