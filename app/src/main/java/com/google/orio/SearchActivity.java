package com.google.orio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.orio.Adapter.ChatsAdapter;
import com.google.orio.Adapter.SearchAdapter;
import com.google.orio.Model.Messages;
import com.google.orio.Model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class SearchActivity extends AppCompatActivity {




    DatabaseReference reference;
    EditText txt;
    List<Messages> mChats=new ArrayList<>();
    List<User> mUsers=new ArrayList<>();
    RecyclerView rec;
    FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rec=findViewById(R.id.recycler_viw);
        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        txt=findViewById(R.id.ptxt);



        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")){
                    Query query=FirebaseDatabase.getInstance().getReference("Chats").orderByChild("message").startAt(s.toString()).endAt(s+"\uf0ff");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            mChats.clear();
                            mUsers.clear();
                            for (DataSnapshot ds:snapshot.getChildren()) {
                                Messages mssg = ds.getValue(Messages.class);

                                if (mssg.getSender().equals(mUser.getUid())){
                                    reference= FirebaseDatabase.getInstance().getReference("Users").child(mssg.getReceiver());
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            User usr=snapshot.getValue(User.class);
                                            mUsers.add(usr);
                                            mChats.add(mssg);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }
                                else if (mssg.getReceiver().equals(mUser.getUid())){
                                    reference= FirebaseDatabase.getInstance().getReference("Users").child(mssg.getSender());
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            User usr=snapshot.getValue(User.class);
                                            mUsers.add(usr);
                                            mChats.add(mssg);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }


                            }

                            SearchAdapter adapter=new SearchAdapter(SearchActivity.this,mChats,mUsers);
                            rec.setAdapter(adapter);


                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}