package com.getmate.demo181201.FindMateUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

@Layout(R.layout.tinder_card_view)
public class TinderCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;


    //TODO: This tinder mPofile is the profile on which the query is to be performed
    private TinderProfile mProfile;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    Profile currentUserProfile;

    public TinderCard(Context context, TinderProfile profile, SwipePlaceHolderView swipeView) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;

    }
    @Resolve
    private void onResolved(){
        Picasso.get().load(mProfile.getImageUrl()).into(profileImageView);
        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
        locationNameTxt.setText(mProfile.getLocation());
        Log.i("KaunHuMin","OnResolvedCalled");
    }
    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);

    }
    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");

        SharedPreferences mPrefs = mContext.getSharedPreferences("currentUserData",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("currentUserDataInString",null);
         currentUserProfile = gson.fromJson(json,Profile.class);


       ArrayList<String> swipedMeRight =  currentUserProfile.getProfilesSwipedMeRight();
        if (swipedMeRight.contains(mProfile.getId())){
            createConnection(mProfile.getId(),currentUserProfile.getFirebase_id());
        }

        else {
            FirebaseFirestore.getInstance().collection("profiles").
                    document("saamne wale ka user Id")
                    .update("profilesSwipedMeRight",
                            FieldValue.arrayUnion(currentUserProfile.getFirebase_id()));
        }
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }


    public void createConnection(String userId1,String userId2){
        FirebaseFirestore.getInstance().collection("profiles").
                document(userId1).update("connetions",FieldValue.arrayUnion(userId2));
        FirebaseFirestore.getInstance().collection("profiles").
                document(userId2).update("connetions",FieldValue.arrayUnion(userId1));


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id",mProfile.getId());
        hashMap.put("profilePic",mProfile.getImageUrl());
        hashMap.put("name",mProfile.getName());
        hashMap.put("handle","handle");
        FirebaseDatabase.getInstance().getReference().child(currentUserProfile.getFirebase_id()).
                child("connections").child(mProfile.getId()).setValue(hashMap);


        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("id",currentUserProfile.getFirebase_id());
        hashMap1.put("profilePic",currentUserProfile.getProfilePic());
        hashMap1.put("handle",currentUserProfile.getHandle());
        hashMap1.put("name",currentUserProfile.getName());
        FirebaseDatabase.getInstance().getReference().child(mProfile.getId()).
                child("connections").child(currentUserProfile.getFirebase_id()).setValue(hashMap1);
    }
}