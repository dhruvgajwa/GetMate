package com.getmate.demo181201;

import android.support.v4.util.LruCache;

public class Cache {
     int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);


    private static Cache instance;
    private LruCache<Object, Object> lru;

    private Cache() {

        lru = new LruCache<Object, Object>(1024);

    }

    public static Cache getInstance() {

        if (instance == null) {

            instance = new Cache();
        }

        return instance;

    }

    public LruCache<Object, Object> getLru() {
        return lru;
    }
}