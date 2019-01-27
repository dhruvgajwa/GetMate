package com.getmate.demo181201;

import com.getmate.demo181201.Objects.Profile;

public  class CurrentUserData {
    public static Profile currentUserProfile;
    public static  CurrentUserData mInstance = null;


    protected CurrentUserData(){}
    public static synchronized CurrentUserData getmInstance(){
        if (mInstance==null){
            mInstance = new CurrentUserData();
            }
        return mInstance;
    }
}

