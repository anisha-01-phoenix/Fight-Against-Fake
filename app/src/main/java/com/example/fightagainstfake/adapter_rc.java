package com.example.fightagainstfake;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightagainstfake.Posts.Activities.ChatActivity;
import com.example.fightagainstfake.admin_package.admin_recv_adapter;

import java.util.ArrayList;

public class adapter_rc extends RecyclerView.Adapter<adapter_rc.v5holder> {
    ArrayList<model_recentchants>data;
    Context context;

    public adapter_rc(ArrayList<model_recentchants> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public v5holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recentchats, parent, false);
        return new adapter_rc.v5holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull v5holder holder, int position) {
holder.username.setText(data.get(position).getUsername());
holder.name.setText(data.get(position).getName());
holder.statusonline.setText(data.get(position).getStatusonline());
holder.cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context, ChatActivity.class);
        intent.putExtra("userid",data.get(position).getFrontuserid());
        context.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class v5holder extends RecyclerView.ViewHolder {
        TextView statusonline,username,name;
        CardView cardView;
        public v5holder(@NonNull View itemView) {
            super(itemView);
            statusonline=itemView.findViewById(R.id.status);
            name=itemView.findViewById(R.id.Name);
            username=itemView.findViewById(R.id.username_rc);
            cardView=itemView.findViewById(R.id.cvgh);
        }
    }
}
