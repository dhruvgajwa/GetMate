package com.getmate.demo181201.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.getmate.demo181201.CustomViews.EventTimelineImageView;
import com.getmate.demo181201.InterestSelection.RoundedImageView;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.R;
import com.getmate.demo181201.VolleyClasses.AppController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class DetailedEvents extends AppCompatActivity {
    RelativeLayout link_relative_layout;
    EventTimelineImageView eventTimelineImageView;
    TextView title,venue,description,creatorName,creatorEmail,creatorPhone,creatorHandle;
    TextView orgname1,orgName2,orgName3;
    TextView orgEmail1,orgEmail2,orgEmail3;
    TextView orgP1,orgP2,orgP3;
    RoundedImageView creatorProfilePic;
    Button link,date,going,buyTicket;
    TextView link1,date1,going1;
    Button cEmail,cPhone;
    LinearLayout org1,org2,org3;
    Event event = new Event();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("KaunHU", "oncreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_events);
        findViewsById();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Log.i("Kaun","current User not null"+currentUser.getDisplayName());
        }
        else {
            Log.i("Kaun","current User is null");
        }
        String s = getIntent().getStringExtra("event");
        if (s!=null){
            Log.i("Detailed event", s);
            Gson gson = new Gson();
            event = gson.fromJson(s, Event.class);
            if (event != null) {
              updateUI(event);

            } else {
                Toast.makeText(getApplicationContext(), "Event Details Not available", Toast.LENGTH_LONG).show();
            }

            }
        else {
            handleIntent(getIntent());
        }

        buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailedEvents.this,PaytmGateway.class);
                Gson gson = new Gson();
                String data = gson.toJson(event);
                i.putExtra("data",data);

                startActivity(i);
            }
        });

        cEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                String creatorsEmail = "cratorEmail@mail.com";
                if (!event.getCreatorEmail().isEmpty()){
                   creatorsEmail = event.getCreatorEmail();
                }
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ creatorsEmail});
                email.putExtra(Intent.EXTRA_SUBJECT, event.getTitle());
                email.putExtra(Intent.EXTRA_TEXT, "write your querry here?");

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        cPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String posted_by = event.getCreatorPhoneNo();
                String uri = "tel:" + posted_by.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });



    }


    private void handleIntent(Intent intent) {
        Log.i("Kaun","handleIntentCalled");
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            String eventId = appLinkData.getLastPathSegment();
            getEventFromEventId(eventId);

        }

    }

    private void getEventFromEventId(String eventId) {
        Log.i("Kaun","getEventFromEventIdCalled");

        db.collection("events").
                document(eventId).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Event event = new Event();
                if (documentSnapshot!=null){
                    event = documentSnapshot.toObject(Event.class);
                    if (event!=null){
                        updateUI(event);

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"NOT REACHABLE",Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Log.i("KaunHai","documnent snapshot is null");
                }

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("Kaun","onNewIntentCalled");

        handleIntent(intent);
    }

    private void findViewsById(){
         eventTimelineImageView = findViewById(R.id.event_poster_sa);
         title = findViewById(R.id.title_sa);
         venue = findViewById(R.id.venue_sa);
         description = findViewById(R.id.description_sa);
         link = findViewById(R.id.link_btn);
         link1 = findViewById(R.id.link_text);
         date1 = findViewById(R.id.time_sa);
         going1 = findViewById(R.id.going_count);
         creatorName = findViewById(R.id.creator_name_sa);
         creatorPhone = findViewById(R.id.creator_phone_sa);
         creatorEmail = findViewById(R.id.creator_email_sa);
         creatorProfilePic = findViewById(R.id.creator_prfl_pic_sa);
         creatorHandle = findViewById(R.id.creator_handle_sa);
         orgname1 = findViewById(R.id.org1Name);
         orgEmail1 = findViewById(R.id.org1Email);
         orgP1 = findViewById(R.id.org1Phone);
         orgName2 = findViewById(R.id.org2Name);
         orgEmail2 = findViewById(R.id.org2Email);
         orgP2 = findViewById(R.id.org2Phone);
         orgName3 = findViewById(R.id.org3Name);
         orgEmail3 = findViewById(R.id.org3Email);
         orgP3 = findViewById(R.id.org3Phone);
         org1 = findViewById(R.id.organiserLinLay1);
        org2 = findViewById(R.id.organiserLinLay2);
        org3 = findViewById(R.id.organiserLinLay3);
        buyTicket = findViewById(R.id.buy_ticket);
        cEmail = findViewById(R.id.creator_email_de);
        cPhone = findViewById(R.id.creator_phone_de);

    }


    public void updateUI(Event event){
        title.setText(event.getTitle());
        venue.setText(event.getAddress());
        description.setText(event.getDescription());
        creatorName.setText(event.getCreatorName());
        going1.setText(event.getGoingCount() + " going");


        if (event.getUrl() != null) {
            link_relative_layout.setVisibility(View.VISIBLE);
            link1.setText(Html.fromHtml("<a href=\"" + event.getUrl() + "\">" + event.getUrl() + "</a>"));
            link1.setMovementMethod(LinkMovementMethod.getInstance());

        }


        CharSequence time = DateUtils.getRelativeTimeSpanString
                (Long.parseLong(event.getTime()), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        date1.setText(time);
        // creatorEmail.setText(event.getcreatorEmail());
        //creatorPhone.setText(event.getCreatorPhone());

        if (event.getOrganisers() != null) {
            Log.i("KaunHUMein", "org size is " + event.getOrganisers().size());
        } else {
            Log.i("KaunHUMein", "org size is null");
        }


        if (event.getOrganisers() != null) {

            for (int i = 0; i < event.getOrganisers().size(); i++) {
                if (i == 0) {
                    org1.setVisibility(View.VISIBLE);
                    orgname1.setText(event.getOrganisers().get(0).getName());
                    orgEmail1.setText(event.getOrganisers().get(0).getEmail());
                    orgP1.setText(event.getOrganisers().get(0).getContactNumber());
                } else if (i == 1) {
                    View view = findViewById(R.id.view2);
                    view.setVisibility(View.VISIBLE);
                    org2.setVisibility(View.VISIBLE);
                    orgName2.setText(event.getOrganisers().get(1).getName());
                    orgEmail2.setText(event.getOrganisers().get(1).getEmail());
                    orgP2.setText(event.getOrganisers().get(1).getContactNumber());
                }
                if (i == 2) {
                    View view = findViewById(R.id.view3);
                    view.setVisibility(View.VISIBLE);
                    org3.setVisibility(View.VISIBLE);
                    orgName3.setText(event.getOrganisers().get(2).getName());
                    orgEmail3.setText(event.getOrganisers().get(2).getEmail());
                    orgP3.setText(event.getOrganisers().get(2).getContactNumber());
                }
            }

        }


        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        Picasso.get().load(event.getCreatorProfilePic()).into(creatorProfilePic);

        if (event.getImageUrl() != null) {
            eventTimelineImageView.setImageUrl(event.getImageUrl(), imageLoader);
            eventTimelineImageView.setVisibility(View.VISIBLE);
            eventTimelineImageView.setResponseObserver(new EventTimelineImageView.ResponseObserver() {
                @Override
                public void onError() {

                }

                @Override
                public void onSuccess() {

                }
            });
        } else {
            eventTimelineImageView.setImageResource(R.mipmap.event_placeholder);
            eventTimelineImageView.setVisibility(View.VISIBLE);
        }

    }
}
