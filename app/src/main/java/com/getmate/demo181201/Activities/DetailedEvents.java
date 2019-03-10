package com.getmate.demo181201.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailedEvents extends AppCompatActivity {
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    RelativeLayout link_relative_layout;
    EventTimelineImageView eventTimelineImageView;
    TextView title,venue,description,creatorName,creatorEmail,creatorPhone,creatorHandle;
    TextView orgname1,orgName2,orgName3;
    TextView orgEmail1,orgEmail2,orgEmail3;
    TextView orgP1,orgP2,orgP3;
    TextView ticketPrice,ticketsLeft,eventStartFrom,eventStopAt;
    RoundedImageView creatorProfilePic;
    Button link,date,going,buyTicket;
    TextView link1,date1,going1;
    Button cEmail,cPhone;
    LinearLayout org1,org2,org3;
    Event event = new Event();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextView like,dislike,save,share;

    Boolean saveFlag=false,likeFlag=false,dislikeFlag=false;


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



                if (event.getTicketType().equals("free")){
                  //1. check how many tickets are left?
                    // 2. the directly go to the print ticket and register in database

                    /*db.collection("events").document(event.getFirebaseId()).get().addOnCompleteListener(
                            new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Event eventCopy = task.getResult().toObject(Event.class);
                                    if (eventCopy.getTicketsLeft()>0){
                                        Intent i = new Intent(DetailedEvents.this,TicketActivity.class);

                                    }
                                }
                            }
                    );*/
                    Intent i = new Intent(DetailedEvents.this,PaytmGateway.class);
                    Gson gson = new Gson();
                    String data = gson.toJson(event);
                    i.putExtra("data",data);
                    startActivity(i);
                }
                else if (event.getTicketType().equals("paid")){
                    Intent i = new Intent(DetailedEvents.this,PaytmGateway.class);
                    Gson gson = new Gson();
                    String data = gson.toJson(event);
                    i.putExtra("data",data);
                    startActivity(i);
                }
                else if (event.getTicketType().equals("notRequired")){

                }


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

        //TODO: Set the network request for like and dislikes
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!likeFlag){
                    like.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.thumps_up_red,0,0);
                    likeFlag =true;


                    if (dislikeFlag){
                        dislike.setCompoundDrawablesWithIntrinsicBounds(0,
                                R.drawable.dislike_icon_black,0,0);
                        dislikeFlag = false;
                    }
                }
                else {
                    like.setCompoundDrawablesWithIntrinsicBounds(0,
                           R.drawable.thumps_up_black,0,0);
                    likeFlag = false;
                }
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!dislikeFlag){
                    dislike.setCompoundDrawablesWithIntrinsicBounds(0,
                            R.drawable.dislike_icon_red,0,0);
                     dislikeFlag =true;
                     if (likeFlag==true){
                         like.setCompoundDrawablesWithIntrinsicBounds(0,
                                 R.drawable.thumps_up_black,0,0);
                         likeFlag = false;
                     }



                }
                else {
                    dislike.setCompoundDrawablesWithIntrinsicBounds(0,
                            R.drawable.dislike_icon_black,0,0);
                    dislikeFlag = false;
                }



            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!saveFlag){
                    save.setCompoundDrawablesWithIntrinsicBounds(0,
                           R.drawable.save_icon_filled,0,0);
                    FirebaseFirestore.getInstance().collection("profiles").
                            document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("savedEvents",FieldValue.arrayUnion(event.getFirebaseId()));
                    saveFlag =true;
                }
                else {
                    save.setCompoundDrawablesWithIntrinsicBounds(0,
                            R.drawable.save_icon_outlined,0,0);
                    FirebaseFirestore.getInstance().collection("profiles").
                            document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                            update("savedEvents",FieldValue.arrayRemove(event.getFirebaseId()));
                    saveFlag = true;
                }
            }
        });



        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(DetailedEvents.this);

                final String id = event.getFirebaseId();

                LayoutInflater inflater = (LayoutInflater)getApplicationContext().
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_for_sharing_event, null);
                final float scale =getApplicationContext().getResources().getDisplayMetrics().density;
                int pixels = (int) (340 * scale + 0.5f);
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.setAnimationStyle(R.style.Animation);
                popupView.setLayoutParams(new LinearLayout.LayoutParams(pixels,pixels));
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

                Button shareUsingCohortso = popupView.findViewById(R.id.share_using_cohortso);
                Button shareUsingOtherMedia = popupView.findViewById(R.id.share_using_other_media);
                shareUsingCohortso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(DetailedEvents.this,ChatActivity.class);
                        i.putExtra("text","http://cohortso.in/event/"+id);
                        i.putExtra("forSharingEvent",true);
                        startActivity(i);

                    }
                });

                shareUsingOtherMedia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        progressDialog.show();
                        progressDialog.setMessage("Loading.....Please wait");
                        int SDK_INT = Build.VERSION.SDK_INT;
                        if (SDK_INT > 8)
                        {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            try {
                                Intent shareIntent;
                                URL url = new URL(event.getImageUrl());
                                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Share.png";
                                OutputStream out = null;
                                File file=new File(path);
                                try {
                                    out = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                path=file.getPath();
                                Uri bmpUri = Uri.parse("file://"+path);
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey please check this application "
                                        + "http://cohortso.in/event/"+id);
                                shareIntent.setType("image/png");
                                progressDialog.dismiss();
                                startActivity(Intent.createChooser(shareIntent,"Share with"));

                            } catch(IOException e) {
                                System.out.println(e);
                            }


                        }
                    }
                });

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
        like = findViewById(R.id.like_btn_text);
        dislike = findViewById(R.id.dislike_btn_text);
        save = findViewById(R.id.save_btn_text);
        share = findViewById(R.id.share_btn_text);
        ticketPrice = findViewById(R.id.ticket_price);
        ticketsLeft = findViewById(R.id.tickets_left);
        eventStartFrom = findViewById(R.id.e_s_f);
        eventStopAt = findViewById(R.id.e_s_a);
        link_relative_layout = findViewById(R.id.link_rl);
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


        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(event.getFrom());
        eventStartFrom.setText(simpleDateFormat.format(c.getTime())+" "+String.valueOf(c.
                get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(c.get(Calendar.MINUTE)));

        c.setTimeInMillis(event.getTo());
        eventStopAt.setText(simpleDateFormat.format(c.getTime())+" "+String.valueOf(c.
                get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(c.get(Calendar.MINUTE)));


        if (event.getTicketType().equals("free")){
            ticketPrice.setText("Free\n" +
                    " on EventHorts");
            ticketsLeft.setText(event.getTicketsLeft()+" Tickets Left");

        }
        else if (event.getTicketType().equals("paid")){
            ticketPrice.setText("INR "+event.getTicketPrice());
            ticketsLeft.setText(event.getTicketsLeft()+" Tickets Left");
        }
        else if (event.getTicketType().equals("notRequired")){
            ticketPrice.setText("No registration required");
            ticketsLeft.setVisibility(View.GONE);
        }



        if (event.getTicketType().equals("free")){
            buyTicket.setText("Get Ticket");
        }
        else if (event.getTicketType().equals("notRequired")){
            buyTicket.setVisibility(View.GONE);
        }
        else if(event.getTicketType().equals("paid")){

        }


        CharSequence time = DateUtils.getRelativeTimeSpanString
                (Long.parseLong(event.getTime()), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        date1.setText(time);
        creatorEmail.setText(event.getCreatorEmail());
        creatorPhone.setText(event.getCreatorPhoneNo());

        if (event.getOrganisers() != null) {
            Log.i("KaunHUMein", "org size is " + event.getOrganisers().size());
        } else {
            Log.i("KaunHUMein", "org size is null");
        }


        if (event.getOrganisers().size()!=0) {

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
