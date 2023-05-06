package com.example.cs4520_final_project.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cs4520_final_project.ChatActivity;
import com.example.cs4520_final_project.Models.User;
import com.example.cs4520_final_project.Models.userAdapter;
import com.example.cs4520_final_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;

public class FriendScreen_User_Adapter extends RecyclerView.Adapter<FriendScreen_User_Adapter.FriendScreenUserViewHolder> {
    private Context mContext;
    private String TAG = "FINAL";

    private ArrayList<User> friends;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;
    User curr_user;


    public FriendScreen_User_Adapter(Context mContext, ArrayList<User> friends) {
        this.mContext = mContext;
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendScreenUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.friendsscreen_user_cardview,parent,false);
        return new FriendScreenUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendScreenUserViewHolder holder, int position){
        Log.d(TAG, "onBindViewHolder: " + friends);
        curr_user = friends.get(position);
        holder.location.setText(curr_user.getLocation());
        //holder.uid.setText(curr_user.getUid());
        holder.username.setText(curr_user.getUser_name());

        if(!curr_user.getImageURL().equals("")){
            Glide.with(mContext).load(curr_user.getImageURL()).into(holder.avatar_image);
        }

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity curr_act = (AppCompatActivity) view.getContext();
                //delete the value from local
                int current_position = holder.getAdapterPosition();
                Log.d(TAG, "onClick: "+friends);



                ///
                // do the change to the database.
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Registered Users").child(mUser.getUid()).child("friend");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        friends.clear();
                        for(DataSnapshot uid:snapshot.getChildren()){
                           if(uid.getValue().equals(curr_user.getUid())){
                                uid.getRef().removeValue();
                               //notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //reference.addValueEventListener(ValueEventListener);
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        //friends.clear();
//                        for(DataSnapshot uid:snapshot.getChildren()){
//                            if(uid.getValue().equals(curr_user.getUid())){
//                                uid.getRef().removeValue();
//                                //notifyDataSetChanged();
//                            }
//                        }
//                        friends.clear();
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                ///


                friends.remove(current_position);
                //view.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onClick: "+friends);
                notifyItemRemoved(current_position);
                notifyItemRangeChanged(current_position, friends.size());
                //notifyDataSetChanged();
                Log.d("final", "onClick delete: " + friends);

                curr_act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(view.getContext(),"Successfully deleted a friend!",Toast.LENGTH_SHORT ).show();
                    }
                });



//                // do the change to the database.
//                mAuth = FirebaseAuth.getInstance();
//                mUser = mAuth.getCurrentUser();
//                reference = FirebaseDatabase.getInstance().getReference("Registered Users").child(mUser.getUid()).child("friend");
//                //reference.addValueEventListener(ValueEventListener);
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        //friends.clear();
//                        for(DataSnapshot uid:snapshot.getChildren()){
//                            if(uid.getValue().equals(curr_user.getUid())){
//                                uid.getRef().removeValue();
//                                //notifyDataSetChanged();
//                            }
//                        }
//                        friends.clear();
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            }
        });

        holder.chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity curr_act = (AppCompatActivity) view.getContext();
                FragmentManager fm = curr_act.getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("user_to_talk_display_name", String.valueOf(curr_user.getUser_name()));
                bundle.putString("user_to_talk_email", String.valueOf(curr_user.getEmail()));
                bundle.putString("user_to_talk_uid", String.valueOf(curr_user.getUid()));


                //ChatFragment to_chat = new ChatFragment();
                //to_chat.setArguments(bundle);

                //fm.beginTransaction().replace(R.id.fragmentContainerView_InClass8,to_chat,"starting chat").addToBackStack(null).commit();

                Intent i = new Intent(mContext, ChatActivity.class);
                i.putExtras(bundle);
                mContext.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }
//
//    @Override
//    public void onDataChange(@NonNull DataSnapshot snapshot) {
//        friends.clear();
//        for(DataSnapshot uid:snapshot.getChildren()){
//            if(uid.getValue().equals(curr_user.getUid())){
//                uid.getRef().removeValue();
//                notifyDataSetChanged();
//            }
//        }
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError error) {
//
//    }


    public class FriendScreenUserViewHolder extends RecyclerView.ViewHolder{
        public ImageView avatar_image;
        public TextView uid,location,username;

        public Button chat_btn,delete_btn;

        public FriendScreenUserViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar_image = itemView.findViewById(R.id.avatar_image_friend);
            //uid = itemView.findViewById(R.id.uid_friendScreen);
            username = itemView.findViewById(R.id.username_on_card);
            location = itemView.findViewById(R.id.location_card);
            chat_btn = itemView.findViewById(R.id.chat_btn_friendScreen);
            delete_btn = itemView.findViewById(R.id.delete_btn_friendScreen);
        }
    }
}
