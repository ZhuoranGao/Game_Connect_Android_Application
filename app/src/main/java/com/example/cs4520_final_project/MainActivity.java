package com.example.cs4520_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.cs4520_final_project.Fragments.HomeScreenFragment;
import com.example.cs4520_final_project.Fragments.RegisterFragment;
import com.example.cs4520_final_project.Fragments.StartFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements RegisterFragment.IregisterFragmentAction,StartFragment.IloginFragmentAction {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("GamerConnect");
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        populateScreen();
    }


    private void populateScreen() {
        //      Check for Authenticated users ....
        Log.d("final", "populateScreen: "+mAuth.toString());
        if(currentUser != null) {
            //The user is authenticated, Populating The Main Fragment....
            /**
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, HomeScreenFragment.newInstance(), "mainFragment")
                    .commit();**/
            Intent toAfterLogin = new Intent(MainActivity.this, AfterLoginMainActivity.class);
            startActivity(toAfterLogin);


        }
    }

    @Override
    public void registerDone(FirebaseUser mUser) {
        this.currentUser = mUser;
        populateScreen();
    }


    @Override
    public void populateMainFragment(FirebaseUser mUser) {

        this.currentUser = mUser;
        populateScreen();
    }

    @Override
    public void populateRegisterFragment() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, RegisterFragment.newInstance(),"registerFragment")
                .commit();
    }
}