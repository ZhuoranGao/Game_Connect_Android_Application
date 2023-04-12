package com.example.cs4520_final_project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cs4520_final_project.Models.User;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class editProfileActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private DatabaseReference reference;
    private FirebaseUser fUser;
    private StorageReference storageReference;
    private static final int IMAGE_REQUEST=1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private FirebaseAuth mAuth;
    private ImageView avatar_imageView;
    private EditText username_edit,name_edit;
    private Button save;
    private String name,location;
    private Button locate_btn;
    private TextView new_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        avatar_imageView=findViewById(R.id.avatar_imageView);
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        DatabaseReference user_reference = FirebaseDatabase.getInstance().getReference("Registered Users").child(fUser.getUid());


        save=findViewById(R.id.save_btn);
        username_edit=findViewById(R.id.username_edit_input);
        name_edit=findViewById(R.id.name_editProfile_input);
        new_location = findViewById(R.id.location_edit_profile);
        locate_btn = findViewById(R.id.edit_profile_locate_button);

        //load info into fields.
        user_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);


                name_edit.setText(user.getName());
                username_edit.setText(user.getUser_name());
                Log.d(TAG, "onDataChange: " + user.getLocation());
                new_location.setText(user.getLocation());

                if(user.getImageURL().equals("")){
                    avatar_imageView.setImageResource(R.drawable.default_avatar);
                    //Glide.with(getContext()).load(user.getImageURL()).into(avatar_image);
                } else{
                    Glide.with(editProfileActivity.this).load(user.getImageURL()).into(avatar_imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //set up the location manager for locating user.
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(editProfileActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(editProfileActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(editProfileActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        locate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locate_user();
            }
        });


        avatar_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseDatabase  database = FirebaseDatabase.getInstance();
                //DatabaseReference mDatabaseRef = database.getReference();

                openImage();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location=new_location.getText().toString();
                name=name_edit.getText().toString();
                String username = username_edit.getText().toString();
                FirebaseDatabase  database = FirebaseDatabase.getInstance();
                DatabaseReference mDatabaseRef = database.getReference();

                mDatabaseRef.child("Registered Users").child(fUser.getUid()).child("location").setValue(location);
                mDatabaseRef.child("Registered Users").child(fUser.getUid()).child("name").setValue(name);
                mDatabaseRef.child("Registered Users").child(fUser.getUid()).child("user_name").setValue(username);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(editProfileActivity.this,"Successfully Updated Profile!",Toast.LENGTH_SHORT).show();
                    }
                });
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
    //function used to locate the user.
    private void locate_user() {
        if (ActivityCompat.checkSelfPermission(editProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(editProfileActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Double MyLat = location.getLatitude();
                Double MyLong = location.getLongitude();
                Geocoder geocoder = new Geocoder(editProfileActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getAdminArea();
                Log.d(TAG, "onLocationChanged: " + String.valueOf(location.getLongitude()));
                Log.d(TAG, "onLocationChanged: " + String.valueOf(location.getLatitude()));
                //location_textView.setText(String.format("%.3f",location.getLatitude())+", "+String.valueOf(location.getLongitude()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new_location.setText(cityName+" , "+stateName);
                        Toast.makeText(editProfileActivity.this, "Successfully got the new location!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
                        fUser = mAuth.getCurrentUser();
                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();

                            reference= FirebaseDatabase.getInstance().getReference("Registered Users").child(fUser.getUid());


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