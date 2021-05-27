package com.google.orio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.orio.MessageActivity;
import com.google.orio.Model.User;
import com.google.orio.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    Context mcon;
    List<String> user;
    DatabaseReference reference;

    public ChatsAdapter(Context mcon,List<String> user){
        this.mcon=mcon;
        this.user=user;
        reference= FirebaseDatabase.getInstance().getReference("Users");
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcon).inflate(R.layout.user_item,parent,false);
        return new ChatsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.rt.setImageResource(R.mipmap.ic_launcher_round);
        String t;
        reference.child(user.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User usr=snapshot.getValue(User.class);
                holder.t.setText(usr.getUsername());
                if(usr.getStatus().equals("online"))
                    holder.status.setImageResource(R.color.blue);
                else if(usr.getStatus().equals("offline"))
                    holder.status.setImageResource(R.color.gray);
                if(!usr.getImageURL().equals("default")&&mcon!=null)
                    Glide.with(mcon).load(usr.getImageURL()).into(holder.rt);
                else
                    holder.rt.setImageResource(R.mipmap.ic_launcher_round);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mcon, MessageActivity.class);
                        intent.putExtra("userid",user.get(position));
                        intent.putExtra("username",usr.getUsername());
                        mcon.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView rt;
        TextView t;
        CircleImageView status;
        public ViewHolder(View itemView){
            super(itemView);
            status=itemView.findViewById(R.id.status);
            rt=itemView.findViewById(R.id.profile_image);
            t=itemView.findViewById(R.id.username);
        }

    }
}
