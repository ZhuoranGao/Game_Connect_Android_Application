package com.example.cs4520_final_project.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4520_final_project.R;

import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder> {

    private ArrayList<User> users;

    public userAdapter(ArrayList<User> users) {
        this.users = users;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private  final TextView name_card,locationCard;
        private final Button add_card_btn,profile_card_btn;

        public TextView getName_card() {
            return name_card;
        }

        public TextView getLocationCard() {
            return locationCard;
        }

        public Button getAdd_card_btn() {
            return add_card_btn;
        }

        public Button getProfile_card_btn() {
            return profile_card_btn;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name_card = itemView.findViewById(R.id.name_on_card);
            this.locationCard = itemView.findViewById(R.id.location_card);
            this.add_card_btn = itemView.findViewById(R.id.add_btn_card);
            this.profile_card_btn = itemView.findViewById(R.id.profile_card);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.ViewHolder holder, int position) {

        holder.getName_card().setText(users.get(position).getName());
        holder.getLocationCard().setText(users.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
