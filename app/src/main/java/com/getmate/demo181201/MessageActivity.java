package com.getmate.demo181201;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.demo181201.Adapters.MessagingAdapter;
import com.getmate.demo181201.Model.Chat;
import com.getmate.demo181201.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    ImageButton send;
    EditText text_send;

    MessagingAdapter messagingAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

         profile_image = findViewById(R.id.profileImage);
         username = findViewById(R.id.userName_ma);
         send = findViewById(R.id.btn_send);
         text_send = findViewById(R.id.text_send);
         intent = getIntent();
         userId= intent.getStringExtra("userid");
         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
         reference = FirebaseDatabase.getInstance().getReference().
                 child(firebaseUser.getUid()).child(userId).child("Chats");

         reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);


         send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String message = text_send.getText().toString();
                 if (!message.equals("")){
                     sendMessage(firebaseUser.getUid(),userId,message);
                 }
                 else {
                     Toast.makeText(getApplicationContext(),"Please don't send emply message",Toast.LENGTH_LONG).show();
                 }
                 text_send.setText("");
             }
         });

         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 User user = dataSnapshot.getValue(User.class);
                 if (user!=null){
                     username.setText(user.getUsername());
                 }
                 if (user.getImageUrl().equals("default")){
                     profile_image.setImageResource(R.mipmap.ic_launcher);
                 }

                 readMessage(firebaseUser.getUid(),userId,user.getImageUrl());
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
    }


    private void sendMessage(String sender, final String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);


        final DatabaseReference charRef = FirebaseDatabase.getInstance().
                getReference("Chatlist").child(firebaseUser.getUid()).child(userId);

        charRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    charRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
               // sendNotification(receiver,user.getUsername(),msg);
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void readMessage(final String myId, final String userId, final String imageUrl){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChat.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId)
                            || chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
                        mChat.add(chat);

                    }
                }
            messagingAdapter = new MessagingAdapter(MessageActivity.this,mChat,imageUrl);
                recyclerView.setAdapter(messagingAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
