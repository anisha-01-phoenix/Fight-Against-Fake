package com.example.fightagainstfake.Posts.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightagainstfake.ModelClass;
import com.example.fightagainstfake.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.NormalViewHolder> {

    Context context;
    ArrayList<ModelClass> list;
    FirebaseUser firebaseUser;
    public boolean shimmering=true;

    public NormalAdapter(Context context, ArrayList<ModelClass> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public NormalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.normal_post_item,parent,false);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NormalViewHolder holder, int position) {
        if (shimmering)
        {
            holder.shimmer.startShimmer();
        }
        else {
            holder.shimmer.stopShimmer();
            holder.shimmer.setShimmer(null);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            ModelClass modelClass = list.get(position);
            holder.dateTime.setBackground(null);
            holder.post.setBackground(null);
            holder.dateTime.setText(modelClass.getTime());
            holder.post.setText(modelClass.getPost());
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
        }

       }


    @Override
    public int getItemCount() {

        return shimmering? 10: list.size();
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder{
        ShimmerFrameLayout shimmer;
        TextView dateTime, post, upvote_count, downvote_count;
        ImageView share, upvote, downvote;
        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTime=itemView.findViewById(R.id.np_date);
            post=itemView.findViewById(R.id.np_post);
            upvote_count=itemView.findViewById(R.id.np_upvote_count);
            downvote_count=itemView.findViewById(R.id.np_downvote_count);
            share=itemView.findViewById(R.id.np_share);
            upvote=itemView.findViewById(R.id.np_upvote);
            downvote=itemView.findViewById(R.id.np_downvote);
            shimmer=itemView.findViewById(R.id.np_shimmer);
        }
    }

    private void isUpvote(ImageView imageView, String postID)
    {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Upvote").child(postID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists())
                {
                    imageView.setImageResource(R.drawable.upvote_filled);
                    imageView.setTag("Upvoted");
                }
                else
                {
                    imageView.setImageResource(R.drawable.upvote_outline);
                    imageView.setTag("Upvote");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void isDownvote(ImageView imageView, String postID)
    {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Downvote").child(postID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists())
                {
                    imageView.setImageResource(R.drawable.downvote_filled);
                    imageView.setTag("Downvoted");
                }
                else
                {
                    imageView.setImageResource(R.drawable.downvote_outline);
                    imageView.setTag("Downvote");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUpvotes(TextView textView, String postID)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Upvote").child(postID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (((int) (snapshot.getChildrenCount()))>0) {
                    String text = String.valueOf(snapshot.getChildrenCount());
                    textView.setText(text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getDownvotes(TextView textView, String postID)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Downvote").child(postID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {if (((int) (snapshot.getChildrenCount()))>0) {
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
