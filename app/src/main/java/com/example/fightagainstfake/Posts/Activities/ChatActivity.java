package com.example.fightagainstfake.Posts.Activities;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());
        getSupportActionBar().hide();


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


        activityChatBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mssg = activityChatBinding.addComment.getText().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy  HH:mm");
                String datetime = dateFormat.format(calendar.getTime());
                if (mssg.isEmpty())
                    Toast.makeText(ChatActivity.this, "Empty Message!", Toast.LENGTH_SHORT).show();
                else {
                    if (activityChatBinding.startRv.getVisibility() == View.VISIBLE)
                        activityChatBinding.startRv.setVisibility(View.INVISIBLE);

                    DatabaseReference referenc = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("status");
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


                    Chat chat = new Chat(user.getUid(), userid, datetime, mssg);
                    FirebaseDatabase.getInstance().getReference().child("Chats").push().setValue(chat);


                    HashMap<String, String> chatNotification = new HashMap<>();
                    chatNotification.put("from", user.getUid());
                    chatNotification.put("msg", "msg");

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notification").child(userid);
                    reference.push().setValue(chatNotification);


                    FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserModel userModel = snapshot.getValue(UserModel.class);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


                activityChatBinding.addComment.setText("");

            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);


        activityChatBinding.rvChats.setLayoutManager(manager);

        messages(user.getUid(), userid);
        list = new ArrayList<>();


    }

    private void sendNotification() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("DeviceToken");
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


    private void messages(String myID, String otherID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chats = dataSnapshot.getValue(Chat.class);
                    if ((chats.getReceiver().equals(otherID) && chats.getSender().equals(myID)) || (chats.getSender().equals(otherID) && chats.getReceiver().equals(myID)))
                        list.add(chats);
                }

                if (list.size() == 0)
                    activityChatBinding.startRv.setVisibility(View.VISIBLE);

                adapter = new ChatAdapter(ChatActivity.this, list);
                activityChatBinding.rvChats.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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


}