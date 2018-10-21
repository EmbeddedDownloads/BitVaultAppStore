package com.bitvault.appstore.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bitvault.appstore.R;

import java.lang.reflect.Method;

/**
 * Created by Dheeraj Bansal on 7/2/17.
 * version 1.0.0
 * for network connection check
 */

public class NetworkConnection {

    public static boolean isNetworkAvailable(Context context) {

        boolean flag = false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        flag = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!flag) {

        }
        return flag;
    }
}
