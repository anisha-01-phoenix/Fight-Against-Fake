package com.example.fightagainstfake.admin_package;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class v2holder extends RecyclerView.ViewHolder {
        TextView date,post;
        public v2holder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(com.example.fightagainstfake.R.id.com_date_admin_info);
            post=itemView.findViewById(com.example.fightagainstfake.R.id.titlecompalint_admin_info);
        }
    }
}
