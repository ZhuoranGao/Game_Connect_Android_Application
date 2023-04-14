package com.example.cs4520_final_project.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4520_final_project.Models.Game;
import com.example.cs4520_final_project.R;

import java.util.ArrayList;

public class ProfileScreen_Game_Adapter extends RecyclerView.Adapter<ProfileScreen_Game_Adapter.ProfileScreenGameViewHolder> {

    private Context mContext;
    private ArrayList<String> games;

    public ProfileScreen_Game_Adapter(Context mcontext, ArrayList<String> game) {
        super();
        mContext = mcontext;
        this.games = game;
    }

    @NonNull
    @Override
    public ProfileScreenGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_screen_game_cardview,parent,false);
        return new ProfileScreenGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileScreenGameViewHolder holder, int position) {
        String curr_game_title = games.get(position);

        holder.game_name.setText(curr_game_title);

    }

    @Override
    public int getItemCount() {
        return games.size();
    }


    public class ProfileScreenGameViewHolder extends RecyclerView.ViewHolder{

        public TextView game_name;


        public ProfileScreenGameViewHolder(@NonNull View itemView){
            super(itemView);
            game_name = itemView.findViewById(R.id.game_title_profile);

        }

    }


}
