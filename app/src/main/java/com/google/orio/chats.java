package com.google.orio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.orio.Fragments.ChatsFragment;
import com.google.orio.Fragments.ProfileFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.lang.ref.Reference;
import java.util.HashMap;

public class chats extends AppCompatActivity{

    ChipNavigationBar chip;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        chip=findViewById(R.id.chip_bar);

        fab=findViewById(R.id.fab);
        getSupportFragmentManager().beginTransaction().replace(R.id.root_layout,new ChatsFragment()).commit();
        chip.setItemSelected(R.id.chat,true);
        chip.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment frag=null;
                switch(i){
                    case R.id.profile:
                        frag=new ProfileFragment();
                        break;
                    case R.id.chat:
                        frag=new ChatsFragment();
                        break;
                }

                chip.setItemSelected(i,true);
                getSupportFragmentManager().beginTransaction().replace(R.id.root_layout,frag).commit();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(chats.this,newChatActivity.class);
                startActivity(intent);
            }
        });
    }

}