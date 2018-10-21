package com.bitvault.appstore.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bitvault.appstore.R;
import com.bitvault.appstore.svlibrary.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vaibhav Jain on 6/10/16.
 * version 1.0.0
 * Some common functions like read response from file, check app installed etc.
 */

public class Utils {

    public static final String TAG_PRODUCT_DETAIL_FRAGMENT = "ProductDetailFragment";
    public static final String TAG_HOME_FRAGMENT = "HomeFragment";
    public static final String TAG_CATEGORY_FRAGMENT = "CategoryFragment";
    public static final String TAG_SEARCH_FRAGMENT = "SearchFragment";
    public static final String TAG_INTERNET_CONNNECTION_FRAGMENT = "InternetConnectionFragment";
    public static final String TAG_MY_APP_FRAGMENT = "MyAppFragment";
    public static final String TAG_REQUEST_CALLBACK_FRAGMENT = "PhoneCallbackRequestFragment";
    public static final String TAG_FEEDBACK_FRAGMENT = "FeedbackFragment";
    public static final String TAG_APP_UPDATE_DIALOG = "AppUpdateDialog";
    public static final String TAG_PURCHASE_AUTH_DIALOG = "AppUpdateDialog";
    public static final String TAG_PAYMENT_DIALOG = "AppUpdateDialog";
    public static final int TAG_INTERNET_NOT_AVAILABLE = 0;
    public static final int TAG_TIMEOUT_ERROR = 1;
    public static final String TAG_DOWNLOAD_KEY = "downloadKey";
    public static final String TAG_RESULT = "result";
    public static final CharSequence TAG_APP_DETAIL = "app_detail";


    /**
     * Method to check service running status
     *
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check the Internet Connectiity
     *
     * @param mContext
     * @return -- true / false
     */
    public static boolean isConnectedToInternet(Context mContext) {

        ConnectivityManager connMgr = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // ARE WE CONNECTED TO THE NET

        if (connMgr.getActiveNetworkInfo() != null
                && connMgr.getActiveNetworkInfo().isAvailable()
                && connMgr.getActiveNetworkInfo().isConnected())
            return true;
        else {
            return false;
        }
    }

    /**
     * This Method is used to get whether Edittext is null or have non -empty values
     *
     * @param mEditext - EditText for which for to test
     * @return - true/false
     */

    public static boolean isEmptyOrNot(EditText mEditext) {

        if (mEditext != null && !(mEditext.getText().toString().trim().equals("")))
            return true;
        else
            return false;
    }


    /**
     * This Method is used to get whether TextView is null or have non -empty values
     *
     * @param mTextview - textview for which for to test
     * @return - true/false
     */

    public static boolean isEmptyOrNot(TextView mTextview) {

        if (mTextview != null && !(mTextview.getText().toString().trim().equals("")))
            return true;
        else
            return false;
    }


    /**
     * Method is used to Validate Email Address
     *
     * @param email - email Address
     * @return - true / false
     */

    public final static boolean isValidEmail(CharSequence email) {
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    /**
     * convert util date into String with specific format
     *
     * @param date   --- Provide Date Object here
     * @param format --- Date format
     * @return --- date in strings
     */
    public static String convertDateToString(Date date, String format) {

        DateFormat df = new SimpleDateFormat(format);
        String str = df.format(date);
        return str;

    }

    /**
     * convert String to Date
     *
     * @param dateString -- date in string
     * @param format     -- date format
     * @return -- Date Object
     */
    public static Date convertStringToDate(String dateString, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }


    /**
     * Method to get Current date and Time in the given format
     *
     * @param format - Format
     */

    public static String getCurrentDateAndTime(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat(format);
        mdformat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return mdformat.format(calendar.getTime());
    }

    /**
     * method
     * @param context
     * @return
     */
    public static boolean isKeyboardOpen(final Context context, final View view) {

        //remove later

//        InputMethodManager imm = (InputMethodManager)
//                context.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        if (imm.isAcceptingText()) {
//            AndroidAppUtils.showLogD("Utils", "Software Keyboard was shown");
//            return true;
//        } else {
//            AndroidAppUtils.showLogD("Utils", "Software Keyboard was not shown");
            return false;
//        }

//        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int heightDiff = view.getRootView().getHeight() - view.getHeight();
//                if (heightDiff > Util.dpToPx(context, 200)) { // if more than 200 dp, it's probably a keyboard...
//                    // ... do something here
//
//                }
//            }
//        });

    }



    /**
     * use for hide keyboard
     *
     * @param mContext
     * @param mView
     */
    public static void hideKeyboard(Context mContext, View mView) {
        if (mView != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mView.getWindowToken(), InputMethodManager.SHOW_FORCED);
            mView.clearFocus();
        }
    }

    /**
     * check activity is in stack
     *
     * @param activityName
     * @return
     */
    public static boolean isActivityExist(Context context, String activityName) {
        ActivityManager mngr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        for (int i = 0; i < taskList.size(); i++) {
            return taskList.get(i).topActivity.getClassName().equals(activityName);
        }
        return false;
    }

