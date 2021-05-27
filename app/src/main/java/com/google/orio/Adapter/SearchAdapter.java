package com.google.orio.Adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.orio.MessageActivity;
import com.google.orio.Model.Messages;
import com.google.orio.Model.User;
import com.google.orio.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    List<Messages> mChats;
    List<User> mUser;
    Context mCon;


    public SearchAdapter(Context mCon,List<Messages> mChats,List<User> mUser){
        this.mChats=mChats;
        this.mCon=mCon;
        this.mUser=mUser;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mCon).inflate(R.layout.search_item,parent,false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.t.setText(mUser.get(position).getUsername());
        holder.ts.setText(mChats.get(position).getMessage());
        if(!mUser.get(position).getImageURL().equals("default")){
            Glide.with(mCon).load(mUser.get(position).getImageURL()).into(holder.rt);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mCon, MessageActivity.class);
                intent.putExtra("userid",mUser.get(position).getId());
                intent.putExtra("username",mUser.get(position).getUsername());
                mCon.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView t;
        CircleImageView rt;
        TextView ts;
        public ViewHolder(View itemView){
            super(itemView);
            t=itemView.findViewById(R.id.username);
            rt=itemView.findViewById(R.id.profile_image);
            ts=itemView.findViewById(R.id.mssgpart);
        }

    }
}
