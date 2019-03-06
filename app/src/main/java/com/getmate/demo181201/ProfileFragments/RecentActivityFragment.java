package com.getmate.demo181201.ProfileFragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.getmate.demo181201.Adapters.SavedEventsListAdapter;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class RecentActivityFragment extends Fragment {
     FirebaseFirestore db;
    ArrayList<Event> events = new ArrayList<>();
    ListView listView;

    private ArrayList<String> recentActivities = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public RecentActivityFragment() {
        // Required empty public constructor
    }


public RecentActivityFragment(ArrayList<String> recentActivities){
        this.recentActivities = recentActivities;
}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recent_activity, container, false);
        db= FirebaseFirestore.getInstance();
        SavedEventsListAdapter savedEventsListAdapter = new SavedEventsListAdapter(getActivity(),events);
        listView = view.findViewById(R.id.recent_activity_list_view);
        listView.setAdapter(savedEventsListAdapter);


        if (recentActivities!=null){
            for (String s:recentActivities) {
                db.collection("events").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Event event = new Event();
                            event = task.getResult().toObject(Event.class);
                            events.add(event);
                            savedEventsListAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }

        else {
            Toast.makeText(getActivity(),"No recent Activities",Toast.LENGTH_LONG).show();
        }

        return  view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
