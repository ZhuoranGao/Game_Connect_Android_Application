package com.example.cs4520_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.cs4520_final_project.Fragments.FriendsScreenFragment;
import com.example.cs4520_final_project.Fragments.HomeScreenFragment;
import com.example.cs4520_final_project.Fragments.ProfileScreenFragment;
import com.example.cs4520_final_project.databinding.ActivityAfterLoginMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AfterLoginMainActivity extends AppCompatActivity implements ProfileScreenFragment.IProfileFragmentButtonAction {
     ActivityAfterLoginMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAfterLoginMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.Home_Nav:
                    replaceFrag(new HomeScreenFragment());
                    break;
                case R.id.Friends_Nav:
                    replaceFrag(new FriendsScreenFragment());
                    break;
                case R.id.Profile_Nav:
                    replaceFrag(new ProfileScreenFragment());
                    break;

            }

            return true;
        });


    }
    private void replaceFrag(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainerView_AfterLogin,fragment);
        ft.commit();
    }
    @Override
    public void logoutPressed() {
        mAuth.signOut();
        currentUser = null;
        populateScreen();
    }

    private void populateScreen() {
        //      Check for Authenticated users ....
        Log.d("final", "populateScreen: "+mAuth.toString());
        if(currentUser != null){
            //The user is authenticated, Populating The Main Fragment....
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView_AfterLogin, HomeScreenFragment.newInstance(),"HomeScreenFragment")
                    .commit();

        }else{
//            The user is not logged in, load the login Fragment....
            Intent toBeforeLogin = new Intent(AfterLoginMainActivity.this, MainActivity.class);
            startActivity(toBeforeLogin);
        }
    }

}