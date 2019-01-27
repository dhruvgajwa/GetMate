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


public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_users, container, false);

       recyclerView = view.findViewById(R.id.recycler_view);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       mUsers = new ArrayList<>();
       readUsers();
        return view;
    }

    private void readUsers() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //TODO: This "Users" in database is updated when a new connection is formed
        //When a new connection is formed, the basic details of that user is added to added to the
        //reference(firest one/redundant one for now)
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("connections");

        //This is getting all users using this application
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user1 = snapshot.getValue(User.class);
                    if (!user1.getId().equals(user.getUid())){
                        mUsers.add(user1);

                    }
                }
                userAdapter = new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
