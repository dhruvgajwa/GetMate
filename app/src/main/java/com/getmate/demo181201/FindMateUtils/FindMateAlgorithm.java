package com.getmate.demo181201.FindMateUtils;

import android.support.annotation.NonNull;

import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.Objects.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class FindMateAlgorithm  {

    FirebaseFirestore db;

    Profile currentUserData ;

    public FindMateAlgorithm(Profile currentUserData){

        this.currentUserData= currentUserData;
        db = FirebaseFirestore.getInstance();
    }

    public ArrayList<Profile> getRecommendedProfiles(){

        ArrayList<String> ids =currentUserData.getSavedEvents();
        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Profile> profilesToBeShown = new ArrayList<>();
        ArrayList<String> allGoingProfilesIds = new ArrayList<>();


        Calendar c = Calendar.getInstance();
        Long t = c.getTimeInMillis();

        for (String s: ids) {
            db.collection("events").document(s).get().
                    addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot!=null){
                        Event e = documentSnapshot.toObject(Event.class);
                        if (Long.valueOf(e.getTime())>t){
                            events.add(e);
                            //TODO:ADD IF NOT PRESENT
                            //TODO:ADD IF NOT SWIPED RIGHT
                            allGoingProfilesIds.addAll(e.getGoingProfile());

                        }
                        }
                }
            });
        }
        int i = allGoingProfilesIds.size();
        //TODO: get ten in one instant


        for (String s : allGoingProfilesIds) {
            db.collection("profiles").document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot!=null){
                        profilesToBeShown.add(documentSnapshot.toObject(Profile.class));
                    }
                }
            });
        }


return profilesToBeShown;

    }



    public ArrayList<Profile> getPoeopleFromSameCity(){
        ArrayList<Profile> peopleFromSameCity = new ArrayList<>();




        db.collection("profiles").whereEqualTo("city",currentUserData.getCity())
                .get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot querry: task.getResult()){
                                peopleFromSameCity.add(querry.toObject(Profile.class));
                            }
                        }
                    }
                }
        );

        return peopleFromSameCity;
    }

}
