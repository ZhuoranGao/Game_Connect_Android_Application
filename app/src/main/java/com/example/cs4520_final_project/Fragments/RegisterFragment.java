package com.example.cs4520_final_project.Fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cs4520_final_project.Models.User;
import com.example.cs4520_final_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ImageView register_select_avatar;
    private EditText register_email,register_username,register_name,register_password,register_password2;
    private Button register_submit_btn,register_locate_btn;
    private String email,username,name,password,rep_password;
    private IregisterFragmentAction mListener;
    private LocationManager locationManager;
    private TextView location_textView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();

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
        location_textView=rootView.findViewById(R.id.location_textView);
        locationManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d(TAG, "onLocationChanged: "+String.valueOf(location.getLongitude()));
                Log.d(TAG, "onLocationChanged: "+String.valueOf(location.getLatitude()));
                location_textView.setText(String.format("%.3f",location.getLatitude())+", "+String.valueOf(location.getLongitude()));
            }
        });



        //
        register_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_register();
            }
        });






        return rootView;
    }

    private void submit_register(){
        //get inputs from text fields.
        this.username = String.valueOf(register_username.getText()).trim();
        this.email = String.valueOf(register_email.getText()).trim();
        this.password = String.valueOf(register_password.getText()).trim();
        this.rep_password = String.valueOf(register_password2.getText()).trim();
        this.name = String.valueOf(register_name.getText()).trim();

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
                        User new_user = new User(uid,name,username, email);
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
}