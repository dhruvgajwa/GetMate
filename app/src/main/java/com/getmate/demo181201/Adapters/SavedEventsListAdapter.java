package com.getmate.demo181201.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.getmate.demo181201.Activities.DetailedEvents;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SavedEventsListAdapter extends BaseAdapter {
    private ImageView eventTimelineImageView;
    private TextView title;
    private TextView date;
    private Context context;
    private ArrayList<Event> events;
    public SavedEventsListAdapter(Context context,ArrayList<Event> eventsList){
        this.context = context;
        this.events = eventsList;
        for (int i=0;i<10;i++){
            this.events.addAll(eventsList);
        }
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater  layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       if (layoutInflater!=null){
           view = layoutInflater.inflate(R.layout.saved_events_small,null);

       }


        final Event event = events.get(i);
        title =view.findViewById(R.id.title_ses);
        date = view.findViewById(R.id.date_ses);
        title.setText(event.getTitle());
        eventTimelineImageView = view.findViewById(R.id.event_image_ses);
        CharSequence time = DateUtils.getRelativeTimeSpanString
                (Long.parseLong(event.getTime()),System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS);
        date.setText(time);
        Picasso.get().load(event.getImageUrl()).into(eventTimelineImageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SavedEventsListAdapter.this.context,DetailedEvents.class);
                Gson gson = new Gson();
                String  json = gson.toJson(event);
                i.putExtra("event",json);
                SavedEventsListAdapter.this.context.startActivity(i);
            }
        });

        return view;
    }
}
