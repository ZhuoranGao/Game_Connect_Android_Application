package com.example.cs4520_final_project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cs4520_final_project.Fragments.HomeScreenFragment;
import com.example.cs4520_final_project.Models.Game;
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
    private Game curr_game;
    private ImageView play,find;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private DatabaseReference ref;
    private String gameName;

    private TextView game_title,player_number;
    private ImageView game_banner;
    private Button go_back_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        // get the game
        curr_game = (Game) getIntent().getSerializableExtra("game");

        //match with the variables.
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        game_banner = findViewById(R.id.game_banner_info);
        game_title = findViewById(R.id.game_title_info);
        player_number = findViewById(R.id.number_of_player);

        Glide.with(gameInfoActivity.this).load(curr_game.image_URL).into(game_banner);
        game_title.setText(curr_game.getName());
        //player_number.setText(String.valueOf(curr_game.getPlayer_number()));

        ref = FirebaseDatabase.getInstance().getReference("Games").child(String.valueOf(curr_game.getApp_id()));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Game curr_game = snapshot.getValue(Game.class);
                int player_num = curr_game.getPlayer_number();
                player_number.setText(String.valueOf(player_num));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        find=findViewById(R.id.find);

        play=findViewById(R.id.I_play_the_game);

        /**
        Intent gameNameIntent= getIntent();
        Bundle from_gameInfo = gameNameIntent.getExtras();

        if(from_gameInfo!=null)
        {

            gameName =(String) from_gameInfo.get("game");
            Log.d(TAG, "onCreate: kankangamename"+gameName);
        }**/

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
                            // check if the user already added the game
                            for(String game : newGameList){
                                if (game.equals(curr_game.name)){
                                    Toast.makeText(gameInfoActivity.this, "You have already added!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            //increase the number of player_number
                            DatabaseReference ref_game = database.getReference("Games").child(String.valueOf(curr_game.getApp_id())).child("player_number");

                            curr_game.player_number = curr_game.player_number + 1;
                            newGameList.add(curr_game.name);
                            ref.setValue(newGameList);
                            ref_game.setValue(curr_game.player_number);
                            player_number.setText(String.valueOf(curr_game.getPlayer_number()));
                            Toast.makeText(gameInfoActivity.this, "You have added this game to your list!", Toast.LENGTH_LONG).show();

                        }
                    }
                });



            }
        });



        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toFindBud=new Intent(gameInfoActivity.this, findGameBudActivity.class);
                toFindBud.putExtra("game",curr_game.name);
                startActivity(toFindBud);
            }
        });



    }



}