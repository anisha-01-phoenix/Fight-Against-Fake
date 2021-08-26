package com.example.fightagainstfake.Posts.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fightagainstfake.ModelClass;
import com.example.fightagainstfake.Posts.Activities.ChatActivity;
import com.example.fightagainstfake.Posts.Activities.FullImageView;
import com.example.fightagainstfake.R;
import com.example.fightagainstfake.UserModel;
import com.example.fightagainstfake.edit_advertisements;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AdvertiseAdapter extends RecyclerView.Adapter<AdvertiseAdapter.AdvertiseViewHolder> {

    Context context;
    ArrayList<ModelClass> list;
    FirebaseUser firebaseUser;
    public boolean isApShimmer = true;

    public AdvertiseAdapter(Context context, ArrayList<ModelClass> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public AdvertiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.advertisement_post_item, parent, false);
        return new AdvertiseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertiseViewHolder holder, int position) {
        if (isApShimmer)
            holder.shimmer.startShimmer();
        else {
            holder.shimmer.setVisibility(View.INVISIBLE);
            holder.shimmer.stopShimmer();
            holder.shimmer.setShimmer(null);
            holder.dateTime.setBackground(null);
            holder.user.setBackground(null);
            holder.post.setBackground(null);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            ModelClass modelClass = list.get(position);
            holder.dateTime.setText(modelClass.getTime());
            holder.post.setText(modelClass.getPost());
            if (modelClass.getImageUrl() != null) {
                Glide.with(context).load(modelClass.getImageUrl()).into(holder.docs);
                holder.docs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FullImageView.class);
                        intent.putExtra("zoom", modelClass.getImageUrl());
                        context.startActivity(intent);
                    }
                });
            } else {
                holder.docs.setVisibility(View.GONE);
                holder.scam_alert.setVisibility(View.VISIBLE);
            }

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    String mssg = modelClass.getPost();
                    intent.putExtra(Intent.EXTRA_TEXT, mssg);
                    context.startActivity(Intent.createChooser(intent, "Send To"));
                }
            });

            isUpvote(holder.upvote, modelClass.getPostID());
            isDownvote(holder.downvote, modelClass.getPostID());
            getUpvotes(holder.upvote_count, modelClass.getPostID());
            getDownvotes(holder.downvote_count, modelClass.getPostID());

            holder.upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.upvote.getTag().equals("Upvote") && holder.downvote.getTag().equals("Downvote")) {
                        FirebaseDatabase.getInstance().getReference().child("Upvote").child(modelClass.getPostID()).child(firebaseUser.getUid()).setValue(true);
                    } else if (holder.upvote.getTag().equals("Upvote") && holder.downvote.getTag().equals("Downvoted")) {
                        FirebaseDatabase.getInstance().getReference().child("Downvote").child(modelClass.getPostID()).child(firebaseUser.getUid()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Upvote").child(modelClass.getPostID()).child(firebaseUser.getUid()).setValue(true);

                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Upvote").child(modelClass.getPostID()).child(firebaseUser.getUid()).removeValue();
                    }
                }
            });

            holder.downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.upvote.getTag().equals("Upvote") && holder.downvote.getTag().equals("Downvote")) {
                        FirebaseDatabase.getInstance().getReference().child("Downvote").child(modelClass.getPostID()).child(firebaseUser.getUid()).setValue(true);
                    } else if (holder.upvote.getTag().equals("Upvoted") && holder.downvote.getTag().equals("Downvote")) {
                        FirebaseDatabase.getInstance().getReference().child("Upvote").child(modelClass.getPostID()).child(firebaseUser.getUid()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Downvote").child(modelClass.getPostID()).child(firebaseUser.getUid()).setValue(true);

                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Downvote").child(modelClass.getPostID()).child(firebaseUser.getUid()).removeValue();
                    }
                }
            });
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(modelClass.getUserID());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    holder.user.setText(userModel.getName());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("userid", modelClass.getUserID());
                    context.startActivity(intent);
                }
            });

            if (modelClass.getUserID().equals(firebaseUser.getUid())) {
              //  holder.delete.setVisibility(View.VISIBLE);
                holder.edit.setVisibility(View.VISIBLE);
           holder.edit.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
Intent intent =new Intent(context, edit_advertisements.class);
intent.putExtra("uid",modelClass.getUserID());
intent.putExtra("imageurl",modelClass.getImageUrl());
intent.putExtra("time",modelClass.getTime());
intent.putExtra("postid",modelClass.getPostID());
intent.putExtra("post",modelClass.getPost());
context.startActivity(intent);
                                              }
                                          });
                       /*
                        final DialogPlus dialogPlus=DialogPlus.newDialog(context)
                                .setContentHolder(new ViewHolder(R.layout.dialog_content))
                                .setExpanded(true)
                                .create();
                        View view1=dialogPlus.getHolderView();
                        final EditText editPost=view1.findViewById(R.id.edit_post);
                        Button submit=view1.findViewById(R.id.btn_edit);
                        editPost.setText(modelClass.getPost());
                        dialogPlus.show();
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                *//*String postid= modelClass.getPostID();
                                String  ImageUrl= modelClass.getImageUrl();
                                FirebaseDatabase.getInstance().getReference("AdvertisementPosts").child(postid).setValue(null);
                                Calendar calendar=Calendar.getInstance();
                                SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy  HH:mm");
                                String datetime=dateFormat.format(calendar.getTime())+" (edited)";
                                modelClass.setPost(editPost.getText().toString());
                                modelClass.setTime(datetime);
                                modelClass.setImageUrl(ImageUrl);
                                modelClass.setUserID(firebaseUser.getUid());
                                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("AdvertisementPosts");
                                modelClass.setPostID(databaseReference.push().getKey());
                                FirebaseDatabase.getInstance().getReference("AdvertisementPosts").push().setValue(modelClass);
                                dialogPlus.dismiss();
                                            notifyDataSetChanged();  Toast.makeText(context, "Posts Updated!", Toast.LENGTH_SHORT).show();
*//*
                 *//* final Map<String,Object> map=new HashMap<>();
                                map.put("post",editPost.getText().toString());
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("AdvertisementPosts");
                                reference.child(modelClass.getPostID()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists())
                                            reference.child(modelClass.getPostID()).updateChildren(map);
                                        else
                                            reference.child(modelClass.getPostID()).setValue(map);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });*//*


                            }
                        });

                    }
                });*/

            }


        }

    }


    @Override
    public int getItemCount() {
        return isApShimmer ? 5 : list.size();
    }

    public class AdvertiseViewHolder extends RecyclerView.ViewHolder {
        TextView dateTime, post, upvote_count, downvote_count, user, scam_alert;
        ImageView share, upvote, downvote, docs, delete, chat, edit;
        ShimmerFrameLayout shimmer;

        public AdvertiseViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTime = itemView.findViewById(R.id.ap_date);
            post = itemView.findViewById(R.id.ap_post);
            upvote_count = itemView.findViewById(R.id.ap_upvote_count);
            downvote_count = itemView.findViewById(R.id.ap_downvote_count);
            share = itemView.findViewById(R.id.ap_share);
            upvote = itemView.findViewById(R.id.ap_upvote);
            downvote = itemView.findViewById(R.id.ap_downvote);
            user = itemView.findViewById(R.id.ap_username);
            scam_alert = itemView.findViewById(R.id.ap_scam);
            docs = itemView.findViewById(R.id.ap_uploadedDoc);
            shimmer = itemView.findViewById(R.id.ap_shimmer);

            chat = itemView.findViewById(R.id.ap_comment);
            edit = itemView.findViewById(R.id.ap_edit);
        }
    }

    private void isUpvote(ImageView imageView, String postID) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Upvote").child(postID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.upvote_filled);
                    imageView.setTag("Upvoted");
                } else {
                    imageView.setImageResource(R.drawable.upvote_outline);
                    imageView.setTag("Upvote");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void isDownvote(ImageView imageView, String postID) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Downvote").child(postID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.downvote_filled);
                    imageView.setTag("Downvoted");
                } else {
                    imageView.setImageResource(R.drawable.downvote_outline);
                    imageView.setTag("Downvote");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUpvotes(TextView textView, String postID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Upvote").child(postID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (((int) (snapshot.getChildrenCount())) > 0) {
                    String text = String.valueOf(snapshot.getChildrenCount());
                    textView.setText(text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getDownvotes(TextView textView, String postID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Downvote").child(postID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (((int) (snapshot.getChildrenCount())) > 0) {
                    String text = String.valueOf(snapshot.getChildrenCount());
                    textView.setText(text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    }
