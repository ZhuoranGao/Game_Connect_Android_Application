package com.example.cs4520_final_project.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cs4520_final_project.Adapters.HomeScreen_Game_Adapter;
import com.example.cs4520_final_project.Models.Game;
import com.example.cs4520_final_project.R;
import com.example.cs4520_final_project.editProfileActivity;
import com.example.cs4520_final_project.gameInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreenFragment extends Fragment {
    private RecyclerView homeScreen_recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference storageReference;
    private DatabaseReference reference;

    private Button try_btn;


    private HomeScreen_Game_Adapter gamesAdapter;
    private ArrayList<Game> games;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    public HomeScreenFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static HomeScreenFragment newInstance() {
        HomeScreenFragment fragment = new HomeScreenFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference= FirebaseDatabase.getInstance().getReference("Games");
        readGames();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);

        try_btn = rootView.findViewById(R.id.test_temp_btn);
        try_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEditProfile = new Intent(getContext(), gameInfoActivity.class);
                String gameName = "00000000000";
                toEditProfile.putExtra("game", gameName);
                startActivity(toEditProfile);
            }
        });

        // implement the RecyclerView
        homeScreen_recyclerView = rootView.findViewById(R.id.recyclerView_HomeScreen);
        homeScreen_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        games = new ArrayList<Game>();

        return rootView;

    }

    public void readGames(){
        //FirebaseUser logUser = mUser;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Games");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // users.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Game game = snap.getValue(Game.class);
                    assert  game != null;
                    //exclude the current user himself.
                    games.add(game);
                }
                HomeScreen_Game_Adapter gameAdapter = new HomeScreen_Game_Adapter(getContext(),games);
                gameAdapter.setmContext(getActivity());
                homeScreen_recyclerView.setAdapter(gameAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}