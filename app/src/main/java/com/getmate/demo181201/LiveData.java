package com.getmate.demo181201;

import com.getmate.demo181201.Objects.Profile;
import com.google.android.gms.maps.model.LatLng;

public class LiveData {

    private static  LiveData instance;
    private Profile currentUserProfile;
    private String currentCity;
    private LatLng currentlLocation;


    private LiveData(){
        currentCity = new String();
        currentlLocation = new LatLng(0,0);
        currentUserProfile = new Profile();
    }

    public static LiveData getInstance(){
        if (instance ==null){
            instance = new LiveData();
        }
        return instance;
    }

    public static void setInstance(LiveData instance) {
        LiveData.instance = instance;
    }

    public Profile getCurrentUserProfile() {
        return currentUserProfile;
    }

    public void setCurrentUserProfile(Profile currentUserProfile) {
        this.currentUserProfile = currentUserProfile;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public LatLng getCurrentlLocation() {
        return currentlLocation;
    }

    public void setCurrentlLocation(LatLng currentlLocation) {
        this.currentlLocation = currentlLocation;
    }
}
