package com.bitvault.appstore.application;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bitvault.appstore.R;
import com.bitvault.appstore.utils.LruBitmapCache;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import controller.SDKControl;



/*
 * Created by Dheeraj Bansal on 27/4/17.
 * version 1.0.0
 */

@ReportsCrashes(formUri = "", // will not be used
        mailTo = "anuj.kumar@vvdntech.in", // my email here
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text,
        resDialogOkToast = R.string.crash_ok_toast
)

/**
 * Application class
 */
public class AppStoreApplication extends SDKControl {


    public static final String TAG = AppStoreApplication.class
            .getSimpleName();
    private static AppStoreApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


    public static synchronized AppStoreApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // The following line triggers the initialization of ACRA
        ACRA.init(this);

        //use later
        // Setup handler for uncaught exceptions.
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
//
//                System.exit(0);
//
//            }
//        });
    }

    /**
     * return request of API
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * return ImageLoder instance
     *
     * @return
     */
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    /**
     * add API request in queue with tag
     *
     * @param req
     * @param tag
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * add API request in queue
     *
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * cancel pending request of API
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}