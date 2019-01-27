package com.getmate.demo181201.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getmate.demo181201.FindMateUtils.TinderCard;
import com.getmate.demo181201.FindMateUtils.TinderProfile;
import com.getmate.demo181201.FindMateUtils.Utils;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;


public class FindMate extends Fragment {
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    ArrayList<String> userGoingEventsId =new ArrayList<>();
    ArrayList<Event> userGoingevents = new ArrayList<>();
    ArrayList<String> userIdofSuggestedProfiles = new ArrayList<>();
    ArrayList<Profile> result = new ArrayList<>();

    Profile currentUserProfile =new Profile();
    public FindMate() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Bundle bundle = getArguments();
        if(bundle!=null){
            currentUserProfile = bundle.getParcelable("currentUserProfile");
            if (currentUserProfile!=null){
                userGoingEventsId = currentUserProfile.getSavedEvents();
            }
            else {
                Toast.makeText(getActivity(),"Not Reachable",Toast.LENGTH_LONG).show();
            }

        }

        Toast.makeText(getActivity(),"LoL",Toast.LENGTH_LONG).show();

        View view= inflater.inflate(R.layout.fragment_find_mate, container, false);

        mSwipeView = (SwipePlaceHolderView)view.findViewById(R.id.swipeView);
        mContext = getActivity();


       // getLatestEvent();
       // getProfilesByUserIds(userIdofSuggestedProfiles);



        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

            //TODO:get the querry to get Tinder Profiles
        for(TinderProfile tinderProfile : Utils.loadProfiles(this.getContext())){
            mSwipeView.addView(new TinderCard(mContext, tinderProfile, mSwipeView));

        }

        view.findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSwipeView.doSwipe(false);
            }
        });

        view.findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });






     //   FindMateAlgorithm f = new FindMateAlgorithm(currentUserProfile);
      //  ArrayList<Profile> profilesToBeShown = f.getEventsFromSavedEventIds();
        //TODO:This are profiles to be shown

        return view;
    }





    private void getLatestEvent(){
        //This query returns all the event Object Data that user will be going;

        for (String s:userGoingEventsId) {
            FirebaseFirestore.getInstance().collection("events")
                    .document(s).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Event event = new Event();
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot!=null){
                                    event = documentSnapshot.toObject(Event.class);
                                    userGoingevents.add(event);
                                }
                            }

                        }});
        }



        for (Event e :userGoingevents) {
            userIdofSuggestedProfiles.addAll(e.getGoingProfile());
        }

    }

public void getProfilesByUserIds(ArrayList<String> userIds){

    for (String userId:userIds) {

        FirebaseFirestore.getInstance().collection("profiles").document(userId).get().
                addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snapshot = task.getResult();
                        Profile profile = new Profile();
                        profile = snapshot.toObject(Profile.class);
                        result.add(profile);
                    }
                }
        );

    }



}

}
