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

import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.flexbox.FlexboxLayout;
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

import de.hdodenhof.circleimageview.CircleImageView;

@Layout(R.layout.tinder_card_view)
public class TinderCard {

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

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


    final int BLUR_PRECENTAGE = 10;
    Target target;


    //TODO: This tinder mPofile is the profile on which the query is to be performed

    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    Profile currentUserProfile;

    public TinderCard(Context context, SwipePlaceHolderView swipeView, Profile p) {
        mContext = context;
        mSwipeView = swipeView;
        this.currentUserProfile = p;

    }

    @Resolve
    private void onResolved() {


        Picasso.get().load(currentUserProfile.getProfilePic()).into(circleImageView);

     /*   Target target = new Target() {
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



        backgroundImage.setTag(target);
        Picasso.get()
                .load(currentUserProfile.getProfilePic())
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(target);
*/

        loadImageInBackground();
        //Picasso.get().load(currentUserProfile.getProfilePic()).into(backgroundImage);
        nameAgeTxt.setText(currentUserProfile.getName());
        locationNameTxt.setText(currentUserProfile.getTagline());
        name.setText(currentUserProfile.getName());
        tagline.setText(currentUserProfile.getTagline());
        city.setText(currentUserProfile.getCity());
        handle.setText(currentUserProfile.getHandle());


        tagsView.setVisibility(android.view.View.VISIBLE);
        int tagsCount = currentUserProfile.getAllInterests().size();//... integer number of textviews
        TextView[] tages = new TextView[tagsCount];//create dynamic textviewsarray
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //loop to customize the text view and add it to flex box
        for (int i = 0; i < 2; i++) {
            tages[i] = new TextView(mContext);
            GradientDrawable gD = new GradientDrawable();
            int strokeWidth = 2;
            int strokeColor = mContext.getResources().getColor(R.color.basil_orange);

            gD.setStroke(strokeWidth, strokeColor);
            gD.setCornerRadius(50);
            gD.setShape(GradientDrawable.RECTANGLE);

            tages[i].setBackground(gD);
            tages[i].setText(currentUserProfile.getAllInterests().get(i));
            layoutParams.setMargins(24, 8, 24, 8);
            tages[i].setPadding(24, 15, 24, 15);
            tagsView.addView(tages[i], layoutParams);
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
    private void onSwipeInState() {
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState");
    }


    public void createConnection(String userId1, String userId2) {
       /* FirebaseFirestore.getInstance().collection("profiles").
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
                child("connections").child(currentUserProfile.getFirebase_id()).setValue(hashMap1);*/
    }


    public class BEckGround extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {





            backgroundImage.setTag(target);
           /* Picasso.get()
                    .load(currentUserProfile.getProfilePic())
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(target);*/
           return null;
        }
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


}