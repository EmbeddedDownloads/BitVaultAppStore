package com.bitvault.appstore.app.myapp;

import android.app.DownloadManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.appdetail.FileDownloadManager;
import com.bitvault.appstore.app.appdetail.ProductDetailFragment;
import com.bitvault.appstore.app.dashboard.MainActivity;
import com.bitvault.appstore.database.DbHelper;
import com.bitvault.appstore.databinding.ItemAllMyappBinding;
import com.bitvault.appstore.databinding.ItemAllMyappTextBinding;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.AppConstants;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.response.AppDetailResponse;
import com.bitvault.appstore.webservice.response.MyAppVersionModel;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.bitvault.appstore.R.string.applicationId;
import static com.bitvault.appstore.R.string.package_name;
import static com.bitvault.appstore.R.string.result;

/**
 * Created by anuj on 8/5/17.
 * version 1.0.0
 * Install apps adapter
 */

public class MyAllAppListAdapter extends RecyclerView.Adapter {

    private static final int RECENT_UPDATE_TYPE = 2;
    private static final int UPDATE_TYPE = 1;
    private static final int INSTALLED_TYPE = 3;
    private static final int RECENT_UPDATE_TYPE_TEXT = 5;
    private static final int UPDATE_TYPE_TEXT = 4;
    private static final int INSTALLED_TYPE_TEXT = 6;
    private ArrayList<FileDownloadManager.FileDownloadListener> fileDownloadListenerarray = new ArrayList<FileDownloadManager.FileDownloadListener>();
    private static List<UsageStats> usageStatsList;
    ArrayList<MyAppVersionModel.MyAppVersion> myAppVersionModelArrayList;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<CustomResolveInfo> updatesArrayList;
    private ArrayList<CustomResolveInfo> recentlyUpdatedArrayList;
    private ArrayList<ResolveInfo> resolveInfoInstalledArrayList;
    private int recentlistsize, updateListSize = 1, totalListSize = 1;
    private String source;
    private DecimalFormat decimalFormat;

