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
import com.example.fightagainstfake.authentication.Startscreen;
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
        proofurl = intent.getStringExtra("proofurl");
        binding.dateCA.setText(post_date);
        binding.idCA.setText(complaintId);
        binding.titleCA.setText(title);
        binding.usernameCA.setText(username);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("complaint").child(uid).child(complaintId);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> map = (Map<String, String>) snapshot.getValue();
                complaint_model model = new complaint_model();
                model.setStatus(map.get("status"));
                model.setProof(map.get("proof"));
                binding.proofCA.setText(map.get("proof"));
                binding.statusCA.setText(map.get("status"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> map = (Map<String, String>) snapshot.getValue();
                binding.nameCA.setText(map.get("name"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.statusChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                bundle.putString("complaintId", complaintId);
                bundle.putString("status1", status);
                status_change statusChange = new status_change();
                statusChange.setArguments(bundle);
                statusChange.show(getSupportFragmentManager(), statusChange.getTag());
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
                input.setHint("Comments regarding the Proofs....");
                input.setHintTextColor(getResources().getColor(R.color.purple_500));
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.ic_baseline_post_24);

                alertDialog.setNeutralButton("CHECK PROOF", new DialogInterface.OnClickListener() {
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

                alertDialog.setPositiveButton("UPDATE PROOF STATUS", new DialogInterface.OnClickListener() {
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
                        complaint_model model = new complaint_model(complaintId, title, post_date, input.getText().toString(), status, uid, username, proofurl);
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