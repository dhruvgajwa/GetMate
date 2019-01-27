package com.getmate.demo181201.Activities;

import android.os.Bundle;
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

    LinearLayout org1,org2,org3;
    Event event = new Event();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("KaunHU","oncreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_events);
        findViewsById();

        String s = getIntent().getStringExtra("event");
        Log.i("Detailed event",s);
        Gson gson = new Gson();

        Event event = gson.fromJson(s,Event.class);



        buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });





            if (event!=null){
                title.setText(event.getTitle());
                venue.setText(event.getAddress());
                description.setText(event.getDescription());
                creatorName.setText(event.getCreatorName());
                going1.setText(event.getGoingCount()+" going");


                if(event.getUrl()!=null){
                    link_relative_layout.setVisibility(View.VISIBLE);
                    link1.setText(Html.fromHtml("<a href=\""+event.getUrl()+"\">"+event.getUrl()+"</a>"));
                    link1.setMovementMethod(LinkMovementMethod.getInstance());

                }


                CharSequence time = DateUtils.getRelativeTimeSpanString
                        (Long.parseLong(event.getTime()),System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS);
                date1.setText(time);
                // creatorEmail.setText(event.getcreatorEmail());
                //creatorPhone.setText(event.getCreatorPhone());

                if (event.getOrganisers()!=null){
                    Log.i("KaunHUMein","org size is "+event.getOrganisers().size());
                }
                else {
                    Log.i("KaunHUMein","org size is null");
                }



                if (event.getOrganisers()!=null){

                    for (int i=0;i<event.getOrganisers().size();i++){
                        if(i==0){
                            org1.setVisibility(View.VISIBLE);
                            orgname1.setText(event.getOrganisers().get(0).getName());
                            orgEmail1.setText(event.getOrganisers().get(0).getEmail());
                            orgP1.setText(event.getOrganisers().get(0).getContactNumber());
                        }
                        else if(i==1){
                            View view = findViewById(R.id.view2);
                            view.setVisibility(View.VISIBLE);
                            org2.setVisibility(View.VISIBLE);
                            orgName2.setText(event.getOrganisers().get(1).getName());
                            orgEmail2.setText(event.getOrganisers().get(1).getEmail());
                            orgP2.setText(event.getOrganisers().get(1).getContactNumber());
                        }
                        if(i==2){
                            View view = findViewById(R.id.view3);
                            view.setVisibility(View.VISIBLE);
                            org3.setVisibility(View.VISIBLE);
                            orgName3.setText(event.getOrganisers().get(2).getName());
                            orgEmail3.setText(event.getOrganisers().get(2).getEmail());
                            orgP3.setText(event.getOrganisers().get(2).getContactNumber());
                        }
                    }

                }


                if (imageLoader==null){
                    imageLoader = AppController.getInstance().getImageLoader();
                }

                Picasso.get().load(event.getCreatorProfilePic()).into(creatorProfilePic);

                if(event.getImageUrl()!=null){
                    eventTimelineImageView.setImageUrl(event.getImageUrl(),imageLoader);
                    eventTimelineImageView.setVisibility(View.VISIBLE);
                    eventTimelineImageView.setResponseObserver(new EventTimelineImageView.ResponseObserver() {
                        @Override
                        public void onError() {

                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
                }
                else {
                    eventTimelineImageView.setVisibility(View.GONE);
                }



            }
            else {
                Toast.makeText(getApplicationContext(),"Event Details Not available",Toast.LENGTH_LONG).show();
            }



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
    }
}
