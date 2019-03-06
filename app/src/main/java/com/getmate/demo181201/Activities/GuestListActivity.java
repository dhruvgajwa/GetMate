package com.getmate.demo181201.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.getmate.demo181201.CurrentUserData;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GuestListActivity extends AppCompatActivity {
ArrayList<Event> events = new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_list);
        //First Show the list Of all the events created by users
        Profile currentUserProfile = CurrentUserData.getInstance().getCurrent();
        ArrayList<String> myEventsFirebaseIds= new ArrayList<>(); //= currentUserProfile.getAllCreatedEvents()
        myEventsFirebaseIds = currentUserProfile.getCreatedEvents();
        GuestListEventAdapter guestListEventAdapter = new GuestListEventAdapter(getApplicationContext(),events);

        listView = findViewById(R.id.saved_items_list_view);
        listView.setAdapter(guestListEventAdapter);
        for (String id:myEventsFirebaseIds) {
            FirebaseFirestore.getInstance().collection("events").document(id).get().addOnCompleteListener(
                    new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Event e = task.getResult().toObject(Event.class);
                            events.add(e);
                            guestListEventAdapter.notifyDataSetChanged();
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }


    public static class GuestListEventAdapter extends BaseAdapter {
        private ImageView eventTimelineImageView;
        private TextView title;
        private TextView date;
        private Context context;
        private ArrayList<Event> events;
        public GuestListEventAdapter(Context context,ArrayList<Event> eventsList){
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
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
                    Intent i = new Intent(GuestListEventAdapter.this.context,GuestListForParticularEvent.class);
                    i.putExtra("eventId",new Gson().toJson(event));
                    GuestListEventAdapter.this.context.startActivity(i);
                }
            });

            return view;
        }
    }

}




