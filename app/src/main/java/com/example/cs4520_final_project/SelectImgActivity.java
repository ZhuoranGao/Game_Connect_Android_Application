package com.example.cs4520_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class SelectImgActivity extends AppCompatActivity {
    private String sender_uid,receiver_uid;

    private ImageView select_image_button;
    private Button send_img_btn;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    StorageReference storageReference;
    DatabaseReference reference;
    FirebaseUser fuser;
    int SELECT_IMG_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_img);
        // get passed info from the chat Activity.
        Bundle bundle = getIntent().getExtras();
        this.sender_uid = bundle.getString("sender");
        this.receiver_uid = bundle.getString("receiver");
        storageReference = FirebaseStorage.getInstance().getReference("image_uploads");

        //bind components in layout.
        select_image_button = findViewById(R.id.select_img_button);
        send_img_btn = findViewById(R.id.button_send_img);

        select_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        send_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendImage(sender_uid,receiver_uid,imageUri);
                uploadImage();
            }
        });



    }

    public void openImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Title"),SELECT_IMG_CODE);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void sendImage(String sender,String receiver,Uri image_to_send){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("image_message", image_to_send);

        reference.child("Chats").push().setValue(hashMap);


        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(receiver_uid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(receiver_uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(receiver_uid)
                .child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid());

        //final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(SelectImgActivity.this);
        pd.setMessage("Uploading");
        //pd.show();
        Log.d("final", "trying to uploadImage: ");
        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("sender", sender_uid);
                        hashMap.put("receiver", receiver_uid);
                        hashMap.put("image_message", mUri);

                        reference.child("Chats").push().setValue(hashMap);


                        pd.dismiss();
                    } else {
                        Toast.makeText(SelectImgActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SelectImgActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(SelectImgActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null){
            imageUri = data.getData();
            select_image_button.setImageURI(imageUri);
            Log.d("final", "onActivityResult: " + "set img!!"+String.valueOf(imageUri));

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(SelectImgActivity.this, "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
                onBackPressed();
            }


        }
    }


}