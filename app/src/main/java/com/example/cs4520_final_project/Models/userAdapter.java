package com.example.cs4520_final_project.Models;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4520_final_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder> {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ArrayList<User> users;
    //private Button add_btn;

    public userAdapter(ArrayList<User> users) {
        this.users = users;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private  final TextView name_card,locationCard;
        private final Button add_card_btn;
        private final ImageView card_image;

        public TextView getName_card() {
            return name_card;
        }

        public TextView getLocationCard() {
            return locationCard;
        }

        public Button getAdd_card_btn() {
            return add_card_btn;
        }

//        public Button getProfile_card_btn() {
//            return profile_card_btn;
//        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name_card = itemView.findViewById(R.id.username_on_card);
            this.locationCard = itemView.findViewById(R.id.location_card);
            this.add_card_btn = itemView.findViewById(R.id.chat_btn_friendScreen);
            //this.profile_card_btn = itemView.findViewById(R.id.profile_card);
            this.card_image = itemView.findViewById(R.id.avatar_image_friend);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.getName_card().setText(users.get(position).getName());
        holder.getLocationCard().setText(users.get(position).getLocation());
        holder.getAdd_card_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=users.get(position).getUid();
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Registered Users").child(mUser.getUid()).child("friend");
                ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){

                            ArrayList<String>newFriendsList=(ArrayList<String>)task.getResult().getValue();
                            //ArrayList<String>newGamw=new ArrayList<String>();
                            Log.d(TAG, "onComplete: "+newFriendsList.size());
                            newFriendsList.add(id);
                            ref.setValue(newFriendsList);
                            Toast.makeText(view.getContext(), "it is added!", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
