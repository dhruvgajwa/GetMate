package com.getmate.demo181201.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.getmate.demo181201.Activities.ProfileActivity;
import com.getmate.demo181201.InterestSelection.RoundedImageView;
import com.getmate.demo181201.Objects.ConnectionObject;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConnectionsAdapter extends BaseAdapter {


    LayoutInflater layoutInflater;
    Context context;
    private View convertView;
    Activity activity;
    ArrayList<ConnectionObject> connections = new ArrayList<>();

    RoundedImageView roundedImageView;
    TextView handle;
    TextView name;

    public ConnectionsAdapter(){}

    public ConnectionsAdapter(Activity activity, ArrayList<ConnectionObject> connections){
        this.context = activity;
        this.activity = activity;
        this.connections = connections;
    }



    @Override
    public int getCount() {
        if(connections!=null){
            return connections.size();
        }
        else {
            return 0;
        }

    }

    @Override
    public Object getItem(int i) {
        return connections.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater==null){
            layoutInflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView = layoutInflater.inflate(R.layout.connection_layout,null);
        }

        name = convertView.findViewById(R.id.connection_name);
        handle = convertView.findViewById(R.id.connection_handle);
        roundedImageView = convertView.findViewById(R.id.connection_image);
        ConnectionObject connection = connections.get(i);
        name.setText(connection.getName());
        handle.setText(connection.getHandle());
        Picasso.get().load(connection.getProfilePic()).into(roundedImageView);
        //TODO: HERE if clicked on any of this view, The user should be redirected to profile of that that particular user!
        // 1.5.2019

        handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("profiles").
                        document(connection.getFirebaseUI()).get().addOnCompleteListener(
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
