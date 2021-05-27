package com.google.orio.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.orio.Adapter.ChatsAdapter;
import com.google.orio.Model.Messages;
import com.google.orio.Model.User;
import com.google.orio.R;
import com.google.orio.SearchActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsFragment extends Fragment {

    FirebaseUser mUser;
    RecyclerView rc;
    ChatsAdapter adapter;
    Context mContext;
    ImageButton btn;
    CircleImageView pflimg;
    DatabaseReference reference;
    List<String> user;


    DatabaseReference reference2;


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat,container,false);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        user=new ArrayList<String>();

        btn=view.findViewById(R.id.search_btn);

        rc=view.findViewById(R.id.recycler_view);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(getContext()));
        pflimg=view.findViewById(R.id.pfilimg);

        reference2=FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User usr=snapshot.getValue(User.class);
                if(!usr.getImageURL().equals("default") && getActivity()!=null)
                    Glide.with(getContext()).load(usr.getImageURL()).into(pflimg);
                else
                    pflimg.setImageResource(R.mipmap.ic_launcher_round);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                user.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Messages mssg=ds.getValue(Messages.class);

                    if(mssg.getSender().equals(mUser.getUid()) && (!user.contains(mssg.getReceiver())))
                        user.add(mssg.getReceiver());
                    else if(mssg.getReceiver().equals(mUser.getUid())&&(!user.contains(mssg.getSender())))
                        user.add(mssg.getSender());
                }

                adapter=new ChatsAdapter(getContext(),user);
                rc.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
