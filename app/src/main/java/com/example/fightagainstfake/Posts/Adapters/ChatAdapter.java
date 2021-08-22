package com.example.fightagainstfake.Posts.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightagainstfake.Chat;
import com.example.fightagainstfake.databinding.MyChatsLayoutBinding;
import com.example.fightagainstfake.databinding.OthersChatsLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Chat> list;

    public ChatAdapter(Context context, ArrayList<Chat> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(firebaseUser.getUid()))
            return 0;
        else
            return 1;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        if (viewType==0)
        {
            MyChatsLayoutBinding myChatsLayoutBinding=MyChatsLayoutBinding.inflate(layoutInflater,parent,false);
            return new MyMssgViewHolder(myChatsLayoutBinding);
        }
        else
        {
            OthersChatsLayoutBinding othersChatsLayoutBinding=OthersChatsLayoutBinding.inflate(layoutInflater,parent,false);
            return new OthersMssgViewHolder(othersChatsLayoutBinding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyMssgViewHolder)
        {
            MyMssgViewHolder myMssgViewHolder=(MyMssgViewHolder) holder;
            myMssgViewHolder.myChatsLayoutBinding.setMyChat(list.get(position));
        }
        else
        {
            OthersMssgViewHolder othersMssgViewHolder=(OthersMssgViewHolder) holder;
            othersMssgViewHolder.othersChatsLayoutBinding.setOtherChat(list.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyMssgViewHolder extends RecyclerView.ViewHolder{
        MyChatsLayoutBinding myChatsLayoutBinding;
        public MyMssgViewHolder(MyChatsLayoutBinding myChatsLayoutBinding) {
            super(myChatsLayoutBinding.getRoot());
            this.myChatsLayoutBinding=myChatsLayoutBinding;
        }
    }

    public class OthersMssgViewHolder extends RecyclerView.ViewHolder{
        OthersChatsLayoutBinding othersChatsLayoutBinding;
        public OthersMssgViewHolder(OthersChatsLayoutBinding othersChatsLayoutBinding) {
            super(othersChatsLayoutBinding.getRoot());
            this.othersChatsLayoutBinding=othersChatsLayoutBinding;
        }
    }
}
