package com.creadigol.inshort.Utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;



/**
 * Created by.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static ImageLoader mImageLoader;
    private static Context mContext;
    private PreferenceSettings mPreferenceSettings;
    private int MY_SOCKET_TIMEOUT_MS = 20000;
    public static int pageChnage;
    public static boolean isNeeded=true;
    public static boolean bookmark=false;
    public static String link="www.google.com";
    public static String syncTime="0";
    public static final String TAG = MyApplication.class
            .getSimpleName();
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        mPreferenceSettings = getPreferenceSettings();
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }



    public ImageLoader getImageLoader(){
        if(mImageLoader == null){
            // UNIVERSAL IMAGE LOADER SETUP
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    mContext)
                    .defaultDisplayImageOptions(getDefaultOptions())
                    .memoryCache(new WeakMemoryCache())
                    .diskCacheSize(100 * 1024 * 1024).build();

            mImageLoader = ImageLoader.getInstance();
            mImageLoader.init(config);
            // END - UNIVERSAL IMAGE LOADER SETUP
        }
        return mImageLoader;
    }

    public DisplayImageOptions getDefaultOptions(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        return defaultOptions;
    }

    public PreferenceSettings getPreferenceSettings() {
        if (mPreferenceSettings == null) {
            mPreferenceSettings = new PreferenceSettings(mContext);
        }
        return mPreferenceSettings;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
