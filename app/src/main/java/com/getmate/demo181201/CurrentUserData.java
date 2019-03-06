package com.getmate.demo181201;

import com.getmate.demo181201.Objects.Profile;

public  class CurrentUserData {

    private static CurrentUserData instance;
    private Profile current;

    private CurrentUserData() {

        current = new Profile();
    }

    public static CurrentUserData getInstance() {

        if (instance == null) {

            instance = new CurrentUserData();
        }

        return instance;

    }

    public Profile getCurrent() {
        return current;
    }

    public void setCurrent(Profile current) {
        this.current = current;
    }
}