    /**
     * check APP is installed or not
     *
     * @param packagename
     * @param context
     * @return
     */
    public static boolean isPackageInstalled(String packagename, Context context) {

        boolean packageInstall;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packagename, 0);
            packageInstall = true;
        } catch (PackageManager.NameNotFoundException e) {
            packageInstall = false;
        }
        return packageInstall;
    }

    /**
     * get version no of Installed app
     *
     * @param packagename
     * @param context
     * @return
     */
    public static int getVersionNumber(String packagename, Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    packagename, 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    /**
     * get version name of given installed application package name
     * @param context
     * @param packageName
     * @return
     */
    public static String getVersionName(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    packageName, 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return context.getString(R.string.version_name);
        }
    }

    /**
     * check screen orientation
     *
     * @param context
     * @return
     */
    public static boolean isPortraitOrientation(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Read API static response from file
     *
     * @param inputStream
     * @return
     */
    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }


    /**
     * method return toolbar height
     *
     * @param context
     * @return
     */
    public static int getToolbarHeight(Context context) {

        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return context.getResources().getDimensionPixelSize(tv.resourceId);
    }

    /**
     * return screen width and height
     *
     * @param context
     * @return
     */
    public static int[] getScreenDimensions(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    /**
     * return permission icon drawable
     *
     * @param context
     * @param permissionGroup
     * @return
     */
    @Nullable
    public static Drawable getPermissionDrawable(Context context, String permissionGroup) {
        PackageManager mPackageManager = context.getPackageManager();
        Drawable drawable = null;
        try {
            PermissionGroupInfo groupInfo = mPackageManager.getPermissionGroupInfo(permissionGroup, 0);
            drawable = ResourcesCompat.getDrawable(mPackageManager.getResourcesForApplication(AppConstants.ANDROID), groupInfo.icon, null);
        } catch (PackageManager.NameNotFoundException | Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * method return string first letter in upper case and other letter of string in lower case
     *
     * @param str
     * @return
     */
    public static String getFirstLetterUpperandOtherSmall(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * get All application Info install in device
     * @param context Used to get list of ResolveInfo list
     * @return list of ResolveInfo objects
     */
    public static List<ResolveInfo> getAppMetaData(Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return context.getPackageManager().queryIntentActivities(mainIntent, 0);

    }

    /**
     * Return list of UsageStats Class for get app last open time
     *
     * @param intervalType
     * @return
     */
    public static List<UsageStats> getUsageStatistics(Context context, int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {
            context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        return queryUsageStats;
    }

    /**
     * Return time interval in week, days, hour and minute
     *
     * @param context
     * @param miliseconds
     * @return
     */
    public static String getTimeInterval(Context context, long miliseconds) {
        String updateTime = "";

        long milisecondsDifference = System.currentTimeMillis() - miliseconds;
        if (TimeUnit.MILLISECONDS.toDays(milisecondsDifference) < 365 && TimeUnit.MILLISECONDS.toDays(milisecondsDifference) > 6) {
            updateTime = TimeUnit.MILLISECONDS.toDays(milisecondsDifference) / 7 + " " + context.getString(R.string.week) + " " + context.getString(R.string.ago);

        } else if (TimeUnit.MILLISECONDS.toDays(milisecondsDifference) > 1) {
            updateTime = TimeUnit.MILLISECONDS.toDays(milisecondsDifference) + " "
                    + context.getString(R.string.days) + " " + context.getString(R.string.ago);
        } else if (TimeUnit.MILLISECONDS.toDays(milisecondsDifference) == 1) {
            updateTime = context.getString(R.string.yesterday);
        } else if (TimeUnit.MILLISECONDS.toHours(milisecondsDifference) < 24 && TimeUnit.MILLISECONDS.toHours(milisecondsDifference) > 0) {
            updateTime = TimeUnit.MILLISECONDS.toHours(milisecondsDifference) + " "
                    + context.getString(R.string.hr) + " " + context.getString(R.string.ago);
        } else if (TimeUnit.MILLISECONDS.toMillis(milisecondsDifference) < 60) {
            updateTime = TimeUnit.MILLISECONDS.toMinutes(milisecondsDifference) + " "
                    + context.getString(R.string.min) + " " + context.getString(R.string.ago);
        }
        return updateTime;
    }

    /**
     * method for create application shortcut on Phone Home Screen
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void createAppShortCutOnHomeScreen(Context context, String packageName, String appName) {

        try {

            Intent shortcutIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);

            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ((BitmapDrawable) context.getPackageManager().getApplicationIcon(packageName)).getBitmap());
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(addIntent);
        } catch (PackageManager.NameNotFoundException mNameNotFoundException) {
            mNameNotFoundException.printStackTrace();
        }

    }

    /**
     * return public address
     * @param context
     * @return
     */
    public static String getPublicAddress(Context context) {
        return context.getString(R.string.anuj);
    }

    /**
     * return version of application
     * @param context
     * @return
     */
    public static CharSequence getAppStoreVersion(Context context) {

        return context.getString(R.string.version) + ": 8.3.73.U-all [0] [FP] 173262113";
    }

    /**
     * method create alert dialog
     * @param context
     * @param title
     * @param message
     * @param neutralButtonText
     * @param positiveButtonText
     * @param negativeButtonText
     * @param onClickListener
     */
    public static void showAlertDialog(Context context, String title, String message, String neutralButtonText,
                                       String positiveButtonText, String negativeButtonText,
                                       DialogInterface.OnClickListener onClickListener) {

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alt_bld.setMessage(message).setCancelable(false);

        //Set neutral button
        if (neutralButtonText != null) {
            if (onClickListener != null)
                alt_bld.setNeutralButton(neutralButtonText, onClickListener);
            else
                alt_bld.setNeutralButton(neutralButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        }
        //Set positive button
        if (positiveButtonText != null && onClickListener != null) {
            alt_bld.setPositiveButton(positiveButtonText, onClickListener);
        }

        //Set negative button
        if (negativeButtonText != null && onClickListener != null) {
            alt_bld.setNegativeButton(negativeButtonText, onClickListener);
        }
        AlertDialog alert = alt_bld.create();
        alert.setTitle(title);
        alert.show();
    }
}
