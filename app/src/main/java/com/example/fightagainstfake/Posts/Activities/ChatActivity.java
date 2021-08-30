package com.example.fightagainstfake.Posts.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fightagainstfake.Chat;
import com.example.fightagainstfake.FcmNotificationsSender;
import com.example.fightagainstfake.Posts.Adapters.ChatAdapter;
import com.example.fightagainstfake.UserModel;
import com.example.fightagainstfake.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference reference;
    Intent intent;
    ArrayList<Chat> list;
    ActivityChatBinding activityChatBinding;
    private LinearLayoutManager layoutManager;
    private ChatAdapter adapter;
    String mssg;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());
        getSupportActionBar().hide();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        activityChatBinding.rvChats.setLayoutManager(manager);
        list = new ArrayList<>();
        adapter = new ChatAdapter(getApplicationContext(), list);
        activityChatBinding.rvChats.setAdapter(adapter);
        activityChatBinding.rvChats.hasFixedSize();

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                activityChatBinding.userMssg.setText(userModel.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getMessages();


        activityChatBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

recentchats();
                mssg = activityChatBinding.addComment.getText().toString().trim();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy  HH:mm");
                String datetime = dateFormat.format(calendar.getTime());
                if (mssg.isEmpty())
                    Toast.makeText(ChatActivity.this, "Empty Message!", Toast.LENGTH_SHORT).show();
                else {
                    if (activityChatBinding.startRv.getVisibility() == View.VISIBLE)
                        activityChatBinding.startRv.setVisibility(View.INVISIBLE);


                    DatabaseReference myuser = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("username");
                    myuser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String opp = snapshot.getValue(String.class);

                            DatabaseReference me = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("username");
                            me.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String me = snapshot.getValue(String.class);


                                    String mainname = me + opp;
                                    String mirror = opp + me;

                                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Chats").child(mainname);
                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Chats").child(mirror);

                                    Chat chat = new Chat(user.getUid(), userid, datetime, mssg);
                                    ref1.push().setValue(chat);
                                    ref2.push().setValue(chat);

                                    activityChatBinding.addComment.setText("");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    DatabaseReference referenc = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("status");
                    referenc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String status = snapshot.getValue(String.class);
                            if (status.equals("offline")) {
                                sendNotification();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }
        });


    }

    private void recentchats() {
        DatabaseReference myuser = FirebaseDatabase.getInstance().getReference("recent chats").child(user.getUid()).child(userid);
        Map<String, String> map=new HashMap<>();
        map.put("uid",userid);
        myuser.setValue(map);
        DatabaseReference myuser2 = FirebaseDatabase.getInstance().getReference("recent chats").child(userid).child(user.getUid());
        Map<String, String> map2=new HashMap<>();
        map2.put("uid",user.getUid());
        myuser2.setValue(map2);
    }


    private void getMessages() {


        DatabaseReference myuser = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("username");
        myuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String opp = snapshot.getValue(String.class);

                DatabaseReference me = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("username");
                me.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String me = snapshot.getValue(String.class);


                        String mainname = me + opp;

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(mainname);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                list.clear();

                                for (DataSnapshot s : snapshot.getChildren()) {

                                    Chat chat = s.getValue(Chat.class);

                                    Log.v("sandy", chat.getMessage());
                                    list.add(chat);


                                }
                                adapter.notifyDataSetChanged();
                                activityChatBinding.rvChats.smoothScrollToPosition(activityChatBinding.rvChats.getAdapter().getItemCount());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendNotification() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("dt");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String token = snapshot.getValue(String.class);

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("username");
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String username = snapshot.getValue(String.class);


                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token, username, mssg, getApplicationContext(), ChatActivity.this);

                        notificationsSender.SendNotifications();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void updateStatus(String status) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        reference.child("status").setValue(status);

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            updateStatus("offline");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            updateStatus("online");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            updateStatus("offline");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            updateStatus("offline");
        }
    }


}