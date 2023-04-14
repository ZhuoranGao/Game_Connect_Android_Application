package com.example.cs4520_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cs4520_final_project.Adapters.MessageAdapter;
import com.example.cs4520_final_project.Models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private String chat_user_display_name,chat_user_email,chat_uid;

    private String TAG = "final";

    private FirebaseUser user;
    private DatabaseReference db;
    private ImageButton btn_send;
    private EditText text_send;
    private RecyclerView recyclerView_chat_record;

    private ImageButton btn_img_send;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        btn_send = findViewById(R.id.chat_btn_send);
        text_send = findViewById(R.id.text_send);
        Bundle extras = getIntent().getExtras();

        chat_user_display_name = extras.getString("user_to_talk_display_name");
        chat_user_email = extras.getString("user_to_talk_email");
        // chat_uid = uid of the user "you" are talking to.
        chat_uid = extras.getString("user_to_talk_uid");
        setTitle("Talking to: "+ chat_user_display_name);

        //set up the user and database
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("Registered Users").child(chat_uid);

        recyclerView_chat_record = findViewById(R.id.recyclerView_chat_record);
        recyclerView_chat_record.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView_chat_record.setLayoutManager(linearLayoutManager);

        db = FirebaseDatabase.getInstance().getReference("Registered Users").child(chat_uid);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //User curr_user = dataSnapshot.getValue(User.class);
                //username.setText(user.getUsername());

                readMesagges(user.getUid(), chat_uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(user.getUid(), chat_uid, msg);
                    text_send.setText("");
                } else {

                    Toast.makeText(ChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                    text_send.setText("");

                }
            }
        });

        btn_img_send = findViewById(R.id.img_btn_send);
        btn_img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChatActivity.this,SelectImgActivity.class);
                Bundle b = new Bundle();
                b.putString("sender",user.getUid());
                b.putString("receiver",chat_uid);
                i.putExtras(b);
                startActivity(i);
            }
        });



    }

    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);


        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(user.getUid())
                .child(chat_uid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(chat_uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(chat_uid)
                .child(user.getUid());
        chatRefReceiver.child("id").setValue(user.getUid());

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                /**
                 if (notify) {
                 sendNotifiaction(receiver, user.getUsername(), msg);
                 }
                 notify = false;
                 **/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMesagges(final String myid, final String userid){
        mchat = new ArrayList<>();

        db = FirebaseDatabase.getInstance().getReference("Chats");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(ChatActivity.this, mchat,chat_user_display_name);
                    recyclerView_chat_record.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}