    public MyAllAppListAdapter(Context context, ArrayList<CustomResolveInfo> updatesArrayList,
                               ArrayList<CustomResolveInfo> recentlyUpdatedArrayList, ArrayList<ResolveInfo> resolveInfoInstalledArrayList,
                               ArrayList<MyAppVersionModel.MyAppVersion> myAppVersionModelArrayList, String source) {

        this.context = context;
        this.updatesArrayList = updatesArrayList;
        this.recentlyUpdatedArrayList = recentlyUpdatedArrayList;
        this.resolveInfoInstalledArrayList = resolveInfoInstalledArrayList;
        this.myAppVersionModelArrayList = myAppVersionModelArrayList;
        this.source = source;
        if (source.equals(AllAppListFragment.class.getCanonicalName())) {
            updateListSize = 0;
            totalListSize = 0;
        }

        layoutInflater = LayoutInflater.from(context);
        upDateListItem();

        decimalFormat = new DecimalFormat("###0.#");
        if (usageStatsList == null) {
            usageStatsList = Utils.getUsageStatistics(context, UsageStatsManager.INTERVAL_DAILY);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == UPDATE_TYPE) {
            View view = layoutInflater.inflate(R.layout.item_all_myapp, parent, false);
            return new MyAllAppListHolder1(view);
        } else if (viewType == RECENT_UPDATE_TYPE) {
            View view = layoutInflater.inflate(R.layout.item_all_myapp, parent, false);
            return new MyAllAppListHolder2(view);
        } else if (viewType == INSTALLED_TYPE) {
            View view = layoutInflater.inflate(R.layout.item_all_myapp, parent, false);
            return new MyAllAppListHolder3(view);
        } else if (viewType == UPDATE_TYPE_TEXT) {
            View view = layoutInflater.inflate(R.layout.item_all_myapp_text, parent, false);
            return new MyAllAppListHolder4(view);
        } else if (viewType == RECENT_UPDATE_TYPE_TEXT) {
            View view = layoutInflater.inflate(R.layout.item_all_myapp_text, parent, false);
            return new MyAllAppListHolder5(view);
        } else {
            View view = layoutInflater.inflate(R.layout.item_all_myapp_text, parent, false);
            return new MyAllAppListHolder6(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemAllMyappBinding itemAllMyappBinding;
        ItemAllMyappTextBinding itemAllMyappTextBinding;

        if (holder instanceof MyAllAppListHolder1) {

            MyAllAppListHolder1 myAllAppListHolder = (MyAllAppListHolder1) holder;
            itemAllMyappBinding = myAllAppListHolder.getItemAllMyappBinding();
            CustomResolveInfo resolveInfo = updatesArrayList.get(position - 1);
            showAppItem(itemAllMyappBinding, resolveInfo, context.getString(R.string.update));

        } else if (holder instanceof MyAllAppListHolder2) {

            MyAllAppListHolder2 myAllAppListHolder = (MyAllAppListHolder2) holder;
            itemAllMyappBinding = myAllAppListHolder.getItemAllMyappBinding();
            CustomResolveInfo resolveInfo = recentlyUpdatedArrayList.get(position - updateListSize - 1);
            showAppItem(itemAllMyappBinding, resolveInfo, context.getString(R.string.open));


        } else if (holder instanceof MyAllAppListHolder3) {

            MyAllAppListHolder3 myAllAppListHolder = (MyAllAppListHolder3) holder;
            itemAllMyappBinding = myAllAppListHolder.getItemAllMyappBinding();
            ResolveInfo resolveInfo = resolveInfoInstalledArrayList.get(position - 1);
            if (source.equals(AllAppListFragment.class.getCanonicalName())) {
                AppUpdateInfo appUpdateInfo = checkAppVersion(resolveInfoInstalledArrayList.get(position - 1));
                if (appUpdateInfo.getUpdateType() == UPDATE_TYPE) {
                    showAppItem(itemAllMyappBinding, new CustomResolveInfo(resolveInfo, appUpdateInfo.getApplicationId(), appUpdateInfo.getAverageRating()), context.getString(R.string.update));
                } else {
                    showAppItem(itemAllMyappBinding, new CustomResolveInfo(resolveInfo, appUpdateInfo.getApplicationId(), appUpdateInfo.getAverageRating()), context.getString(R.string.open));
                }

            } else {
                //use later
                //showAppItem(itemAllMyappBinding, resolveInfo, context.getString(R.string.installed));
            }

        } else if (holder instanceof MyAllAppListHolder4) {

            MyAllAppListHolder4 myAllAppListHolder = (MyAllAppListHolder4) holder;
            itemAllMyappTextBinding = myAllAppListHolder.getItemAllMyappTextBinding();
            if (updatesArrayList.size() == 0) {
                itemAllMyappTextBinding.idTvListType.setText(context.getString(R.string.no_updates_available));

            } else {
                showTextItem(itemAllMyappTextBinding, context.getString(R.string.updates_pending), updatesArrayList.size());
            }


        } else if (holder instanceof MyAllAppListHolder5) {

            MyAllAppListHolder5 myAllAppListHolder = (MyAllAppListHolder5) holder;
            itemAllMyappTextBinding = myAllAppListHolder.getItemAllMyappTextBinding();
            showTextItem(itemAllMyappTextBinding, context.getString(R.string.recently_updated), recentlyUpdatedArrayList.size());

        } else {

            MyAllAppListHolder6 myAllAppListHolder = (MyAllAppListHolder6) holder;
            itemAllMyappTextBinding = myAllAppListHolder.getItemAllMyappTextBinding();
            showTextItem(itemAllMyappTextBinding, context.getString(R.string.all_apps), resolveInfoInstalledArrayList.size());

        }

    }

    @Override
    public int getItemCount() {
        return totalListSize;

    }

    @Override
    public int getItemViewType(int position) {
        //later we can remove hardcode value, its only for design
        if (position == 0 && source.equals(MyAllAppListFragment.class.getCanonicalName())) {
            return 4;
        } else if (position < updateListSize) {
            return 1;
        } else if (position == updateListSize && recentlyUpdatedArrayList != null && recentlyUpdatedArrayList.size() > 0) {
            return 5;
        } else if (position < recentlistsize) {
            return 2;
        } else if (position == updateListSize && resolveInfoInstalledArrayList.size() > 0) {
            return 6;
        } else {
            return 3;
        }
    }

    private void showAppItem(final ItemAllMyappBinding itemAllMyappBinding, final CustomResolveInfo resolveInfo, final String status) {


        itemAllMyappBinding.idAppIcon.setImageDrawable(context.getPackageManager().getApplicationIcon(resolveInfo.activityInfo.applicationInfo));
        final String appName = (String) ((resolveInfo != null) ? context.getPackageManager().getApplicationLabel(resolveInfo.activityInfo.applicationInfo) : "???");
        itemAllMyappBinding.idAppName.setText(appName);

        final String packageName = resolveInfo.activityInfo.applicationInfo.packageName.toString();
        itemAllMyappBinding.idPackageName.setText(packageName);

        File file = new File(resolveInfo.activityInfo.applicationInfo.publicSourceDir);
        long size = file.length();
        if (size / 1024 < 1024) {
            itemAllMyappBinding.idTvAppSize.setText(decimalFormat.format((float) size / 1024) + " " + context.getString(R.string.kb) + " ");
        } else {
            itemAllMyappBinding.idTvAppSize.setText(decimalFormat.format((float) size / (1024 * 1024)) + " " + context.getString(R.string.mb) + " ");
        }

        itemAllMyappBinding.idSearchappRating.setText(String.valueOf(resolveInfo.getAverageRating()));

        if (source.equals(AllAppListFragment.class.getCanonicalName())) {
            for (int i = 0; i < usageStatsList.size(); i++) {
                if (usageStatsList.get(i).getPackageName().equals(packageName)) {
                    String timeString = Utils.getTimeInterval(context, usageStatsList.get(i).getLastTimeUsed());
                    if (timeString.length() > 1) {
                        itemAllMyappBinding.idTvLastUpdateOpen.setText(" " + context.getString(R.string.used) + " " + timeString);
                    } else {
                        itemAllMyappBinding.idTvBullet.setVisibility(View.INVISIBLE);
                        itemAllMyappBinding.idTvLastUpdateOpen.setVisibility(View.INVISIBLE);
                    }
                    break;
                } else if (i == usageStatsList.size() - 1) {
                    itemAllMyappBinding.idTvBullet.setVisibility(View.INVISIBLE);
                    itemAllMyappBinding.idTvLastUpdateOpen.setVisibility(View.INVISIBLE);
                }
            }
        } else {

            try {
                int currentVersion = context.getPackageManager().getPackageInfo(resolveInfo.activityInfo.applicationInfo.packageName.toString(), 0).versionCode;
                for (int i = 0; i < myAppVersionModelArrayList.size(); i++) {
                    if (myAppVersionModelArrayList.get(i).getPackageName().equals(packageName)) {

                        long update_time = new File(resolveInfo.activityInfo.applicationInfo.sourceDir).lastModified();
                        String timeString = Utils.getTimeInterval(context, update_time);
                        if (timeString.length() > 1) {
                            itemAllMyappBinding.idTvLastUpdateOpen.setText(" " + context.getString(R.string.updated) + " " + timeString);
                        } else {
                            itemAllMyappBinding.idTvBullet.setVisibility(View.INVISIBLE);
                            itemAllMyappBinding.idTvLastUpdateOpen.setVisibility(View.INVISIBLE);
                        }
                    }
                }

            } catch (Exception e) {
                AndroidAppUtils.showLogE(source, e.toString());
            }
        }

        itemAllMyappBinding.idTvStatus.setText(status);

        Long downloadAppInfo[] = DbHelper.getInstance(context).getDownloadingFileInfo(resolveInfo.getAppId());
        AndroidAppUtils.showLogD("My applist adapter", downloadAppInfo[0] + " " + downloadAppInfo[1]);
        if (downloadAppInfo[0] != -1) {
            if(DbHelper.getInstance(context).getDownloadingFileApkDownloaded(context, downloadAppInfo[0]) == AppConstants.APP_INSTALLING) {

                itemAllMyappBinding.idTvDownload.setText(context.getString(R.string.installing___));
                itemAllMyappBinding.idTvDownloadProgress.setVisibility(View.GONE);
                itemAllMyappBinding.idLlDownloading.setVisibility(View.VISIBLE);
                itemAllMyappBinding.idLlSizeTime.setVisibility(View.GONE);
                itemAllMyappBinding.idRlStatus.setVisibility(View.GONE);
            } else {
                setDownloadLayout(itemAllMyappBinding);
                FileDownloadManager.FileDownloadListener fileDownloadListener = new FileDownloadManager.FileDownloadListener() {
                    @Override
                    public void onFileDownloading(int status, int downloadProgress, int byteDownloaded, int byteTotal, long appId) {

                        updateDownloadLayout(resolveInfo.getAppId(), itemAllMyappBinding, status, downloadProgress, byteDownloaded, byteTotal, appId);

                    }
                };
                fileDownloadListenerarray.add(fileDownloadListener);
                if(downloadAppInfo[1] != -1) {
                FileDownloadManager fileDownloadManager = new FileDownloadManager(context, downloadAppInfo[0], downloadAppInfo[1]);
                    fileDownloadManager.setFileDownloadListener(fileDownloadListener, downloadAppInfo[0]);

                    FileDownloadManager.downloading = true;
                    fileDownloadManager.getFileDownloadStatus(DbHelper.getInstance(context).getDownloadingFileName(context));
                }
            }
        }

        // For open app detail Page
        itemAllMyappBinding.idLlBaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString(context.getResources().getString(R.string.appId), String.valueOf(resolveInfo.getAppId()));
                ((MainActivity) context).replaceFragment(productDetailFragment, bundle, Utils.TAG_PRODUCT_DETAIL_FRAGMENT, true);
            }
        });

