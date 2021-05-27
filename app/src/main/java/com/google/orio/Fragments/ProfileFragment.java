package com.google.orio.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.orio.MainActivity;
import com.google.orio.Model.User;
import com.google.orio.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    private static final int IMAGE_REQUEST=1;
    TextView profile_id;
    FirebaseUser usr;
    CircleImageView profile_img;
    StorageReference storageRef;
    DatabaseReference reference;
    TextView logout;
    private Uri image_url;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        mAuth= FirebaseAuth.getInstance();
        logout=view.findViewById(R.id.logout);
        usr=FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Users").child(usr.getUid());
        profile_id=view.findViewById(R.id.profile_id);
        profile_img=view.findViewById(R.id.profile_img);
        storageRef = FirebaseStorage.getInstance().getReference("ProfileImg");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if(!user.getImageURL().equals("default") && getActivity()!=null)
                    Glide.with(getContext()).load(user.getImageURL()).into(profile_img);
                else
                    profile_img.setImageResource(R.mipmap.ic_launcher_round);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });




        profile_id.setText(mAuth.getCurrentUser().getEmail());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }

    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }


    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mipmap=MimeTypeMap.getSingleton();
        return mipmap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadImage(){
        ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();
        if(image_url!=null){
            final StorageReference fileReference=storageRef.child(System.currentTimeMillis()+"."+getFileExtension(image_url));
            uploadTask=fileReference.putFile(image_url);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                        throw task.getException();
                    return fileReference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        String url=downloadUri.toString();
                        HashMap<String, Object> hashMap=new HashMap<>();
                        hashMap.put("imageURL",url);
                        reference.updateChildren(hashMap);
                        pd.dismiss();
                        getActivity().finish();
                    }
                    else{
                        Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
        else
            Toast.makeText(getContext(),"no image selected",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMAGE_REQUEST &&resultCode==RESULT_OK &&data!=null&&data.getData()!=null){
            image_url=data.getData();
            uploadImage();
        }
    }
}
