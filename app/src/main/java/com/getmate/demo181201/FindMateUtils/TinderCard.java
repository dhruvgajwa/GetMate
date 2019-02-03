package com.getmate.demo181201.FindMateUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getmate.demo181201.Model.User;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

@Layout(R.layout.tinder_card_view)
public class TinderCard {


    @View(R.id.circle_profile_i)
    private CircleImageView circleImageView;


    @View(R.id.name_)
    private TextView name;


    @View(R.id.tagline_)
    private TextView tagline;

    @View(R.id.city_)
    private TextView city;

    @View(R.id.handle_)
    private TextView handle;

    @View(R.id.background_)
    private ImageView backgroundImage;


    @View(R.id.tags_view)
    private FlexboxLayout tagsView;

    @View(R.id.bio_tcp)
    private TextView bio;

    final int BLUR_PRECENTAGE = 10;
    Target target;


    //TODO: This tinder mPofile is the profile on which the query is to be performed

    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    Profile currentUserProfile;
    Profile recommended;

    public TinderCard(Context context, SwipePlaceHolderView swipeView, Profile recommended,Profile currntUserData) {
        mContext = context;
        mSwipeView = swipeView;
        this.currentUserProfile = currntUserData;
        this.recommended = recommended;

    }

    @Resolve
    private void onResolved() {
        Log.i("FindMate","3");

        Picasso.get().load(recommended.getProfilePic()).into(circleImageView);

        loadImageInBackground();


        name.setText(recommended.getName());
        tagline.setText(recommended.getTagline());
        city.setText(recommended.getCity());
        handle.setText(recommended.getHandle());
        bio.setText(recommended.getBio());

        tagsView.setVisibility(android.view.View.VISIBLE);
        int tagsCount = recommended.getAllInterests().size();

        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        ArrayList<String> toBered = new ArrayList<>(recommended.getAllInterests());
        toBered.retainAll(currentUserProfile.getAllInterests());
        ArrayList<String> toBeBlack = new ArrayList<>(recommended.getAllInterests());
        toBeBlack.removeAll(toBered);
        TextView[] tages1 = new TextView[toBered.size()];
        TextView[] tages2 = new TextView[toBeBlack.size()];


        for (int i = 0; i < toBered.size(); i++) {
            tages1[i] = new TextView(mContext);
            GradientDrawable gD = new GradientDrawable();
            int strokeWidth = 2;
            int strokeColor = mContext.getResources().getColor(R.color.basil_orange);
            gD.setStroke(strokeWidth, strokeColor);
            gD.setCornerRadius(50);
            gD.setShape(GradientDrawable.RECTANGLE);
            tages1[i].setTextColor(strokeColor);
            tages1[i].setBackground(gD);
            tages1[i].setText(recommended.getAllInterests().get(i));
            layoutParams.setMargins(8, 8, 8, 8);
            tages1[i].setPadding(24, 8, 24, 8);
            tagsView.addView(tages1[i], layoutParams);
        }


        for (int i = 0; i < toBeBlack.size(); i++)   {
            tages2[i] = new TextView(mContext);
            GradientDrawable gD2 = new GradientDrawable();
            int strokeWidth2 = 2;
            int strokeColor2 = mContext.getResources().getColor(R.color.black);
            gD2.setStroke(strokeWidth2, strokeColor2);
            gD2.setCornerRadius(50);
            gD2.setShape(GradientDrawable.RECTANGLE);
            tages2[i].setBackground(gD2);
            tages2[i].setText(recommended.getAllInterests().get(i));
            layoutParams.setMargins(24, 8, 24, 8);
            tages2[i].setPadding(24, 15, 24, 15);
            tagsView.addView(tages2[i], layoutParams);
        }
        Log.i("KaunHuMin", "OnResolvedCalled");
    }

    @SwipeOut
    private void onSwipedOut() {
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);

    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn() {
        Log.d("EVENT", "onSwipedIn");

       ArrayList<String> swipedMeRight =  currentUserProfile.getProfilesSwipedMeRight();
        if (swipedMeRight.contains(recommended.getFirebase_id())){
            //crete connection
           BackGround b = new BackGround();
           b.execute();
        }

        else {
            //add ur id to oters swiped me right
            BackGround2 b2 = new BackGround2();
            b2.execute();
             }

    }

    @SwipeInState
    private void onSwipeInState() {
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState");
    }


    public void createConnection() {

      //Code to create connection in Realtime database for chat purposes
       User user = new User();
       user.setId(recommended.getFirebase_id());
       user.setImageUrl(recommended.getProfilePic());
       user.setUsername(recommended.getHandle());

       User user1 = new User();
       user1.setUsername(currentUserProfile.getHandle());
       user1.setImageUrl(currentUserProfile.getProfilePic());
       user1.setId(currentUserProfile.getFirebase_id());

        DatabaseReference reference1 = FirebaseDatabase.getInstance().
                getReference().child(currentUserProfile.getFirebase_id()).child("connections").
                child(recommended.getFirebase_id())
                ;
        reference1.setValue(user);

        DatabaseReference reference = FirebaseDatabase.getInstance().
                getReference().child(recommended.getFirebase_id()).child("connections").
                child(currentUserProfile.getFirebase_id())
                ;
        reference.setValue(user1);

        //Code to add connection object in Cloud database
        HashMap<String, String > hashMap = new HashMap<>();
        hashMap.put("name",recommended.getName());
        hashMap.put("handle",recommended.getHandle());
        hashMap.put("profilePic",recommended.getProfilePic());
        hashMap.put("firebaseUI",recommended.getFirebase_id());
        FirebaseFirestore.getInstance().collection("profiles").
                document(currentUserProfile.getFirebase_id()).update(
                "connections",FieldValue.arrayUnion(hashMap)
        );


        HashMap<String, String > hashMap2 = new HashMap<>();
        hashMap2.put("name",currentUserProfile.getName());
        hashMap2.put("handle",currentUserProfile.getHandle());
        hashMap2.put("profilePic",currentUserProfile.getProfilePic());
        hashMap2.put("firebaseUI",currentUserProfile.getFirebase_id());
        FirebaseFirestore.getInstance().collection("profiles").document(recommended.getFirebase_id()).update(
                "connections",FieldValue.arrayUnion(hashMap2)
        );
        FirebaseFirestore.getInstance().collection("profiles").
                document(currentUserProfile.getFirebase_id())
                .update("profilesSwipedMeRight",
                        FieldValue.arrayRemove(currentUserProfile.getFirebase_id()));

    }



    public void loadImageInBackground() {
        ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Chargement...");
        mProgressDialog.setIndeterminate(false);

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                backgroundImage.setImageBitmap(BlurImage.fastblur(bitmap, 1f, BLUR_PRECENTAGE));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                backgroundImage.setImageResource(R.mipmap.ic_launcher);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.get()
                .load(currentUserProfile.getProfilePic()).resize(340,180).centerCrop()
                .into(target);
    }


    public class BackGround extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            createConnection();
            return null;
        }
    }


    public class BackGround2 extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            addFirebaseIdToRecomendedUsersProfile();
            return null;
        }
    }

    private void addFirebaseIdToRecomendedUsersProfile() {
        FirebaseFirestore.getInstance().collection("profiles").
                document(recommended.getFirebase_id())
                .update("profilesSwipedMeRight",
                        FieldValue.arrayUnion(currentUserProfile.getFirebase_id()));

    }


}