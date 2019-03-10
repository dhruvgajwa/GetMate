package com.getmate.demo181201.createEvent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.getmate.demo181201.R;

public class EventPrivacyAndTicketPrice extends AppCompatActivity {
    CheckBox publicCheckBox, privateCheckBox;
    Button next,previous;
    Intent intent;
    String privacy = "public";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_price_and_event_level);
        publicCheckBox = findViewById(R.id.public_eve);
        privateCheckBox = findViewById(R.id.private_eve);
        next = findViewById(R.id.next_age);
        previous = findViewById(R.id.previous_age);

        intent = getIntent();
        publicCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicCheckBox.setChecked(true);
                privateCheckBox.setChecked(false);
                privacy = "public";
            }
        });
        privateCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privateCheckBox.setChecked(true);
                publicCheckBox.setChecked(false);
                privacy = "private";
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent( EventPrivacyAndTicketPrice.this,TicketPrice.class);

                i.putExtra("description",intent.getStringExtra("description"));
                i.putExtra("title",intent.getStringExtra("title"));
                i.putExtra("from",intent.getLongExtra("from",0));
                i.putExtra("to",intent.getLongExtra("to",0));
                i.putStringArrayListExtra("interestB",intent.getStringArrayListExtra("interestB"));
                i.putStringArrayListExtra("interestI",intent.getStringArrayListExtra("interestI"));
                i.putStringArrayListExtra("interestE",intent.getStringArrayListExtra("interestE"));
                i.putStringArrayListExtra("AllParentInterests",intent.
                        getStringArrayListExtra("AllParentInterests"));
                i.putExtra("lat",intent.getDoubleExtra("lat",0.00));
                i.putExtra("lon",intent.getDoubleExtra("lon",0.00));
                i.putExtra("address",intent.getStringExtra("address"));
                i.putExtra("city",intent.getStringExtra("city"));
                i.putExtra("imageUri",intent.getStringExtra("imageUri"));
                i.putExtra("organisers",intent.getStringArrayListExtra("organisers"));
                i.putExtra("link",intent.getStringExtra("link"));
                i.putExtra("eventType",intent.getStringExtra("eventType"));
                i.putExtra("privacy",privacy);
                startActivity(i);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



/*
        String[] level = {"Private", "public"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(level, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
            }
        });
        builder.show();*/

    }
}
