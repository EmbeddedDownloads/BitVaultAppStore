package com.bitvault.appstore.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created Dheeraj Bansal root on 15/5/17.
 * version 1.0.0
 * used for logger
 */

public class AndroidAppUtils {

    public static void showLogD(String tag,String msg){
        Log.d(tag,msg);
    }

    public static void showLogE(String tag,String msg){
        Log.e(tag,msg);
    }

    public static void showLogW(String tag,String msg){
        Log.w(tag,msg);
    }

    public static void showLogI(String tag,String msg){
        Log.i(tag,msg);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
