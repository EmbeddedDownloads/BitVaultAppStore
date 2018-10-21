package com.bitvault.appstore.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bitvault.appstore.R;
import com.bitvault.appstore.app.appdetail.FileDownloadManager;
import com.bitvault.appstore.database.DbHelper;
import com.bitvault.appstore.dialog.PaymentDialog;
import com.bitvault.appstore.preference.AppPreferences;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.AppStoreUtils;
import com.bitvault.appstore.utils.Utils;

import java.io.File;

/**
 * Created Dheeraj Bansal root on 18/5/17.
 * version 1.0.0
 * call after networrk change
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static boolean isUploadingStart = false;
    private static boolean isDownloadingStart = false;
    private final String TAG = NetworkChangeReceiver.class.getName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (Utils.isConnectedToInternet(context)) {
            AndroidAppUtils.showLogD(TAG, context.getResources().getString(R.string.wifi_connected));
            Long downloadingFileInfo[] = DbHelper.getInstance(context).getDownloadingFileInfo();
            if(downloadingFileInfo[0] != -1) {
                new FileDownloadManager.FileDownloadInfo(context, null, String.valueOf(downloadingFileInfo[0]), DbHelper.getInstance(context).getDownloadingFileName(context),
                        DbHelper.getInstance(context).getDownloadingFilePackageName(context)).startAppDownload(true);
            }
            if (!isDownloadingStart) {
                isDownloadingStart = true;

            }

            String unInstallAppPackages = AppPreferences.getInstance(context).getUninstallAppPakages();
            if(unInstallAppPackages != null) {
                new UninstallReceiver().sendUninstallInfo(context, unInstallAppPackages);
            }

            String downloadAppPackages = AppPreferences.getInstance(context).getDownloadAppPakages();
            if(downloadAppPackages != null) {
                AppStoreUtils.sendDownloadInfo(context, downloadAppPackages);
            }

            String installedPackage = AppPreferences.getInstance(context).getInstalledAppPakages();
            if(installedPackage != null) {
               new FileDownloadManager.InstallReceiver().sendAppInstallInfoToServer(context, installedPackage);
            }

            String paymentInfo = AppPreferences.getInstance(context).getPaymentInfo();
            String paymentInfoExtra = AppPreferences.getInstance(context).getPaymentInfoExtra();
            if(paymentInfo != null && paymentInfoExtra != null) {
                String paymentInfoStr[] = paymentInfo.split(File.separator);
                String paymentInfoExtraStr[] = paymentInfoExtra.split(File.separator);
                if(paymentInfoStr.length == 2) {
                    new PaymentDialog().sendPaymentInfo(context, paymentInfoStr[0], paymentInfoStr[1], Double.parseDouble(paymentInfoExtraStr[3])
                            , paymentInfoExtraStr[4], Long.parseLong(paymentInfoExtraStr[2]));
                } else {
                    new PaymentDialog().sendPaymentInfo(context, context.getString(R.string.blank_space), context.getString(R.string.Failure),
                            Double.parseDouble(paymentInfoExtraStr[3])
                            , paymentInfoExtraStr[4], Long.parseLong(paymentInfoExtraStr[2]));
                }

            }
        } else {
            isUploadingStart = false;
            isDownloadingStart = false;

        }
    }
}
