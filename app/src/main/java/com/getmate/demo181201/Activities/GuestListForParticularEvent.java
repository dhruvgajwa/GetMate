package com.getmate.demo181201.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.getmate.demo181201.InterestSelection.RoundedImageView;
import com.getmate.demo181201.Objects.ConnectionObject;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GuestListForParticularEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_list_for_particular_event);

        Event event = new Gson().fromJson(getIntent().getStringExtra("eventId"),Event.class);
        ArrayList<ConnectionObject> going = new ArrayList<>();
        going = event.getGoingProfiles();

       GuestListAdapter adapter =
                new GuestListAdapter(getApplicationContext(),going);
        ListView listView;
        listView = findViewById(R.id.connections_listview);
        listView.setAdapter(adapter);

    }

    public class GuestListAdapter extends BaseAdapter {


        LayoutInflater layoutInflater;
        Context context;
        private View convertView;

        ArrayList<ConnectionObject> goingProfiles = new ArrayList<>();

        RoundedImageView roundedImageView;
        TextView handle;
        TextView name;

        public GuestListAdapter(){}

        public GuestListAdapter(Context context, ArrayList<ConnectionObject> goingProfiles){
            this.context = context;

            this.goingProfiles = goingProfiles;
        }



        @Override
        public int getCount() {
            if(goingProfiles!=null){
                return goingProfiles.size();
            }
            else {
                return 0;
            }

        }

        @Override
        public Object getItem(int i) {
            return goingProfiles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (layoutInflater==null){
                layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if(convertView==null)
            {
                convertView = layoutInflater.inflate(R.layout.connection_layout,null);
            }

            name = convertView.findViewById(R.id.connection_name);
            handle = convertView.findViewById(R.id.connection_handle);
            roundedImageView = convertView.findViewById(R.id.connection_image);
            ConnectionObject goingProfile = goingProfiles.get(i);
            name.setText(goingProfile.getName());
            handle.setText(goingProfile.getHandle());
            Picasso.get().load(goingProfile.getProfilePic()).into(roundedImageView);
            //TODO: HERE if clicked on any of this view, The user should be redirected to profile of that that particular user!
            // 1.5.2019

            handle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore.getInstance().collection("profiles").
                            document(goingProfile.getFirebaseUI()).get().addOnCompleteListener(
                            new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Intent intent = new Intent(context,ProfileActivity.class);
                                    Profile p = task.getResult().toObject(Profile.class);
                                    Gson gson = new Gson();
                                    String ps = gson.toJson(p);
                                    intent.putExtra("profileString",ps);
                                    context.startActivity(intent);
                                }
                            }
                    );
                }
            });
          return convertView;
        }
    }

}
