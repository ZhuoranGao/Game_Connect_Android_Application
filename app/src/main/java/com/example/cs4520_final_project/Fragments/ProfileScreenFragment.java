package com.example.cs4520_final_project.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cs4520_final_project.Models.User;
import com.example.cs4520_final_project.R;
import com.example.cs4520_final_project.editProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileScreenFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private Button editProfile_btn;

    private TextView userName,location;
    private String userName_input,location_Input;
    private ImageView imageView;

    private IProfileFragmentButtonAction mListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Button logout_btn;



    public ProfileScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileScreenFragment newInstance(String param1, String param2) {
        ProfileScreenFragment fragment = new ProfileScreenFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            //readUsers();
        } else {

            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();

            //readUsers();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IProfileFragmentButtonAction){
            mListener = (IProfileFragmentButtonAction) context;
        }else{
            throw new RuntimeException(context.toString()+ "must implement IaddButtonAction");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        editProfile_btn=rootView.findViewById(R.id.save_btn);

        imageView=rootView.findViewById(R.id.avatar_imageView);
        userName=rootView.findViewById(R.id.userName_profile);
        location=rootView.findViewById(R.id.location_profile);
/////////////load current profile info/////////////////

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Registered Users").child(mUser.getUid());


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);

                location.setText(user.getLocation());
                userName.setText(user.getUser_name());
                if(user.getImageURL()!=null){
                    Glide.with(getContext()).load(user.getImageURL()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



     /////////////////////////////////////








        logout_btn = rootView.findViewById(R.id.log_out_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.logoutPressed();
            }
        });



        editProfile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toEditProfile=new Intent(getContext(), editProfileActivity.class);
                startActivity(toEditProfile);
            }
        });

        return rootView;
    }


    public interface IProfileFragmentButtonAction {
        void logoutPressed();
    }
}