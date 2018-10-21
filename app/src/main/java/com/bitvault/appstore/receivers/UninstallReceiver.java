package com.bitvault.appstore.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bitvault.appstore.R;
import com.bitvault.appstore.application.APIs;
import com.bitvault.appstore.preference.AppPreferences;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.NetworkConnection;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.STRINGRequest;

/**
 * Created Dheeraj Bansal root on 18/5/17.
 * version 1.0.0
 * call after uninstall app
 */

public class UninstallReceiver extends BroadcastReceiver {

    private final static int UNINSTALL_APK_FILE = 1;
    private static UninstallRecieverListener uninstallRecieverListener;
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    public void onReceive(final Context context, Intent intent) {

        //remove later
//        AndroidAppUtils.showToast(context, intent.getData().toString());
//        AndroidAppUtils.showLogD("String Uninstall", intent.getData().toString());

        if (uninstallRecieverListener != null) {
            uninstallRecieverListener.onAppUninstall();
            uninstallRecieverListener = null;
        }

        sendUninstallInfo(context, intent.getData().getSchemeSpecificPart());

    }

    /**
     * method for send app uninstall information to server
     * @param context
     * @param packageName
     */
    public void sendUninstallInfo(final Context context, final String packageName) {

        if(NetworkConnection.isNetworkAvailable(context)) {
            //for hit install app api
            //remove later
            //String appId = AppPreferences.getInstance(context).getDeleteAppId();
            String param = "";
            if (packageName != null) {
                param = param.concat(context.getString(R.string.packageNameList));
                param = param.concat("=");
                param = param.concat(packageName);
                param = param.concat("&");
                param = param.concat(context.getString(R.string.action));
                param = param.concat("=");
                param = param.concat(context.getString(R.string.Uninstall));
                param = param.concat("&");
                param = param.concat(context.getString(R.string.publicAddress));
                param = param.concat("=");
                param = param.concat(Utils.getPublicAddress(context));
            }

            String url = APIs.API_UNINSTALL_MULTIPLE_APK + param;
            new STRINGRequest().postRequest(context, new STRINGRequest.StringResponseListener() {
                @Override
                public void onResponseSuccess(String response, int requestCode) {
                    AndroidAppUtils.showLogD(TAG, response);
                    AppPreferences.getInstance(context).setUninstallAppPackage(null);
                }

                @Override
                public void onResponseFailure(VolleyError error) {
                    AndroidAppUtils.showLogD(TAG, error.toString());
                    sendUninstallInfo(context, packageName);
                }
            }, null, url, Request.Method.POST, UNINSTALL_APK_FILE);
        } else {
            AppPreferences.getInstance(context).setUninstallAppPackage(packageName);
        }
    }

    /**
     * This method register listerner for uninsatll application
     *
     * @param unstallReceiverListener
     */
    public void setOnUnstallReceiverListener(UninstallRecieverListener unstallReceiverListener) {
        this.uninstallRecieverListener = unstallReceiverListener;

    }

    /**
     * Uninstall application listener
     */
    public interface UninstallRecieverListener {
        void onAppUninstall();
    }

}
