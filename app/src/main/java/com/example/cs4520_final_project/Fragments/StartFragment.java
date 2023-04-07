package com.example.cs4520_final_project.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cs4520_final_project.AfterLoginMainActivity;
import com.example.cs4520_final_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {

    private Button login_button;
    private Button register_button;
    private FirebaseAuth mAuth;
    private EditText email_input,password_input;
    private String email,password;
    private IloginFragmentAction mListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragment.IregisterFragmentAction) {
            this.mListener = (IloginFragmentAction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement RegisterRquest");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        email_input=rootView.findViewById(R.id.login_enter_email);
        password_input=rootView.findViewById(R.id.login_enter_password);
        register_button = rootView.findViewById(R.id.buttonOpenRegister);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragmentContainerView,new RegisterFragment(),"register").addToBackStack(null).commit();


            }
        });


        login_button = rootView.findViewById(R.id.Login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = email_input.getText().toString().trim();
                password = password_input.getText().toString().trim();
                if(email.equals("")){
                    email_input.setError("Must input email!");
                }
                if(password.equals("")){
                    password_input.setError("Password must not be empty!");
                }
                if(!email.equals("") && !password.equals("")){
//                    Sign in to the account....
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Login Failed!"+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        mListener.populateMainFragment(mAuth.getCurrentUser());
                                    }
                                }
                            })
                    ;
                }
//                Intent afterLogin = new Intent(getActivity(), AfterLoginMainActivity.class);
//                startActivity(afterLogin);
            }
        });

        return rootView;
    }





    public interface IloginFragmentAction {
        void populateMainFragment(FirebaseUser mUser);
        void populateRegisterFragment();
    }
}