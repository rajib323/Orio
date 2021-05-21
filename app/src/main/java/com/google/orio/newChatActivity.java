package com.google.orio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.orio.Adapter.NewChatsAdapter;
import com.google.orio.Model.User;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class newChatActivity extends AppCompatActivity {

    List<User> user;
    NewChatsAdapter adapter;
    EditText ptxt;
    FirebaseUser mUser;
    RecyclerView rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        user= new ArrayList<User>();
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        ptxt=findViewById(R.id.ptxt);
        rec=findViewById(R.id.recycler_view);
        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ptxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")){

                    Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s.toString()).endAt(s+"\uf0ff");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            user.clear();
                            for(DataSnapshot ds:snapshot.getChildren()){
                                User usr=ds.getValue(User.class);
                                if(!usr.getId().equals(mUser.getUid()))
                                    user.add(usr);
                            }


                            adapter=new NewChatsAdapter(newChatActivity.this,user);
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