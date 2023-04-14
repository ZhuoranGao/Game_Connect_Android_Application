package com.example.cs4520_final_project.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cs4520_final_project.Models.Chat;
import com.example.cs4520_final_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String receiver_display_name;

            FirebaseUser fuser;
            DatabaseReference db;

    public MessageAdapter(Context mContext, List<Chat> mChat, String chat_user_name){
            this.mChat = mChat;
            this.mContext = mContext;
            this.receiver_display_name = chat_user_name;
            }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            db = FirebaseDatabase.getInstance().getReference("Registered Users");

            if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
            } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
            }
            }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

            Chat chat = mChat.get(position);



            //if the image_message exists, means this is an image message.
            if (chat.getImage_message() != null){
            Glide.with(mContext).load(chat.getImage_message()).into(holder.image_message);
            holder.show_message.setVisibility(View.GONE);
            } else{
            holder.show_message.setText(chat.getMessage());
            holder.image_message.setVisibility(View.GONE);
            }




            Log.d("final", "onBindViewHolder: " + receiver_display_name);

            if(getItemViewType(position) == MSG_TYPE_RIGHT ){
            holder.user_display_name.setText(fuser.getDisplayName());

            } else{
            holder.user_display_name.setText(receiver_display_name);

            }




            }

    @Override
    public int getItemCount() {
            return mChat.size();
            }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public TextView user_display_name;

        public ImageView image_message;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            user_display_name= itemView.findViewById(R.id.chat_history_user_id);
            image_message = itemView.findViewById(R.id.sent_image_chat);
        }
    }

        @Override
        public int getItemViewType(int position) {
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            if (mChat.get(position).getSender().equals(fuser.getUid())){
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
        }
}
