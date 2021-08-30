package com.example.fightagainstfake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.fightagainstfake.databinding.ActivityRecentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class recentChats extends AppCompatActivity {
    ActivityRecentChatsBinding binding;
adapter_rc adapter;
    FirebaseUser user;
    ArrayList<model_recentchants>data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRecentChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.chatList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        data=new ArrayList<>();
        adapter=new adapter_rc(data,this);

        binding.chatList.setAdapter(adapter);

        user= FirebaseAuth.getInstance().getCurrentUser();
        getData();
    }

    private void getData() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("recent chats").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for(DataSnapshot s:snapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) s.getValue();
                    String uidl=map.get("uid");
                    DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("user for rc").child(uidl);
                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                            String name=map.get("name");
                           // String statusl=map.get("status");
                            String username=map.get("username");
                         //   Log.v("jkjk",name+statusl+username);
                            model_recentchants modelRecentchants=new model_recentchants();
                           // modelRecentchants.setStatusonline(statusl);
                            modelRecentchants.setUsername(username);
                            modelRecentchants.setName(name);
                            modelRecentchants.setFrontuserid(uidl);
                            data.add(modelRecentchants);

                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}