package com.google.orio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btn,butn;
    TextView policy;
    String url="https://github.com/adi323/adi323.github.io";
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Intent intent=new Intent(MainActivity.this,Home.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn=findViewById(R.id.login);
        butn=findViewById(R.id.signup);
        policy=findViewById(R.id.policy);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_login();
            }
        });


        butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_signup();
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_policy();
            }
        });
    }

    private void open_policy() {
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void open_signup() {
        Intent intent=new Intent(this,Signup.class);
        startActivity(intent);
    }

    private void open_login() {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}