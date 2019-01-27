package com.getmate.demo181201.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.getmate.demo181201.EventListAdapter;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class TimelineFragment extends Fragment {
    ArrayList<Event> events = new ArrayList<>();
    private Profile currentUserProfile;
    ProgressDialog progressDialog;
    public static ListView listView ;
    public static EventListAdapter eventListAdapter;

    private boolean flag_loading= false;
    private int a =1;



    public TimelineFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Kaun","OnCreate called");

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Kaun","OnCreateView called");
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_timeline, container, false);
        listView = view.findViewById(R.id.frag1_list);
        progressDialog = new ProgressDialog(this.getActivity());

        Bundle bundle = getArguments();
        if (bundle!=null){
            currentUserProfile = bundle.getParcelable("currentUserProfile");
            if (currentUserProfile!=null){
                //Update UI
            }


            events = bundle.getParcelableArrayList("events");
            if (events!=null){
                //Update UI
            }
        }

        demoFunction();

        eventListAdapter = new EventListAdapter(this.getActivity(),events);
        listView.setAdapter(eventListAdapter);


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(flag_loading == false)
                    {
                        flag_loading = true;
                        a++;
                        //additems(); a function to load another 20 items

                    }
                }

            }
        });


        return view;

    }



    private void demoFunction(){

        progressDialog.show();
        FirebaseFirestore.getInstance().collection("events").
                document("MAXFIMvh7CKlNZYZwRnD").
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Event event = new Event();
                if (documentSnapshot!=null){

                    event = documentSnapshot.toObject(Event.class);
                    if (event!=null){
                        events.add(event);

                        Gson gson = new Gson();
                        String json = gson.toJson(event);
                        Log.i("KaunHai",documentSnapshot.toString());
                        Log.i("KaunJuMein",json);
                        Log.i("Kaun",event.getTitle());

                        eventListAdapter = new EventListAdapter(getActivity(),events);
                        listView.setAdapter(eventListAdapter);
                        progressDialog.dismiss();

                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"NOT REACHABLE",Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    progressDialog.dismiss();
                    Log.i("KaunHai","documnent snapshot is null");
                }

            }
        });
    }


    private  boolean getProfileDataBoolean =false;
    private boolean getProfileData(String firebase_id) {
        progressDialog.show();
        //or u can set the document name by the firebase Id itself, that would be nice
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("profiles").document(firebase_id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                currentUserProfile = documentSnapshot.toObject(Profile.class);
                if (currentUserProfile!=null){

                    progressDialog.dismiss();

                    SharedPreferences.Editor editor =
                            getActivity().getSharedPreferences("currentUserData", MODE_PRIVATE).edit();
                    Gson gson =new Gson();
                    String json = gson.toJson(currentUserProfile);
                    editor.putString("currentUserDataInString",json);
                    editor.apply();

                }
                Log.i("KaunH",documentSnapshot.toString());
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("KaunH","getProfileDataFailed"+e.getCause());
                Log.e("KaunH","getProfileDataFailed",e.getCause());
                getProfileDataBoolean = false;
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Coudn't receive profile data from server",Toast.LENGTH_SHORT).show();
            }
        });

        return getProfileDataBoolean;
    }


}
