package com.getmate.demo181201;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.getmate.demo181201.Activities.DetailedEvents;
import com.getmate.demo181201.CustomViews.EventTimelineImageView;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.VolleyClasses.AppController;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private ArrayList<Event> events ;
    private LayoutInflater layoutInflater;
    private View convertView;
    private TextView title;


    private EventTimelineImageView eventTimelineImageView;
    private TextView description;
    private TextView timeStamp;
    private TextView savedCount;
    private TextView goingCount;
    private Button dtl,map,share;
    TextView going;
     ImageLoader  imageLoader = AppController.getInstance().getImageLoader();

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
            layoutInflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView = layoutInflater.inflate(R.layout.card_view,null);
            }

        if(imageLoader==null){
            imageLoader=AppController.getInstance().getImageLoader();
        }
        final Event event = events.get(i);


        title = convertView.findViewById(R.id.event_title);
        timeStamp = convertView.findViewById(R.id.timestamp);
        description = convertView.findViewById(R.id.event_description);

        eventTimelineImageView = convertView.findViewById(R.id.event_image);
        dtl = convertView.findViewById(R.id.dtl_btn);
        going = convertView.findViewById(R.id.gng_button);
        map= convertView.findViewById(R.id.map_btn);
        share = convertView.findViewById(R.id.share_btn);




        if(event.getImageUrl()!=null){
            imageLoader.get(event.getImageUrl(), ImageLoader.getImageListener(eventTimelineImageView,
                   R.mipmap.badminton , android.R.drawable
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
            });
        }
        else {
            eventTimelineImageView.setVisibility(View.GONE);
        }



        title.setText(event.getTitle());



        CharSequence time = DateUtils.getRelativeTimeSpanString
                (Long.parseLong(event.getTime()),System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS);
        timeStamp.setText(time);
        description.setText(event.getDescription());
        //eventTimelineImageView.setImageUrl(event.getUrl());
        //use Picasso Library here!


        dtl.setOnClickListener(new View.OnClickListener() {
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

        going.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //increase the count by one + add the currentUserId in event list + add the eventId in profile data

            }
        });

        going.setText(String.valueOf(event.getGoingCount())+"+");

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int SDK_INT = android.os.Build.VERSION.SDK_INT;
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
                                + "http://cohortso.in/event/MAXFIMvh7CKlNZYZwRnD");
                        shareIntent.setType("image/png");
                        context.startActivity(Intent.createChooser(shareIntent,"Share with"));

                    } catch(IOException e) {
                        System.out.println(e);
                    }


                }



/*
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/jpeg");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Cohortso");
                   Uri imageUri = Uri.parse(event.getImageUrl());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "http://cohortso.in/event/MAXFIMvh7CKlNZYZwRnD";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(Intent.createChooser(shareIntent, "share event using"));
                } catch(Exception e) {
                    //e.toString();
                }*/

            }
        });
        return convertView;
    }



    public void getDistance(Location location){
        //Double distance = Math.sqrt();
        }

}
