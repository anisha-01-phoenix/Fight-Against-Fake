package com.example.fightagainstfake.complaints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightagainstfake.R;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.vh> {

    ArrayList<model> data;
    Context context;

    public adapter(ArrayList<model> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_sr, parent, false);
        return new adapter.vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull vh holder, int position) {

        String date=data.get(position).getDatetime();
        String id=data.get(position).getComplainId();
        String title=data.get(position).getComplaintTitle();
        String status=data.get(position).getStatus();
        String uid=data.get(position).getUid();

        holder.title.setText(title);
        holder.complaintId.setText(id);
        holder.status.setText(status);
        holder.date.setText(date);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class vh extends RecyclerView.ViewHolder {

        TextView status,complaintId,title,date;

        public vh(@NonNull View itemView) {
            super(itemView);

            status=itemView.findViewById(R.id.status);
            date=itemView.findViewById(R.id.com_date);
            complaintId=itemView.findViewById(R.id.id);
            title=itemView.findViewById(R.id.titlecompalint);

        }
    }
}
