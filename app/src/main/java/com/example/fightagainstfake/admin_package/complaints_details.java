package com.example.fightagainstfake.admin_package;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fightagainstfake.Posts.Activities.FullImageView;
import com.example.fightagainstfake.R;
import com.example.fightagainstfake.UserModel;
import com.example.fightagainstfake.authentication.Startscreen;
import com.example.fightagainstfake.complaints.ModelComplaint;
import com.example.fightagainstfake.databinding.ActivityComplaintsDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class complaints_details extends AppCompatActivity {
    ActivityComplaintsDetailsBinding binding;
    String status, complaintId, title, post_date, username, uid, proof, proofurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        Intent intent = getIntent();
        post_date = intent.getStringExtra("date");
        complaintId = intent.getStringExtra("compid");
        title = intent.getStringExtra("title");
        username = intent.getStringExtra("username");
        uid = intent.getStringExtra("uid");
        proof = intent.getStringExtra("proof");
        status = intent.getStringExtra("status");
        proofurl = intent.getStringExtra("proofurl");
        binding.dateCA.setText(post_date);
        binding.idCA.setText(complaintId);
        binding.titleCA.setText(title);
        binding.usernameCA.setText(username);
        binding.proofCA.setText(proof);
        binding.statusCA.setText(status);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("complaint").child(uid).child(complaintId);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelComplaint model = snapshot.getValue(ModelComplaint.class);
                binding.statusCA.setText(model.getStatus());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel model = snapshot.getValue(UserModel.class);
                binding.nameCA.setText(model.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.statusChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] statArray={"Pending","In Progress","Completed","Rejected"};
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(complaints_details.this);
                alertDialog.setTitle("SET STATUS");
                alertDialog.setSingleChoiceItems(statArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                status=statArray[which];

                    }
                });

                alertDialog.setPositiveButton("SET STATUS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference db3 = FirebaseDatabase.getInstance().getReference("complaint").child(uid).child(complaintId);
                        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("totalcomplaints").child(complaintId);

                        db3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ModelComplaint complaint=snapshot.getValue(ModelComplaint.class);
                                proof=complaint.getProof();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        binding.proofCA.setText(proof);
                        binding.statusCA.setText(status);
                        ModelComplaint model = new ModelComplaint();
                        model.setComplainId(complaintId);
                        model.setComplaintTitle(title);
                        model.setDatetime(post_date);
                        model.setProof(proof);
                        model.setProofurl(proofurl);
                        model.setStatus(status);
                        model.setUid(uid);
                        model.setUsername(username);
                        db1.setValue(model);
                        db3.setValue(model);
                    }
                });

                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();

               /* Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                bundle.putString("complaintId", complaintId);
                bundle.putString("status1", status);
                status_change statusChange = new status_change();
                statusChange.setArguments(bundle);
                statusChange.show(getSupportFragmentManager(), statusChange.getTag());*/
            }
        });

        binding.validateProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(complaints_details.this);
                alertDialog.setTitle("PROOF INFO");

                final EditText input = new EditText(complaints_details.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lp.leftMargin = 5;
                lp.rightMargin = 5;
                lp.gravity = 0;
                input.setLayoutParams(lp);
                input.setHint("Comments regarding Proofs....");
                input.setHintTextColor(getResources().getColor(R.color.purple_500));
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.ic_baseline_post_24);

                alertDialog.setPositiveButton("CHECK PROOF", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (proofurl == null) {
                            Toast.makeText(complaints_details.this, "No Proofs have been uploaded!", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(complaints_details.this, FullImageView.class);
                            intent.putExtra("zoom", proofurl);
                            startActivity(intent);
                        }
                    }
                });

                alertDialog.setNeutralButton("UPDATE PROOF STATUS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().isEmpty()) {
                            input.setError("Enter your comments!");
                            input.requestFocus();
                            Toast.makeText(complaints_details.this, "No Comments Updated!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DatabaseReference db3 = FirebaseDatabase.getInstance().getReference("complaint").child(uid).child(complaintId);
                        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("totalcomplaints").child(complaintId);

                        db3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ModelComplaint complaint=snapshot.getValue(ModelComplaint.class);
                                    status=complaint.getStatus();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        binding.proofCA.setText(input.getText().toString());
                        binding.statusCA.setText(status);
                        ModelComplaint model = new ModelComplaint();
                        model.setComplainId(complaintId);
                        model.setComplaintTitle(title);
                        model.setDatetime(post_date);
                        model.setProof(input.getText().toString());
                        model.setProofurl(proofurl);
                        model.setStatus(status);
                        model.setUid(uid);
                        model.setUsername(username);
                        db1.setValue(model);
                        db3.setValue(model);
                    }
                });


                /*alertDialog.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });*/
                alertDialog.show();
            }
        });
    }

    public void goBack(View view) {
        startActivity(new Intent(this, dashboard.class));

    }
}