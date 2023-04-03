package com.example.cs4520_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;

import com.example.cs4520_final_project.Fragments.FriendsScreenFragment;
import com.example.cs4520_final_project.Fragments.HomeScreenFragment;
import com.example.cs4520_final_project.Fragments.ProfileScreenFragment;
import com.example.cs4520_final_project.databinding.ActivityAfterLoginMainBinding;

public class AfterLoginMainActivity extends AppCompatActivity {
     ActivityAfterLoginMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_after_login_main);
        binding = ActivityAfterLoginMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        ft.replace(R.id.fragmentContainerView5,fragment);
        ft.commit();

    }

}