package com.google.orio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference  reference;
    EditText username,password,pin;
    MaterialButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth= FirebaseAuth.getInstance();
        btn=findViewById(R.id.butn);
        username=findViewById(R.id.usrname);
        password=findViewById(R.id.pssword);
        pin=findViewById(R.id.spin);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(username.getText().toString())||TextUtils.isEmpty(password.getText().toString())||TextUtils.isEmpty(pin.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Fields are missing",Toast.LENGTH_SHORT).show();
                else if(password.getText().toString().length()<6)
                    Toast.makeText(getApplicationContext(),"Password too short",Toast.LENGTH_SHORT).show();
                else if(pin.getText().toString().length()<4)
                    Toast.makeText(getApplicationContext(),"Pin must be at least 4 characters",Toast.LENGTH_SHORT).show();
                else
                    register();
            }
        });
    }

    private void register() {
        mAuth.createUserWithEmailAndPassword(
                username.getText().toString(),
                password.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser=mAuth.getCurrentUser();
                    assert firebaseUser!=null;
                    String userid = firebaseUser.getUid();
                    Toast.makeText(getApplicationContext(),"SignUp Success",Toast.LENGTH_SHORT).show();
                    reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",username.getText().toString());
                    hashMap.put("pin",pin.getText().toString());
                    hashMap.put("imageURL","default");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent=new Intent(Signup.this,chats.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Database Problem",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"SignUpFailed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}