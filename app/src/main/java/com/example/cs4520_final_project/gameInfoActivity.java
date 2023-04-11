package com.example.cs4520_final_project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cs4520_final_project.Fragments.HomeScreenFragment;
import com.example.cs4520_final_project.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class gameInfoActivity extends AppCompatActivity  {
    private ImageView play,find;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();




        find=findViewById(R.id.find);

        play=findViewById(R.id.I_play_the_game);


        Intent gameNameIntent= getIntent();
        Bundle from_gameInfo = gameNameIntent.getExtras();

        if(from_gameInfo!=null)
        {

            gameName =(String) from_gameInfo.get("game");
            Log.d(TAG, "onCreate: kankangamename"+gameName);
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Registered Users").child(mUser.getUid()).child("games");
                ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){

                            ArrayList<String>newGameList=(ArrayList<String>)task.getResult().getValue();
                            //ArrayList<String>newGamw=new ArrayList<String>();
                            newGameList.add(gameName);
                            ref.setValue(newGameList);
                            Toast.makeText(gameInfoActivity.this, "it is added!", Toast.LENGTH_LONG).show();

                        }
                    }
                });



            }
        });



        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toFindBud=new Intent(gameInfoActivity.this, findGameBudActivity.class);
                toFindBud.putExtra("game",gameName);
                startActivity(toFindBud);
            }
        });



    }



}