package com.getmate.demo181201.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.getmate.demo181201.Activities.ChatActivity;
import com.getmate.demo181201.Activities.DetailedEvents;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private ArrayList<Event> events ;
    private LayoutInflater layoutInflater;
    private View convertView;
    private TextView title;

    private CheckBox like, dislike;
    private ImageView eventTimelineImageView;
    private TextView description;
    private TextView timeStamp;
    private TextView savedCount;
    private TextView goingCount;
    private Button dtl,map,share;
    private CheckBox save;
    TextView going;
     //ImageLoader  imageLoader = AppController.getInstance().getImageLoader();

    public EventListAdapter( Context context){
        this.context= context;
    }


    public EventListAdapter(Activity activity, ArrayList<Event> eventList){
        this.activity = activity;
        this.context = activity;
        this.events = eventList;
        }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }




    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (layoutInflater==null){
            layoutInflater= (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView = layoutInflater.inflate(R.layout.card_view,null);
            }

       /* if(imageLoader==null){
            imageLoader=AppController.getInstance().getImageLoader();
        }*/
        final Event event = events.get(i);


        title = convertView.findViewById(R.id.event_title);
        timeStamp = convertView.findViewById(R.id.timestamp);
        description = convertView.findViewById(R.id.event_description);

        eventTimelineImageView = convertView.findViewById(R.id.event_image);
        dtl = convertView.findViewById(R.id.dtl_btn);

        map= convertView.findViewById(R.id.map_btn);
        share = convertView.findViewById(R.id.share_btn);
        save = convertView.findViewById(R.id.save_btn);
        like = convertView.findViewById(R.id.like_btn);
        dislike = convertView.findViewById(R.id.dislike_btn);


      like.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (!like.isChecked()){
                   like.setChecked(true);
                   }
                   else{
                   like.setChecked(false);

               }

           }
       });

       dislike.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (!dislike.isChecked()){
                   dislike.setChecked(true);
               }
               else {
                   dislike.setChecked(false);
               }
           }
       });




       save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if (save.isChecked()){
                   FirebaseFirestore.getInstance().collection("profiles").
                           document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("savedEvents",FieldValue.arrayUnion(event.getFirebaseId()));
               }
               else {
                   FirebaseFirestore.getInstance().collection("profiles").
                           document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                           update("savedEvents",FieldValue.arrayRemove(event.getFirebaseId()));

               }
           }
       });


      if(event.getImageUrl()!=null){
            /*imageLoader.get(event.getImageUrl(), ImageLoader.getImageListener(eventTimelineImageView,
                   R.drawable.event_image_demo , android.R.drawable
                            .ic_dialog_alert));

            eventTimelineImageView.setImageUrl(event.getImageUrl(),imageLoader);
            eventTimelineImageView.setVisibility(View.VISIBLE);
            eventTimelineImageView.setResponseObserver(new EventTimelineImageView.ResponseObserver() {
                @Override
                public void onError() {

                }

                @Override
                public void onSuccess() {

                }
            });*/
          Picasso.get().load(event.getImageUrl()).into(eventTimelineImageView);
        }
        else {
            eventTimelineImageView.setVisibility(View.GONE);
        }

        title.setText(event.getTitle());

        CharSequence time = DateUtils.getRelativeTimeSpanString
                (Long.parseLong(event.getTime()),System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS);
        timeStamp.setText(time);
        description.setText(event.getDescription());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To scroll activity
                Intent i = new Intent(EventListAdapter.this.context,DetailedEvents.class);
                Gson gson = new Gson();
                String  json = gson.toJson(event);
                i.putExtra("event",json);
                EventListAdapter.this.context.startActivity(i);

            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //To Map Activity
            }
        });




        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(context);

                final String id = event.getFirebaseId();

                LayoutInflater inflater = (LayoutInflater)context.
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_for_sharing_event, null);
                final float scale =context.getResources().getDisplayMetrics().density;
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
                        Intent i = new Intent(context,ChatActivity.class);
                        i.putExtra("text","http://cohortso.in/event/"+id);
                        i.putExtra("forSharingEvent",true);
                        context.startActivity(i);

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
                                context.startActivity(Intent.createChooser(shareIntent,"Share with"));

                            } catch(IOException e) {
                                System.out.println(e);
                            }


                        }
                    }
                });

            }
        });
        return convertView;
    }




    public void getDistance(Location location){
        //Double distance = Math.sqrt();
        }


        public void showPopupOnsharingClicked(View view){
            LayoutInflater inflater = (LayoutInflater)context.
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_for_sharing_event, null);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
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

                }
            });

            shareUsingOtherMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

}
