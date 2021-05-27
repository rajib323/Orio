package com.google.orio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.orio.Adapter.MessageAdapter;
import com.google.orio.Model.Messages;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {


    RecyclerView rec;
    TextView prof;
    EditText msg;
    CircleImageView img;
    FloatingActionButton snd;
    MaterialToolbar toolbar;
    FirebaseUser usr;
    List<Messages> mChats;
    MessageAdapter adapter;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar=findViewById(R.id.toolbar);
        usr= FirebaseAuth.getInstance().getCurrentUser();
        mChats=new ArrayList<Messages>();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String receiver=getIntent().getStringExtra("username");
        String receiverId=getIntent().getStringExtra("userid");
        rec=findViewById(R.id.recyclerview);
        img=findViewById(R.id.profileimg);
        prof=findViewById(R.id.profileid);
        msg=findViewById(R.id.mssg);
        snd=findViewById(R.id.send);
        prof.setText(receiver);

        rec.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        //linearLayoutManager.getStackFromEnd();
        rec.setLayoutManager(linearLayoutManager);
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mChats.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Messages mssg=ds.getValue(Messages.class);
                    //Toast.makeText(MessageActivity.this, mssg.getMessage(), Toast.LENGTH_SHORT).show();
                    if((mssg.getSender().equals(receiverId)&&mssg.getReceiver().equals(usr.getUid()))||(mssg.getReceiver().equals(receiverId)&&mssg.getSender().equals(usr.getUid()))){
                        mChats.add(mssg);
                    }
                }
                if(!mChats.isEmpty()){
                    adapter=new MessageAdapter(MessageActivity.this,mChats);
                    rec.setAdapter(adapter);
                    rec.scrollToPosition(mChats.size()-1);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        snd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(!msg.getText().toString().equals("")){
                    DatabaseReference r2=FirebaseDatabase.getInstance().getReference();
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("receiver",receiverId);
                    hashMap.put("sender",usr.getUid());
                    hashMap.put("message",msg.getText().toString());
                    Date cur= Calendar.getInstance().getTime();
                    r2.child("Chats").push().setValue(hashMap);
                    msg.setText("");
                }
            }
        });


    }
}