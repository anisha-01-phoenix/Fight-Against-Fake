package com.example.fightagainstfake.admin_package;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fightagainstfake.Posts.Activities.FullImageView;
import com.example.fightagainstfake.R;

import java.util.ArrayList;

public class admin_info_corner_adapter extends  RecyclerView.Adapter<admin_info_corner_adapter.v2holder>{
    ArrayList<model_info_corner> data;
    Context context;

    public admin_info_corner_adapter(ArrayList<model_info_corner> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public v2holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_info_corner, parent, false);
        return new admin_info_corner_adapter.v2holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull v2holder holder, int position) {
        holder.date.setText(data.get(position).getDate());
        holder.post.setText(data.get(position).getPostdata());

        if (data.get(position).getImgUrl()!=null) {
            holder.info_pic.setVisibility(View.VISIBLE);
            Glide.with(context).load(data.get(position).getImgUrl()).into(holder.info_pic);
            holder.info_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, FullImageView.class);
                    intent.putExtra("zoom",data.get(position).getImgUrl());
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class v2holder extends RecyclerView.ViewHolder {
        TextView date,post;
        ImageView info_pic;
        public v2holder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(com.example.fightagainstfake.R.id.com_date_admin_info);
            post=itemView.findViewById(com.example.fightagainstfake.R.id.titlecompalint_admin_info);
            info_pic=itemView.findViewById(R.id.info_img);
        }
    }
}
