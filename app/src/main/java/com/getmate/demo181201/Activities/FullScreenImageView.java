package com.getmate.demo181201.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.getmate.demo181201.R;
import com.squareup.picasso.Picasso;

public class FullScreenImageView extends AppCompatActivity {
    ImageView fullscreenImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_view);
        fullscreenImageView = findViewById(R.id.fullScreenImageView);


        String imageLink = getIntent().getStringExtra("imageLink");
        if (imageLink!=null){
            Picasso.get().load(imageLink).into(fullscreenImageView);
        }

    }
}
