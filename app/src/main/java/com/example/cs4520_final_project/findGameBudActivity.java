package com.example.cs4520_final_project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.cs4520_final_project.Models.Game;
import com.example.cs4520_final_project.Models.User;
import com.example.cs4520_final_project.Models.userAdapter;
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

public class findGameBudActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private userAdapter useradapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> users;
    private ArrayList<String> games;
    private String game_extra;
    private Button add_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_game_bud);
        add_btn=findViewById(R.id.add_btn_card);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        users=new ArrayList<User>();
        games=new ArrayList<String>();
        games.add("cs");

        //users.add(new User("123","name","username","email","location","imageurl",games));

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("Registered Users");

        //////////////get extra///////////
        Intent gameNameIntent= getIntent();
        Bundle from_gameInfo = gameNameIntent.getExtras();

        if(from_gameInfo!=null)
        {
            game_extra =(String) from_gameInfo.get("game");

        }
        /////////////////////////

        usersdRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        User user = ds.getValue(User.class);
                        for(int i=0;i<user.getGames().size();i++){
                            Log.d(TAG, "onComplete: "+user.getGames().get(i)+game_extra);
                            if(user.getGames().get(i).equals(game_extra)){
                                Log.d(TAG, "onComplete: "+user.getName()+usersdRef.child(mUser.getUid()).child("name"));

                            if(!user.getUid().equals(mUser.getUid())){
                                    users.add(user);

                                    break;
                                }

                            }


                        }
                    }
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }



                recyclerView=findViewById(R.id.userRecyclerView);
                layoutManager=new LinearLayoutManager(findGameBudActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                useradapter=new userAdapter(users);

                recyclerView.setAdapter(useradapter);
            }
        });






//        ValueEventListener eventListener = new ValueEventListener(){
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot ds : snapshot.getChildren()){
//                    ArrayList<String>gamelist=(ArrayList<String>)ds.child(mUser.getUid()).child("games").getValue();
//                    Log.d(TAG, "onDataChange: "+gamelist);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };



    }
}