package com.getmate.demo181201.CustomViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout.LayoutParams;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Dhruv on 5/27/2018.
 */

public class EventTimelineImageView extends android.support.v7.widget.AppCompatImageView {

    public interface ResponseObserver{
        public void onError();
        public void onSuccess();

    }

    private ResponseObserver mObserver;

    public void setResponseObserver(ResponseObserver observer){
        mObserver= observer;
    }

    //The URL to load network Images
    public String mUrl;


    private int mDefaultImageId; //Resource ID of Default IMage

    private int mErrorImageID;

    private ImageLoader mImageLoader;

    private ImageLoader.ImageContainer imageContainer;




    public EventTimelineImageView(Context context) {
        this(context,null);
    }

    public EventTimelineImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EventTimelineImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String url, ImageLoader imageLoader){
        mUrl=url;
        mImageLoader =imageLoader;

        loadImageIfNecessary(false);
    }

    public void setDeafaultImageResID(int defaultImage){
        mDefaultImageId =defaultImage;
    }

    public void setmErrorImageID(int errorImageID){
        mErrorImageID = errorImageID;
    }

    private void loadImageIfNecessary(final boolean isInLayoutPass) {
        final int width = getWidth();
        final int height = getHeight();

        boolean ifFullyWrapContent = getLayoutParams()!=null &&
                getLayoutParams().height==LayoutParams.WRAP_CONTENT && getLayoutParams().width==LayoutParams.WRAP_CONTENT;

        if(width==0 && height ==0 && !ifFullyWrapContent){
            return;
        }

        if(TextUtils.isEmpty(mUrl)) {
            if (imageContainer != null) {
                imageContainer.cancelRequest();
                ;
                imageContainer = null;
            }

            setDefaultImageOrNull();
            return;
        }
        //what if there is already a request in this view, check if it needs to be
        if(imageContainer!=null &&imageContainer.getRequestUrl()!=null){

            if(imageContainer.getRequestUrl().equals(mUrl)){
                return;
            }
            else {
                imageContainer.cancelRequest();
                setDefaultImageOrNull();
            }
        }

        //The previous one didn't matched so need to load image
        ImageLoader.ImageContainer newImageContainer = mImageLoader.get(mUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse( final ImageLoader.ImageContainer response, boolean isImmediate) {

                if(isImmediate&&isInLayoutPass){
                    post(new Runnable() {
                        @Override
                        public void run() {
                            onResponse(response, false);
                        }
                    });
                    return;

                }
                int bWidth =0, bHeight = 0;
                if(response.getBitmap()!=null){
                    setImageBitmap(response.getBitmap());
                    bWidth=response.getBitmap().getWidth();
                    bHeight = response.getBitmap().getHeight();
                    adjustImageAspects(bWidth,bHeight);

                }
                else if(mDefaultImageId!=0){
                    setImageResource(mDefaultImageId);
                }

                if(mObserver!=null){
                    mObserver.onSuccess();
                }


            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mErrorImageID!=0){
                    setImageResource(mErrorImageID);
                }

                if(mObserver!=null){
                    mObserver.onError();
                }

            }
        });

        imageContainer = newImageContainer;


    }

    private void adjustImageAspects(int bWidth, int bHeight) {

        LayoutParams params = (LayoutParams) getLayoutParams();

        if(bWidth ==0 || bHeight==0){
            return;
        }
        int sWidth = getWidth();
        int new_height = 0;
        new_height = sWidth*bHeight/bWidth;
        params.width = sWidth;
        params.height = new_height;
        setLayoutParams(params);
    }


    private void setDefaultImageOrNull() {
        if(mDefaultImageId!=0){
            setImageResource(mDefaultImageId);
        }
        else {
            setImageBitmap(null);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        loadImageIfNecessary(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (imageContainer != null) {
            // If the view was bound to an image request, cancel it and clear
            // out the image from the view.
            imageContainer.cancelRequest();
            setImageBitmap(null);
            // also clear out the container so we can reload the image if
            // necessary.
            imageContainer = null;
        }
        super.onDetachedFromWindow();
    }
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    
}
