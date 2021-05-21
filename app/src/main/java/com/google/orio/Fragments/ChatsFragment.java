package com.google.orio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.orio.Adapter.ChatsAdapter;
import com.google.orio.Model.Messages;
import com.google.orio.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    FirebaseUser mUser;
    RecyclerView rc;
    ChatsAdapter adapter;
    DatabaseReference reference;
    List<String> user;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat,container,false);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        user=new ArrayList<String>();
        rc=view.findViewById(R.id.recycler_view);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(getContext()));


        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                user.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Messages mssg=ds.getValue(Messages.class);
                    if(mssg.getSender().equals(mUser.getUid()) && (!user.contains(mssg.getReceiver())))
                        user.add(mssg.getReceiver());
                    else if(mssg.getSender().equals(mUser.getUid())&&(!user.contains(mssg.getReceiver())))
                        user.add(mssg.getSender());
                }

                adapter=new ChatsAdapter(getContext(),user);
                rc.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }
}
