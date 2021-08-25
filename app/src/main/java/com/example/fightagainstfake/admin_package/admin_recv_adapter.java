package com.example.fightagainstfake.admin_package;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightagainstfake.R;
import com.example.fightagainstfake.complaints.adapter;
import com.example.fightagainstfake.complaints.model;

import java.util.ArrayList;

public class admin_recv_adapter extends RecyclerView.Adapter<admin_recv_adapter.vholder> {
    ArrayList<model_admin_recv> data;
    Context context;

    public admin_recv_adapter(ArrayList<model_admin_recv> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_admin_single_row, parent, false);
        return new admin_recv_adapter.vholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull vholder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
        holder.complaintId.setText(data.get(position).getComplaintId());
        holder.status.setText(data.get(position).getStatus());
        holder.date.setText(data.get(position).getDate());
        holder.username.setText(data.get(position).getUsername());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,complaints_details.class);
               intent.putExtra("date",data.get(position).getDate());
               intent.putExtra("status",data.get(position).getStatus());
               intent.putExtra("compid",data.get(position).getComplaintId());
               intent.putExtra("title",data.get(position).getTitle());
               intent.putExtra("username",data.get(position).getUsername());
               intent.putExtra("uid",data.get(position).getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class vholder extends RecyclerView.ViewHolder {
        TextView status,complaintId,title,date,username;
        CardView cardView;
        public vholder(@NonNull View itemView) {
            super(itemView);
            status=itemView.findViewById(R.id.status_admin);
            date=itemView.findViewById(R.id.com_date_admin);
            complaintId=itemView.findViewById(R.id.id_admin);
            title=itemView.findViewById(R.id.titlecompalint_admin);
            username=itemView.findViewById(R.id.username_admin);
            cardView=itemView.findViewById(R.id.cvg);
        }
    }
}
