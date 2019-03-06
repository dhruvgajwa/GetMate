package com.getmate.demo181201.Activities;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    Profile profile =new Profile();
    TextView school,work,name,handle,email,bio,city;
    ImageView profilePic;
    String profileString=null;
    private FlexboxLayout tagsView;

    boolean isImageFitToScreen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewsById();


         profileString = getIntent().getStringExtra("profileString");
        if (profile!=null){
            Gson gson = new Gson();
            profile = gson.fromJson(profileString,Profile.class);
            updateUI();
        }

    }

    public void findViewsById(){
        View view1 = findViewById(R.id.photoHeader);
        name = findViewById(R.id.tvName);
        handle = findViewById(R.id.handle);
        work = findViewById(R.id.tvTitle);

        email = findViewById(R.id.tvEmail);
        city = findViewById(R.id.tvAddress);
        bio = findViewById(R.id.tvSummary);
        profilePic =findViewById(R.id.image_view_p);
        tagsView = findViewById(R.id.tags_view);

    }

    public void updateUI(){
        name.setText(profile.getName());
        handle.setText(profile.getHandle());
        if (profile.getProfilePic()!=null){
            Picasso.get().load(profile.getProfilePic()).into(profilePic);
        }
        email.setText(profile.getEmail());
        city.setText(profile.getCity());
        bio.setText(profile.getBio());
        work.setText(profile.getTagline());
        Picasso.get().load(profile.getProfilePic()).into(profilePic);
        TextView[] tages1 = new TextView[profile.getAllParentTags().size()];

        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < profile.getAllParentTags().size(); i++) {
            tages1[i] = new TextView(getApplicationContext());
            GradientDrawable gD = new GradientDrawable();
            int strokeWidth = 2;
            int strokeColor = getApplication().getResources().getColor(R.color.basil_orange);
            gD.setStroke(strokeWidth, strokeColor);
            gD.setCornerRadius(50);
            gD.setShape(GradientDrawable.RECTANGLE);
            tages1[i].setTextColor(strokeColor);
            tages1[i].setBackground(gD);
            tages1[i].setText(profile.getAllParentTags().get(i));
            layoutParams.setMargins(8, 8, 8, 8);
            tages1[i].setPadding(24, 8, 24, 8);
            tagsView.addView(tages1[i], layoutParams);
        }


    }
}
