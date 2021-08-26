package com.example.fightagainstfake.Posts.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fightagainstfake.Chat;
import com.example.fightagainstfake.Posts.Adapters.ChatAdapter;
import com.example.fightagainstfake.R;
import com.example.fightagainstfake.UserModel;
import com.example.fightagainstfake.databinding.ActivityChatBinding;
import com.example.fightagainstfake.notification.Data;
import com.example.fightagainstfake.notification.MyResponse;
import com.example.fightagainstfake.notification.Sender;
import com.example.fightagainstfake.notification.Token;
import com.example.fightagainstfake.notification.client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference reference;
    Intent intent;
    ArrayList<Chat> list;
    ActivityChatBinding activityChatBinding;
    private LinearLayoutManager layoutManager;
    private ChatAdapter adapter;

    APIService apiService;
    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());
        getSupportActionBar().hide();
        intent = getIntent();
        String userid = intent.getStringExtra("userid");
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

        apiService = client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        activityChatBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String mssg = activityChatBinding.addComment.getText().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy  HH:mm");
                String datetime = dateFormat.format(calendar.getTime());
                if (mssg.isEmpty())
                    Toast.makeText(ChatActivity.this, "Empty Message!", Toast.LENGTH_SHORT).show();
                else {
                    if (activityChatBinding.startRv.getVisibility() == View.VISIBLE)
                        activityChatBinding.startRv.setVisibility(View.INVISIBLE);
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
if(notify){
    sendNotification(userid,userModel.getUsername(),mssg);
}

                            sendNotification(userid, userModel.getPhone(), mssg);
                            notify=false;

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


                activityChatBinding.addComment.setText("");

            }
        });

        layoutManager = new LinearLayoutManager(this);
        activityChatBinding.rvChats.setLayoutManager(layoutManager);
        messages(user.getUid(), userid);
        list = new ArrayList<>();


    }

    private void sendNotification(String userid, String uid, String mssg) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(userid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot s : snapshot.getChildren()) {

                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(user.getUid(), R.drawable.logo, user.getUid() + ": " + mssg, "New Message", userid);

                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    if (response.body().success != 1) {

                                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                                    }


                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });


                }


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


}