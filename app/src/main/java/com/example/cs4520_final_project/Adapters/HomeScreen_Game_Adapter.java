package com.example.cs4520_final_project.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cs4520_final_project.Models.Game;
import com.example.cs4520_final_project.Models.User;
import com.example.cs4520_final_project.R;

import java.util.ArrayList;

public class HomeScreen_Game_Adapter extends RecyclerView.Adapter<HomeScreen_Game_Adapter.HomeScreenGameViewHolder>{
    private Context mContext;
    private ArrayList<Game> games;




    public HomeScreen_Game_Adapter(Context mcontext, ArrayList<Game> game) {
        super();
        mContext = mcontext;
        this.games = game;
    }

    @NonNull
    @Override
    public HomeScreenGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.homescreen_game_cardview,parent,false);


        return new HomeScreenGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeScreenGameViewHolder holder, int position) {
        Game curr_game = games.get(position);

        String banner_url = curr_game.getImage_URL();
        holder.game_name.setText(curr_game.getName());
        Glide.with(mContext).load(banner_url).into(holder.game_banner);

    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }


    public class HomeScreenGameViewHolder extends RecyclerView.ViewHolder{

        public TextView game_name;
        public ImageView game_banner;


        public HomeScreenGameViewHolder(@NonNull View itemView){
                super(itemView);
                game_banner = itemView.findViewById(R.id.game_banner_homescreen);
                game_name = itemView.findViewById(R.id.game_name_HomeScreen);

        }

    }
}
