package com.bitvault.appstore.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bitvault.appstore.utils.AndroidAppUtils;

/**
 * Created Dheeraj Bansal root on 18/5/17.
 * version 1.0.0
 * call after install app.
 */

public class InstallReceiver extends BroadcastReceiver {

    private InstallAppListener mInstallAppListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(mInstallAppListener != null) {
            mInstallAppListener.onInstallApp();
        }

    }

    public interface InstallAppListener {
        public void onInstallApp();
    }

    public void setOnAppInstallListener(InstallAppListener mInstallAppListener) {
        this.mInstallAppListener = mInstallAppListener;

    }
}
