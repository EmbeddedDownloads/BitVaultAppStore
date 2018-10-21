package com.bitvault.appstore.services;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;


/*
 * Created Dheeraj Bansal root on 5/5/17.
 * version 1.0.0
 * broadcast notification after download complete
 */

//later use
public class ServiceNotification extends NotificationListenerService {
//    private String TAG = this.getClass().getSimpleName();
//    public static final String NOT_TAG = "com.gopcpro.app.NOTIFICATION_LISTENER";
//    public static final String NOT_POSTED = "POSTED";
//    public static final String NOT_REMOVED = "REMOVED";
//    public static final String NOT_EVENT_KEY = "not_key";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        AndroidAppUtils.showLogI(TAG, "**********  onNotificationPosted");
//        Intent i = new Intent(NOT_TAG);
//        i.putExtra(NOT_EVENT_KEY, NOT_POSTED);
//        sendBroadcast(i);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        AndroidAppUtils.showLogI(TAG, "********** onNOtificationRemoved");
//        Intent i = new Intent(NOT_TAG);
//        i.putExtra(NOT_EVENT_KEY, NOT_REMOVED);
//        sendBroadcast(i);
    }

}