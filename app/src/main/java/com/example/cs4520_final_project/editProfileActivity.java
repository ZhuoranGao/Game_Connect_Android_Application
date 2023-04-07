package com.example.cs4520_final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class editProfileActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseUser fuser;
    private StorageReference storageReference;
    private static final int IMAGE_REQUEST=1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private FirebaseAuth mAuth;
    private ImageView avatar_imageView;
    private EditText name_edit,location_edit;
    private Button save;
    private String name,location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        avatar_imageView=findViewById(R.id.avatar_imageView);
        storageReference= FirebaseStorage.getInstance().getReference("uploads");

        save=findViewById(R.id.save_btn);
        name_edit=findViewById(R.id.name_edit_input);
        location_edit=findViewById(R.id.location_editProfile_input);
        avatar_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseDatabase  database = FirebaseDatabase.getInstance();
                //DatabaseReference mDatabaseRef = database.getReference();

                //mDatabaseRef.child("Registered Users").child(fuser.getUid()).child("email").setValue("123");

                openImage();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location=location_edit.getText().toString();
                name=name_edit.getText().toString();
                FirebaseDatabase  database = FirebaseDatabase.getInstance();
                DatabaseReference mDatabaseRef = database.getReference();

                mDatabaseRef.child("Registered Users").child(fuser.getUid()).child("Location").setValue(location);
                mDatabaseRef.child("Registered Users").child(fuser.getUid()).child("name").setValue(name);
            }
        });

    }





    /////////////////open gallary f phone\\\\\\\\\\\\\\
    private void openImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }



    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadImage(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if(imageUri!=null){
            final StorageReference fileReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        mAuth = FirebaseAuth.getInstance();
                        fuser = mAuth.getCurrentUser();
                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();

                            reference= FirebaseDatabase.getInstance().getReference("Registered Users").child(fuser.getUid());


                        HashMap<String,Object> map=new HashMap<>();
                        map.put("imageURL",mUri);
                        reference.updateChildren(map);
                        Glide.with(editProfileActivity.this).load(mUri).into(avatar_imageView);






                        pd.dismiss();

                    }else{
                        Toast.makeText(editProfileActivity.this,"Failed",Toast.LENGTH_SHORT).show();

                        pd.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(editProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else{
            Toast.makeText(editProfileActivity.this,"No image selected",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST&&resultCode==RESULT_OK
                &&data!=null&&data.getData()!=null){
            imageUri=data.getData();
            if(uploadTask!=null&&uploadTask.isInProgress()){
                Toast.makeText(this,"Upload in progress",Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
    }

}