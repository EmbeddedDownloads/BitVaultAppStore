package com.bitvault.appstore.utils;

import com.paypal.android.sdk.payments.PayPalConfiguration;

/**
 * Created by linchpin on 18/5/17.
 */
public interface AppConstants {

    int ACTIVITY_INTERNET_CONNECTION_RESULT_CODE = 1;
    int aPP_DOWNLOAD_NOTIFICATION_ID = 1;
    int APP_INSTALLING = 1;
    String ANDROID = "android";
    int APK_FILE_DOWNLOADING = 0;

    //remove later
    //public static final String PAYPAL_CLIENT_ID = "AYOSIgdx_puRC29PCtSh88Jb4GSHzrTVUkyKmq0I9Kvadg4QGciucDcQWtXP2CYHMc-Vcm9xXxgx2Q2L";
    String PAYPAL_CLIENT_ID = "AcgBnaW-XRB2HBKwLQoLIg7t5Txdpl5zC4DFRUqspGXyGOPErp9KPVDEoEf5O0XmwHjWOtVEYRNIn30r";
    String CURRENCY = "USD";
    String PAYMENT_DESC = "Anuj Payment Test";
    String PAYPAL_PAYMENT_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
}
