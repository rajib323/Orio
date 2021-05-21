package com.google.orio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.orio.MessageActivity;
import com.google.orio.Model.Messages;
import com.google.orio.Model.User;
import com.google.orio.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private Context mContext;
    private List<Messages> mChats;

    FirebaseUser mUser;

    public MessageAdapter(Context mContext,List<Messages> mChats){
        this.mContext=mContext;
        this.mChats=mChats;
        mUser=FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType==0){
            View view= LayoutInflater.from(mContext).inflate(R.layout.chatitem_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else{
            View view=LayoutInflater.from(mContext).inflate(R.layout.chatitem_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.t.setText(mChats.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mUser.getUid().equals(mChats.get(position).getSender()))
            return 0;
        else
            return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView t;
        public ViewHolder(View itemView){
            super(itemView);

            t=itemView.findViewById(R.id.messg);
        }
    }
}
