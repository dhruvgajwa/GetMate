package com.getmate.demo181201.ChatsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getmate.demo181201.Adapters.UserAdapter;
import com.getmate.demo181201.Model.User;
import com.getmate.demo181201.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



//TODO: This activity is useless!!

public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    FirebaseUser fuser;
    DatabaseReference reference;
    String messageForSharing = null;

    public ChatsFragment(){}
    public ChatsFragment(String  text){
        this.messageForSharing = text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view= inflater.inflate(R.layout.fragment_chats, container, false);
            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child(fuser.getUid()).child("connections");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User connectionObject = snapshot.getValue(User.class);
                    mUsers.add(connectionObject);
                }

                if (messageForSharing!=null){
                    userAdapter = new UserAdapter(getContext(),mUsers,messageForSharing);
                }
                else {
                    userAdapter = new UserAdapter(getContext(),mUsers);
                }

                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }









}
