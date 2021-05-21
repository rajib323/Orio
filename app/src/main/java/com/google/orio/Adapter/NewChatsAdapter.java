package com.google.orio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.orio.MessageActivity;
import com.google.orio.Model.User;
import com.google.orio.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewChatsAdapter extends RecyclerView.Adapter<NewChatsAdapter.ViewHolder> {

    Context mcon;
    List<User> usersList;

    public NewChatsAdapter(Context mcon,List<User> usersList){
        this.mcon=mcon;
        this.usersList=usersList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcon).inflate(R.layout.user_item,parent,false);
        return new NewChatsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.t.setText(usersList.get(position).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcon, MessageActivity.class);
                intent.putExtra("userid",usersList.get(position).getId());
                intent.putExtra("username",usersList.get(position).getUsername());
                mcon.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView t;
        public ViewHolder(View itemView){
            super(itemView);
            t=itemView.findViewById(R.id.username);
        }

    }
}
