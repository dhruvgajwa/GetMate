package com.getmate.demo181201.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.demo181201.Adapters.MessagingAdapter;
import com.getmate.demo181201.Model.Chat;
import com.getmate.demo181201.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    Button send;
    EditText text_send;

    MessagingAdapter messagingAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    String connectionuUserId, handle, profilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.basil_orange));
        }
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
        connectionuUserId= intent.getStringExtra("userid");
        handle = intent.getStringExtra("handle");
        profilePicUrl = intent.getStringExtra("profilePic");
        username.setText(handle);
        Picasso.get().load(profilePicUrl).into(profile_image);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().
                child(firebaseUser.getUid()).child(connectionuUserId).child("Chats");

        //  reference = FirebaseDatabase.getInstance().getReference("Users").child(connectionuUserId);
        readMessage(firebaseUser.getUid(),connectionuUserId,profilePicUrl);

         Boolean forEventSharing = getIntent().getBooleanExtra("forSharingEvent",false);
         if (forEventSharing){
             String message = getIntent().getStringExtra("messgaeData");
             sendMessage(firebaseUser.getUid(),connectionuUserId,message);
         }



         send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String message = text_send.getText().toString();
                 if (!message.equals("")){
                     sendMessage(firebaseUser.getUid(),connectionuUserId,message);
                 }
                 else {
                     Toast.makeText(getApplicationContext(),"Please don't send emply message",Toast.LENGTH_LONG).show();
                 }
                 text_send.setText("");
             }
         });

    }


    private void sendMessage(String sender, final String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child(firebaseUser.getUid()).child(connectionuUserId)
                .child("Chats").push().setValue(hashMap);
        reference.child(connectionuUserId).child(firebaseUser.getUid())
                .child("Chats").push().setValue(hashMap);


    }



    private void readMessage(final String myId, final String userId, final String imageUrl){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().
                child(myId).child(userId).child("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChat.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    mChat.add(chat);

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
