package com.example.cs4520_final_project.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cs4520_final_project.R;
import com.example.cs4520_final_project.editProfileActivity;
import com.example.cs4520_final_project.gameInfoActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreenFragment extends Fragment {


    private Button try_btn;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_home_screen, container, false);

        try_btn=rootView.findViewById(R.id.test_temp_btn);
        try_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEditProfile=new Intent(getContext(), gameInfoActivity.class);
                String gameName="00000000000";
                toEditProfile.putExtra("game",gameName);
                startActivity(toEditProfile);
            }
        });
        return rootView;
    }

}