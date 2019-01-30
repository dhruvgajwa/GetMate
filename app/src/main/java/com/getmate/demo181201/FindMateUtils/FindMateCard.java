package com.getmate.demo181201.FindMateUtils;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getmate.demo181201.CustomViews.EventTimelineImageView;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
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

import de.hdodenhof.circleimageview.CircleImageView;

@Layout(R.layout.find_mate_card_view)
public class FindMateCard {


    @View(R.id.circle_profile_image)
    private CircleImageView circleProfileImage;

    @View(R.id.background_image)
    private EventTimelineImageView backgroungImage;

    @View(R.id.name_FMC)
    private TextView name;


    @View(R.id.tagline_FMC)
    private TextView tagline;

    @View(R.id.city_FMC)
    private TextView city;

    @View(R.id.handle_FMC)
    private TextView handle;


    @View(R.id.tagsView)
    private FlexboxLayout tagsView;

    @View(R.id.bio1)
    private TextView bio1;

    @View(R.id.bio2)
    private TextView bio2;

    @View(R.id.bio3)
    private TextView bio3;




    private Profile recommendeduserprofile;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    Profile currentUserProfile;




    public FindMateCard(Context context, Profile profile, SwipePlaceHolderView swipeView) {
        mContext = context;
        recommendeduserprofile = profile;
        mSwipeView = swipeView;
    }






    @Resolve
    private void onResolved(){

        Picasso.get().load(recommendeduserprofile.getProfilePic()).into(backgroungImage);
        Picasso.get().load(recommendeduserprofile.getProfilePic()).into(circleProfileImage);
        name.setText(recommendeduserprofile.getName());
        tagline.setText(recommendeduserprofile.getTagline());
        city.setText(recommendeduserprofile.getCity());
        handle.setText(recommendeduserprofile.getHandle());




        tagsView.setVisibility(android.view.View.VISIBLE);
        int  tagsCount =recommendeduserprofile.getAllInterests().size() ;//... integer number of textviews
        TextView[] tages= new TextView[tagsCount];//create dynamic textviewsarray
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //loop to customize the text view and add it to flex box
        for (int i = 0; i < tagsCount; i++) {
            tages[i] = new TextView(mContext);
            GradientDrawable gD = new GradientDrawable();
            int strokeWidth = 5;
            int strokeColor = mContext.getResources().getColor(R.color.grey);

            gD.setStroke(strokeWidth, strokeColor);
            gD.setCornerRadius(15);
            gD.setShape(GradientDrawable.RECTANGLE);

            tages[i].setBackground(gD);
            tages[i].setText(recommendeduserprofile.getAllInterests().get(i));
            layoutParams.setMargins(28, 8, 28, 8);
            tages[i].setPadding(24, 15, 24, 15);
            tagsView.addView(tages[i], layoutParams);
        }




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

     /*   SharedPreferences mPrefs = mContext.getSharedPreferences("currentUserData",MODE_PRIVATE);
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
        */
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
/*
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
    */
    }

}