package com.example.cs4520_final_project.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cs4520_final_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        logout_btn = rootView.findViewById(R.id.log_out_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.logoutPressed();
            }
        });

        return rootView;
    }


    public interface IProfileFragmentButtonAction {
        void logoutPressed();
    }
}