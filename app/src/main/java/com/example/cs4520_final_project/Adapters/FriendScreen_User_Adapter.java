package com.example.cs4520_final_project.Adapters;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;

public class FriendScreen_User_Adapter extends RecyclerView.Adapter<FriendScreen_User_Adapter.FriendScreenUserViewHolder> {
    private Context mContext;
    private String TAG = "FINAL";

    private ArrayList<User> friends;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;



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
    public void onBindViewHolder(@NonNull FriendScreenUserViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + friends);
        User curr_user = friends.get(position);
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
                friends.remove(current_position);
                notifyDataSetChanged();
                Log.d("final", "onClick delete: " + friends);

                curr_act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //notifyDataSetChanged();
                        //notifyItemRemoved(current_position);
                        //notifyItemRangeChanged (current_position, getItemCount());
                        Toast.makeText(view.getContext(),"Successfully deleted a friend!",Toast.LENGTH_SHORT ).show();
                    }
                });



                // do the change to the database.
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Registered Users").child(mUser.getUid()).child("friend");

                reference.addValueEventListener(new ValueEventListener() {
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
                //notifyDataSetChanged();
                /**
                curr_act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                        //notifyItemRemoved(position);
                        //notifyItemRangeChanged(position, friends.size());
                        Toast.makeText(view.getContext(),"Successfully deleted a friend!",Toast.LENGTH_SHORT ).show();
                    }
                });**/





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