        // For open app
        itemAllMyappBinding.idRlStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (status.equals(context.getString(R.string.open))) {

                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                    context.startActivity(intent);
                } else {
                    if (itemAllMyappBinding.idLlSizeTime.getVisibility() == View.VISIBLE) {

                        updateAppItem(itemAllMyappBinding, resolveInfo.getAppId(), appName, packageName);
                    } else {
                        itemAllMyappBinding.idLlSizeTime.setVisibility(View.VISIBLE);
                        itemAllMyappBinding.idLlDownloading.setVisibility(View.GONE);
                        itemAllMyappBinding.idTvStatus.setVisibility(View.VISIBLE);
                        itemAllMyappBinding.idFtvCancel.setVisibility(View.GONE);

                        long appId = resolveInfo.getAppId();

                        Long downloadAppInfo[] = DbHelper.getInstance(context).getDownloadingFileInfo(appId);

                        DbHelper.getInstance(context).deleteRow(appId);

                            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                            try {
                                if(downloadAppInfo[1] != -1) {
                                    downloadManager.remove(downloadAppInfo[1]);
                                }
                                FileDownloadManager.mFileDownloadListener = null;
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                            if(fileDownloadListenerarray.size() > 0) {
                                fileDownloadListenerarray.remove(0);
                            }
                            downloadAppInfo = DbHelper.getInstance(context).getDownloadingFileInfo();
                            if (downloadAppInfo[0] != -1 && downloadAppInfo[0] != DbHelper.getInstance(context).getDownloadingFileInfo(appId)[0]) {
                                String appTitle = DbHelper.getInstance(context).getDownloadingFileName(context);
                                String packageName = DbHelper.getInstance(context).getDownloadingFilePackageName(context);
                                FileDownloadManager.FileDownloadListener fileDownloadListener = null;
                                    if(fileDownloadListenerarray.size() > 0) {
                                        fileDownloadListener = fileDownloadListenerarray.get(0);
                                }
                                FileDownloadManager.FileDownloadInfo fileDownloadInfo = new FileDownloadManager.FileDownloadInfo(context, fileDownloadListener, String.valueOf(downloadAppInfo[0]), appTitle, packageName);
                                fileDownloadInfo.startAppDownload(false);
                            }

                    }

                }
            }
        });
    }

    /**
     * set Download Layout for download app item
     * @param itemAllMyappBinding
     */
    private void setDownloadLayout(final ItemAllMyappBinding itemAllMyappBinding) {
        itemAllMyappBinding.idLlSizeTime.setVisibility(View.GONE);
        itemAllMyappBinding.idLlDownloading.setVisibility(View.VISIBLE);
        itemAllMyappBinding.idTvStatus.setVisibility(View.GONE);
        itemAllMyappBinding.idFtvCancel.setVisibility(View.VISIBLE);
    }

    private void updateDownloadLayout(final long applicationId, ItemAllMyappBinding itemAllMyappBinding, int status, int downloadProgress, int byteDownloaded, int byteTotal, long appId) {
        //use later
        if(applicationId == appId) {
            if (status == DownloadManager.STATUS_SUCCESSFUL) {

                if(fileDownloadListenerarray != null && fileDownloadListenerarray.size() > 0) {
                    fileDownloadListenerarray.remove(0);
                }
                ((MainActivity)context).replaceFragment(new MyAppFragment(), null, Utils.TAG_MY_APP_FRAGMENT, false);

                //remove later
//                            downloadLayout.setVisibility(View.GONE);
//                            installingTxt.setVisibility(View.GONE);
//                            rlBtnLayout.setVisibility(View.VISIBLE);
//                            lluobtnLayout.setVisibility(View.VISIBLE);
//                            installTxt.setVisibility(View.GONE);
//                            if (appAdapter != null) {
//                                appAdapter.notifyDataSetChanged();
//                            }
            } else if (status == AppConstants.APP_INSTALLING) {
                itemAllMyappBinding.idTvDownload.setText(context.getString(R.string.installing___));
                itemAllMyappBinding.idTvDownloadProgress.setVisibility(View.GONE);

            } else if (status == DownloadManager.STATUS_FAILED) {
                itemAllMyappBinding.idTvDownload.setText(context.getString(R.string.waiting_for_network));
                itemAllMyappBinding.idTvDownloadProgress.setText(context.getString(R.string.blank_space));
            } else {
                try {

                    if(downloadProgress < 0) {
                        itemAllMyappBinding.idTvDownloadProgress.setText("");
                        itemAllMyappBinding.idTvDownload.setText(context.getString(R.string.downloading));
                    } else {
                        itemAllMyappBinding.idTvDownloadProgress.setText(downloadProgress + "%");

                        float filesizeInMB = (float) byteTotal / (1024 * 1024);

                        float downloadSizeInMB = 0.0f;

                        if (byteDownloaded < (1024 * 1000)) {
                            downloadSizeInMB = (float) byteDownloaded / 1024;
                            itemAllMyappBinding.idTvDownload.setText(decimalFormat.format(Math.abs(downloadSizeInMB)) + " " + context.getString(R.string.kb) + "/ " + decimalFormat.format(Math.abs(filesizeInMB)) + " " + context.getString(R.string.mb));
                        } else {
                            downloadSizeInMB = (float) byteDownloaded / (1024 * 1024);
                            itemAllMyappBinding.idTvDownload.setText(decimalFormat.format(Math.abs(downloadSizeInMB)) + " " + context.getString(R.string.mb) + "/ " + decimalFormat.format(Math.abs(filesizeInMB)) + " " + context.getString(R.string.mb));
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    /**
     * update app item
     * @param itemAllMyappBinding
     */
    private void updateAppItem(final ItemAllMyappBinding itemAllMyappBinding, final long applicationId, String appName, String packageName) {

            setDownloadLayout(itemAllMyappBinding);

            // remove later
            //AppDetailResponse.Application application = result.getAppDetail().getApplication();
        FileDownloadManager.FileDownloadListener fileDownloadListener = new FileDownloadManager.FileDownloadListener() {

            @Override
            public void onFileDownloading(int status, int downloadProgress, int byteDownloaded, int byteTotal, long appId) {

                updateDownloadLayout(applicationId, itemAllMyappBinding, status, downloadProgress, byteDownloaded, byteTotal, appId);
            }
        };

        fileDownloadListenerarray.add(fileDownloadListener);
            FileDownloadManager.FileDownloadInfo fileDownloadInfo = new FileDownloadManager.FileDownloadInfo(context, fileDownloadListener, String .valueOf(applicationId), appName, packageName);

            fileDownloadInfo.startAppDownload(false);
    }

    public void upDateListItem() {
        if (updatesArrayList != null && updatesArrayList.size() > 0) {
            updateListSize = updateListSize + updatesArrayList.size();
            totalListSize = totalListSize + updatesArrayList.size();
        }
        if (recentlyUpdatedArrayList != null && recentlyUpdatedArrayList.size() > 0) {
            recentlistsize = updateListSize + recentlyUpdatedArrayList.size() + 1;
            totalListSize++;
            totalListSize = totalListSize + recentlyUpdatedArrayList.size();
        }

        if (resolveInfoInstalledArrayList != null && resolveInfoInstalledArrayList.size() > 0) {
            totalListSize = totalListSize + resolveInfoInstalledArrayList.size() + 1;
        }
    }

    private void showTextItem(ItemAllMyappTextBinding itemAllMyappTextBinding, String listType, int listSize) {

        itemAllMyappTextBinding.idTvListType.setText(listType);
        itemAllMyappTextBinding.idTvNoOfApps.setText(listSize + " " + context.getString(R.string.apps));

    }

    /**
     * method return last update time
     *
     * @param resolveInfo
     * @return
     */
    private String getLastUpdateTime(CustomResolveInfo resolveInfo) {
        String updateTime = "";

        try {
            String packageName = resolveInfo.activityInfo.applicationInfo.packageName.toString();
            int currentVersion = context.getPackageManager().getPackageInfo(resolveInfo.activityInfo.applicationInfo.packageName.toString(), 0).versionCode;
            for (int i = 0; i < myAppVersionModelArrayList.size(); i++) {
                if (myAppVersionModelArrayList.get(i).getPackageName().equals(packageName)) {

                    long installed = new File(resolveInfo.activityInfo.applicationInfo.sourceDir).lastModified();
                    long milisecondsDifference = System.currentTimeMillis() - installed;
                    if (TimeUnit.MILLISECONDS.toDays(milisecondsDifference) > 1) {
                        updateTime = context.getString(R.string.updated) + " " + TimeUnit.MILLISECONDS.toDays(milisecondsDifference) + " "
                                + context.getString(R.string.days) + " " + context.getString(R.string.ago);
                    } else if (TimeUnit.MILLISECONDS.toDays(milisecondsDifference) == 1) {
                        updateTime = context.getString(R.string.updated) + " " + context.getString(R.string.yesterday);
                    } else if (TimeUnit.MILLISECONDS.toHours(milisecondsDifference) < 24 && TimeUnit.MILLISECONDS.toHours(milisecondsDifference) > 0) {
                        updateTime = context.getString(R.string.updated) + " " + TimeUnit.MILLISECONDS.toHours(milisecondsDifference) + " "
                                + context.getString(R.string.hr) + " " + context.getString(R.string.ago);
                    } else if (TimeUnit.MILLISECONDS.toMillis(milisecondsDifference) < 60) {
                        updateTime = context.getString(R.string.updated) + " " + TimeUnit.MILLISECONDS.toMinutes(milisecondsDifference) + " "
                                + context.getString(R.string.min) + " " + context.getString(R.string.ago);
                    }
                    return updateTime;

                }
            }

        } catch (Exception e) {
            AndroidAppUtils.showLogE(source, e.toString());
            return updateTime;
        }
        return updateTime;
    }

    /**
     * return long array containing value for app update and appId
     *
     * @param resolveInfo
     */
    private AppUpdateInfo checkAppVersion(ResolveInfo resolveInfo) {

        AppUpdateInfo appUpdateInfo = new AppUpdateInfo();
        try {

            String packageName = resolveInfo.activityInfo.applicationInfo.packageName.toString();
            int currentVersion = context.getPackageManager().getPackageInfo(resolveInfo.activityInfo.applicationInfo.packageName.toString(), 0).versionCode;
            for (int i = 0; i < myAppVersionModelArrayList.size(); i++) {
                if (myAppVersionModelArrayList.get(i).getPackageName().equals(packageName)) {
                    if (currentVersion == Integer.parseInt(myAppVersionModelArrayList.get(i).getLatestVersionNo())) {
                        appUpdateInfo.setApplicationId(Long.parseLong(myAppVersionModelArrayList.get(i).getApplicationId()));
                        appUpdateInfo.setAverageRating(myAppVersionModelArrayList.get(i).getAverageRating());
                        return appUpdateInfo;

                    } else {
                        appUpdateInfo.setUpdateType((long) UPDATE_TYPE);
                        appUpdateInfo.setApplicationId(Long.parseLong(myAppVersionModelArrayList.get(i).getApplicationId()));
                        appUpdateInfo.setAverageRating(myAppVersionModelArrayList.get(i).getAverageRating());
                        return appUpdateInfo;
                    }

                }
            }
            return appUpdateInfo;
        } catch (Exception e) {
            AndroidAppUtils.showLogE(source, e.toString());
            return appUpdateInfo;
        }

    }

    public class MyAllAppListHolder1 extends RecyclerView.ViewHolder {

        private ItemAllMyappBinding itemAllMyappBinding;

        public MyAllAppListHolder1(View itemView) {
            super(itemView);
            itemAllMyappBinding = DataBindingUtil.bind(itemView);
        }

        public ItemAllMyappBinding getItemAllMyappBinding() {
            return itemAllMyappBinding;
        }
    }

    public class MyAllAppListHolder2 extends RecyclerView.ViewHolder {

        private ItemAllMyappBinding itemAllMyappBinding;

        public MyAllAppListHolder2(View itemView) {
            super(itemView);
            itemAllMyappBinding = DataBindingUtil.bind(itemView);
        }

        public ItemAllMyappBinding getItemAllMyappBinding() {
            return itemAllMyappBinding;
        }
    }

    public class MyAllAppListHolder3 extends RecyclerView.ViewHolder {

        private ItemAllMyappBinding itemAllMyappBinding;

        public MyAllAppListHolder3(View itemView) {
            super(itemView);
            itemAllMyappBinding = DataBindingUtil.bind(itemView);
        }

        public ItemAllMyappBinding getItemAllMyappBinding() {
            return itemAllMyappBinding;
        }
    }

    public class MyAllAppListHolder4 extends RecyclerView.ViewHolder {

        private ItemAllMyappTextBinding itemAllMyappTextBinding;

        public MyAllAppListHolder4(View itemView) {
            super(itemView);
            itemAllMyappTextBinding = DataBindingUtil.bind(itemView);
        }

        public ItemAllMyappTextBinding getItemAllMyappTextBinding() {
            return itemAllMyappTextBinding;
        }
    }

    public class MyAllAppListHolder5 extends RecyclerView.ViewHolder {

        private ItemAllMyappTextBinding itemAllMyappTextBinding;

        public MyAllAppListHolder5(View itemView) {
            super(itemView);
            itemAllMyappTextBinding = DataBindingUtil.bind(itemView);
        }

        public ItemAllMyappTextBinding getItemAllMyappTextBinding() {
            return itemAllMyappTextBinding;
        }
    }

    public class MyAllAppListHolder6 extends RecyclerView.ViewHolder {

        private ItemAllMyappTextBinding itemAllMyappTextBinding;

        public MyAllAppListHolder6(View itemView) {
            super(itemView);
            itemAllMyappTextBinding = DataBindingUtil.bind(itemView);
        }

        public ItemAllMyappTextBinding getItemAllMyappTextBinding() {
            return itemAllMyappTextBinding;
        }
    }
}
