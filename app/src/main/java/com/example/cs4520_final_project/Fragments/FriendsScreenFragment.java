package com.example.cs4520_final_project.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cs4520_final_project.Adapters.FriendScreen_User_Adapter;
import com.example.cs4520_final_project.Adapters.HomeScreen_Game_Adapter;
import com.example.cs4520_final_project.Models.Game;
import com.example.cs4520_final_project.Models.User;
import com.example.cs4520_final_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsScreenFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ArrayList<User> friends;
    private ArrayList<String> friends_uid;

    private RecyclerView friends_list_recycler;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendsScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsScreenFragment newInstance(String param1, String param2) {
        FriendsScreenFragment fragment = new FriendsScreenFragment();
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
        currentUser = mAuth.getCurrentUser();
        DatabaseReference friends_reference = FirebaseDatabase.getInstance().getReference("Registered Users").child(currentUser.getUid()).child("friend");
        friends_reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                friends_uid = (ArrayList<String>) task.getResult().getValue();
                Log.d("final", "onComplete: "+friends_uid);
            }
        });
        readFriends();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  = inflater.inflate(R.layout.fragment_friends_screen, container, false);

        friends = new ArrayList<User>();

        friends_list_recycler = rootView.findViewById(R.id.recyclerView_friends);
        friends_list_recycler.setLayoutManager(new LinearLayoutManager(getContext()));


        return rootView;
    }


    public void readFriends(){

        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("Registered Users");
        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    User user = snap.getValue(User.class);
                    assert  user != null;
                    //exclude the current user himself.
                    if(!user.getUid().equals(currentUser.getUid()) && friends_uid.contains(user.getUid())){
                        friends.add(user);
                    }
                }
                FriendScreen_User_Adapter userAdapter = new FriendScreen_User_Adapter(getContext(),friends);
                userAdapter.setmContext(getActivity());
                friends_list_recycler.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}