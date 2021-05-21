package com.google.orio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.orio.Fragments.ChatsFragment;
import com.google.orio.Fragments.ProfileFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class chats extends AppCompatActivity {

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
                        chip.setItemSelected(i,true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.root_layout,new ProfileFragment()).commit();
                        break;
                    case R.id.chat:
                        chip.setItemSelected(i,true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.root_layout,new ChatsFragment()).commit();
                        break;
                }
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