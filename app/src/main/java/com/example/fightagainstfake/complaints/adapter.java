package com.example.fightagainstfake.complaints;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightagainstfake.Posts.Activities.FullImageView;
import com.example.fightagainstfake.R;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.viewHolderComplaints> {

    ArrayList<model> data;
    Context context;



    public adapter(ArrayList<model> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderComplaints onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_sr, parent, false);
        return new viewHolderComplaints(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderComplaints holder, int position) {

        String date=data.get(position).getDatetime();
        String id=data.get(position).getComplainId();
        String title=data.get(position).getComplaintTitle();
        String status=data.get(position).getStatus();
        String proof=data.get(position).getProof();
       // String uid=data.get(position).getUid();

        holder.title.setText(title);
        holder.complaintId.setText(id);
        holder.status.setText(status);
        holder.date.setText(date);
        holder.proof.setText(proof);


        if (status.equals("Accepted"))
            holder.status.setTextColor(context.getResources().getColor(R.color.accepted));
        if (status.equals("In Progress"))
            holder.status.setTextColor(context.getResources().getColor(R.color.in_progress));
        if (status.equals("Completed"))
            holder.status.setTextColor(context.getResources().getColor(R.color.Completed));
        if (status.equals("Rejected"))
            holder.status.setTextColor(context.getResources().getColor(R.color.Rejected));

        if (data.get(position).getProofurl()!=null)
        {
            holder.chkProof.setVisibility(View.VISIBLE);
            String imgUrl1=data.get(position).getProofurl();

            holder.chkProof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullImageView.class);
                    intent.putExtra("zoom", imgUrl1);
                    context.startActivity(intent);
                }
            });

        }
        else
            holder.chkProof.setVisibility(View.INVISIBLE);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public  void filterList(ArrayList<model>filteredList){

        data=filteredList;
        notifyDataSetChanged();
    }



    public class viewHolderComplaints extends RecyclerView.ViewHolder {

        TextView status,complaintId,title,date,proof,chkProof;

        public viewHolderComplaints(@NonNull View itemView) {
            super(itemView);

            status=itemView.findViewById(R.id.status);
            date=itemView.findViewById(R.id.com_date);
            complaintId=itemView.findViewById(R.id.id);
            title=itemView.findViewById(R.id.titlecompalint);
            proof=itemView.findViewById(R.id.proof);
            chkProof=itemView.findViewById(R.id.chk_proof);

        }
    }
}
