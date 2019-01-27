package com.getmate.demo181201.VolleyClasses;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Dhruv on 5/27/2018.
 */

public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }
    public LruBitmapCache(){
        this(getDefaultLruCacheSize());
    }

    public static int getDefaultLruCacheSize(){
        final int maxmemory = (int)(Runtime.getRuntime().maxMemory()/1024);
            final int cachesize = maxmemory/8;
            return  cachesize;
    }

    @Override
    public Bitmap getBitmap(String s) {
       return get(s);
    }
    protected int sizeOf(String key,Bitmap value){
        return value.getRowBytes()*value.getHeight()/1024;

    }


    @Override
    public void putBitmap(String s, Bitmap bitmap) {
put(s,bitmap);
    }
}
