package com.example.cs4520_final_project.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cs4520_final_project.Models.Game;
import com.example.cs4520_final_project.Models.User;
import com.example.cs4520_final_project.R;
import com.example.cs4520_final_project.editProfileActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    private static final int IMAGE_REQUEST=1;
    private Uri imageUri;


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private StorageTask uploadTask;




    private ImageView register_select_avatar;
    private EditText register_email, register_username, register_name, register_password, register_password2;
    private Button register_submit_btn, register_locate_btn;
    private String email, username, name, password, rep_password,location;
    private IregisterFragmentAction mListener;
    private LocationManager locationManager;
    private TextView location_textView;

    private String uploaded_img_url;




    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("uploads");

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IregisterFragmentAction) {
            this.mListener = (IregisterFragmentAction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement RegisterRquest");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        //connect with the components:
        //EditText register_email,register_username,register_name,register_password,register_password2;
        register_email = rootView.findViewById(R.id.register_email);
        register_username = rootView.findViewById(R.id.register_username);
        register_name = rootView.findViewById(R.id.register_name);
        register_password = rootView.findViewById(R.id.register_password);
        register_password2 = rootView.findViewById(R.id.register_password2);
        register_submit_btn = rootView.findViewById(R.id.register_submission_button);
        location_textView = rootView.findViewById(R.id.location_textView);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        register_select_avatar = rootView.findViewById(R.id.register_select_image);

        //implement the function of locate button.
        register_locate_btn = rootView.findViewById(R.id.button_register_location);
        register_locate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locate_user();
            }
        });

        //implement the function of uploading the profile image when registering.
        register_select_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });


        //implement the function of the submitt button.
        register_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_register();
            }
        });


        return rootView;
    }

    private void locate_user() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        location_textView.setText(cityName+" , "+stateName);
                    }
                });
            }
        });

    }


    private void submit_register(){
        //get inputs from text fields.
        this.username = String.valueOf(register_username.getText()).trim();
        this.email = String.valueOf(register_email.getText()).trim();
        this.password = String.valueOf(register_password.getText()).trim();
        this.rep_password = String.valueOf(register_password2.getText()).trim();
        this.name = String.valueOf(register_name.getText()).trim();
        this.location = String.valueOf(location_textView.getText()).trim();

        // check inputs are not null.
        if(username.equals("")){
            register_username.setError("Must input username!");
        }
        if(email.equals("")){
            register_email.setError("Must input email!");
        }
        if(password.equals("")){
            register_password.setError("Password must not be empty!");
        }
        if(!rep_password.equals(password)){
            register_password2.setError("Passwords must match!");
        }
        if(name.equals("")){
            register_name.setError("Must input first name!");
        }
        if(location.equals("Click the button to get location.")){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"Please provide a location.",Toast.LENGTH_SHORT).show();
                }
            });
        }

        //validation complete, then :
        if(!username.equals("") && !email.equals("")
                && !password.equals("")
                && rep_password.equals(password)){
                //create user in the Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mUser = task.getResult().getUser();

//                                    Adding name to the FirebaseUser...
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();
                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                        String uid = mAuth.getUid();
                        //String uid, String name, String user_name, String email, String location, String imageURL
                        ArrayList<String> games=new ArrayList<String>();
                        ArrayList<String> friends=new ArrayList<String>();
                        games.add("default");
                        friends.add("default");


                        User new_user = new User(uid,name,username, email,location,uploaded_img_url,games,friends);
                        referenceProfile.child(mUser.getUid()).setValue(new_user);//.addOnCompleteListener(new OnCompleteListener<Void>() {



                        mUser.updateProfile(profileChangeRequest)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            mListener.registerDone(mUser);
                                        }
                                    }
                                });

                    }


                }
            });





        }

    }
    public interface IregisterFragmentAction {
        void registerDone(FirebaseUser mUser);
    }

    private void openImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd=new ProgressDialog(getContext());
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
                        mUser = mAuth.getCurrentUser();
                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();

                        //reference= FirebaseDatabase.getInstance().getReference("Registered Users").child(mUser.getUid());


                        //HashMap<String,Object> map=new HashMap<>();
                        //map.put("imageURL",mUri);
                        //reference.updateChildren(map);
                        uploaded_img_url = mUri;
                        Glide.with(getContext()).load(mUri).into(register_select_avatar);



                        pd.dismiss();

                    }else{
                        Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();

                        pd.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else{
            Toast.makeText(getContext(),"No image selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST&&resultCode==RESULT_OK
                &&data!=null&&data.getData()!=null){
            imageUri=data.getData();
            if(uploadTask!=null&&uploadTask.isInProgress()){
                Toast.makeText(getContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
    }



}