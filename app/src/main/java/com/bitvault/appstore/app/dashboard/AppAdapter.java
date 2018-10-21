package com.bitvault.appstore.app.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.appdetail.FileDownloadManager;
import com.bitvault.appstore.app.appdetail.ProductDetailFragment;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.custom.FontTextView;
import com.bitvault.appstore.database.DbHelper;
import com.bitvault.appstore.dialog.PaymentDialog;
import com.bitvault.appstore.receivers.UninstallReceiver;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.AppStoreUtils;
import com.bitvault.appstore.utils.OnLoadMoreListener;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.response.AppList;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created Dheeraj Bansal root on 2/5/17.
 * version 1.0.0
 */

/**
 * app grid adapter for homepage
 */
public class AppAdapter extends RecyclerView.Adapter implements FileDownloadManager.FileDownloadListener, UninstallReceiver.UninstallRecieverListener {

    private final static int VIEW_ITEM = 1;
    private final static int VIEW_PROG = 0;
    private final String TAG = this.getClass().getCanonicalName();
    private final Activity activity;
    private List<AppList> mAppLists;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 1;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private ArrayList<String> installingPackage;
    private DecimalFormat decimalFormat;

    public AppAdapter(Activity activity, List<AppList> mAppLists, RecyclerView recyclerView) {
        this.mAppLists = mAppLists;
        this.activity = activity;
        decimalFormat = new DecimalFormat("##0.##");

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();
                if (!loading
                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something

                    if (onLoadMoreListener != null && !recyclerView.isComputingLayout()) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });
    }

    /**
     * method return loading status
     *
     * @return
     */
    public boolean isLoading() {
        return loading;
    }

    /**
     * set loading
     */
    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_app, parent, false);
            vh = new AppAdapter.AppViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_progress_horizontal, parent, false);

            vh = new AppAdapter.ProgressViewHolder(v);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        AppList item = mAppLists.get(listPosition);

        if (holder instanceof AppViewHolder) {
            AppViewHolder appViewHolder = (AppViewHolder) holder;
            appViewHolder.setIcon(item.getAppIconUrl());
            appViewHolder.setAppName(item.getAppName());
            Long downloadAppInfo[] = DbHelper.getInstance(activity).getDownloadingFileInfo(Long.parseLong(item.getApplicationId()));
            if (downloadAppInfo[0] != -1) {
                appViewHolder.mtvInstalling.setVisibility(View.VISIBLE);
                appViewHolder.mllRating.setVisibility(View.INVISIBLE);
            } else {
                appViewHolder.mtvInstalling.setVisibility(View.GONE);
                appViewHolder.mllRating.setVisibility(View.VISIBLE);
                appViewHolder.setAppRating(item.getAverageRating());
                appViewHolder.setAppPrice(item.getAppPrice(), item.getPackageName());
            }
            appViewHolder.mAppThreeDot.setTag(listPosition);
            appViewHolder.mAppLayout.setTag(listPosition);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(false);
        }

    }

    /**
     * add loader in list
     *
     * @param item
     */
    public void addItem(AppList item) {
        mAppLists.add(item);
        notifyItemChanged(mAppLists.size() - 1);
    }

    /**
     * remove loader in list
     *
     * @param position
     */
    public void removeItem(int position) {
        if (position > 0) {
            mAppLists.remove(position);
            notifyItemChanged(mAppLists.size() - 1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mAppLists.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return mAppLists.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onFileDownloading(int status, int downloadProgress, int byteDownloaded, int byteTotal, long appId) {

        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_RUNNING) {
            notifyDataSetChanged();
        }

    }

    @Override
    public void onAppUninstall() {
        notifyDataSetChanged();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private NetworkImageView mAppIcon;
        private TextView mAppName;
        private LinearLayout mllRating;
        private TextView mAppRating, mtvInstalling;
        private TextView mAppPrice;
        private TextView mAppThreeDot;
        private FontTextView mPriceSymbol;
        private LinearLayout mAppLayout;


        public AppViewHolder(View itemView) {
            super(itemView);
            this.mAppIcon = (NetworkImageView) itemView.findViewById(R.id.id_app_icon);
            this.mAppName = (TextView) itemView.findViewById(R.id.id_app_name);
            this.mllRating = (LinearLayout) itemView.findViewById(R.id.id_ll_rating);
            this.mAppRating = (TextView) itemView.findViewById(R.id.id_app_rating);
            this.mAppPrice = (TextView) itemView.findViewById(R.id.id_app_free);
            this.mtvInstalling = (TextView) itemView.findViewById(R.id.id_tv_installing);
            this.mAppThreeDot = (TextView) itemView.findViewById(R.id.id_three_dot);
            this.mPriceSymbol = (FontTextView) itemView.findViewById(R.id.id_price_symbol);
            this.mAppLayout = (LinearLayout) itemView.findViewById(R.id.id_app_linear_lout);

            this.mAppLayout.setOnClickListener(this);
            this.mAppThreeDot.setOnClickListener(this);
            this.mAppIcon.setErrorImageResId(R.drawable.default_icon);
        }

        /**
         * set app name
         *
         * @param name
         */
        private void setAppName(String name) {
            if (name != null) {
                mAppName.setText(name);
            }
        }

        /**
         * set RAting of App
         *
         * @param rating
         */
        private void setAppRating(double rating) {
            mAppRating.setText(String.format("%s", decimalFormat.format(rating)));
        }

        /**
         * set App price
         *
         * @param price
         */
        private void setAppPrice(double price, String packageName) {
            if (Utils.isPackageInstalled(packageName, activity)) {
                mAppPrice.setVisibility(View.GONE);
                mPriceSymbol.setText(activity.getString(R.string.store_icon));
                mPriceSymbol.setVisibility(View.VISIBLE);
            } else {
                if (price == 0) {
                    mAppPrice.setText(activity.getResources().getString(R.string.free));
                    mAppPrice.setVisibility(View.VISIBLE);
                    mPriceSymbol.setVisibility(View.GONE);
                } else {
                    mAppPrice.setText("" + price);
                    mAppPrice.setVisibility(View.VISIBLE);
                    if (activity != null) {
                        mPriceSymbol.setText(activity.getString(R.string.currency_icon) + " ");
                    }
                    mPriceSymbol.setVisibility(View.VISIBLE);
                }
            }
        }

        /**
         * set App icon
         *
         * @param url
         */
        private void setIcon(String url) {
            if (url != null) {
                mAppIcon.setDefaultImageResId(R.drawable.default_icon);
                mAppIcon.setErrorImageResId(R.drawable.default_icon);
                mAppIcon.setImageUrl(url, AppStoreApplication.getInstance().getImageLoader());
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.id_three_dot:
                    final int position = (int) view.getTag();
                    PopupMenu popup = new PopupMenu(activity, view);
                    if (Utils.isPackageInstalled(mAppLists.get(position).getPackageName(), activity)
                            && Utils.getVersionNumber(mAppLists.get(position).getPackageName(), activity) != -1
                            && Utils.getVersionNumber(mAppLists.get(position).getPackageName(), activity)
                            == Integer.parseInt(mAppLists.get(position).getLatestVersionNo())) {

                        popup.getMenuInflater().inflate(R.menu.popup_uninstall, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                onUnInstallClicked(mAppLists.get(position).getPackageName());
                                return true;
                            }
                        });
                    } else if (DbHelper.getInstance(activity).getDownloadingFileInfo(Long.parseLong(mAppLists.get(position).getApplicationId()))[0] != -1) {
                        if (!Utils.isPackageInstalled(mAppLists.get(position).getPackageName(), activity)) {
                            popup.getMenuInflater().inflate(R.menu.popup_installing, popup.getMenu());
                        } else {
                            popup.getMenuInflater().inflate(R.menu.popup_updating, popup.getMenu());
                        }
                    } else {
                        if (!Utils.isPackageInstalled(mAppLists.get(position).getPackageName(), activity)) {
                            popup.getMenuInflater().inflate(R.menu.popup_install, popup.getMenu());
                        } else {
                            popup.getMenuInflater().inflate(R.menu.popup_update, popup.getMenu());
                        }
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                    Nammu.askForPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionCallback() {
                                        @Override
                                        public void permissionGranted() {
                                            onInstallClicked(position);
                                        }

                                        @Override
                                        public void permissionRefused() {
                                            AndroidAppUtils.showToast(activity, activity.getResources().getString(R.string.text_permisson_refused));
                                        }
                                    });
                                } else {
                                    onInstallClicked(position);
                                }
                                return true;
                            }
                        });
                    }
                    popup.show();

                    break;
                case R.id.id_app_linear_lout:
                    int position1 = (int) view.getTag();

                    Bundle bundle = new Bundle();
                    bundle.putString(activity.getResources().getString(R.string.appId), mAppLists.get(position1).getApplicationId());

                    ((MainActivity) activity).replaceFragment(new ProductDetailFragment(), bundle, Utils.TAG_PRODUCT_DETAIL_FRAGMENT, true);
                    break;
            }
        }

        /**
         * method for install application
         *
         * @param position
         */
        private void onInstallClicked(final int position) {

            if (activity != null) {
                if (mAppLists.get(position).getAppPrice() > 0 && mAppLists.get(position).getPaymentStatus() != null
                        && !mAppLists.get(position).getPaymentStatus().equals(activity.getString(R.string.payment_done))) {
                    PaymentDialog paymentDialog = new PaymentDialog();
                    long appId = Long.parseLong((mAppLists.get(position).getApplicationId()));


                    Bundle bundle = new Bundle();
                    bundle.putDouble(activity.getString(R.string.amount), new Double(mAppLists.get(position).getAppPrice()));
                    bundle.putLong(activity.getString(R.string.appId), appId);
                    bundle.putString(activity.getString(R.string.file_name), mAppLists.get(position).getAppName());
                    bundle.putString(activity.getString(R.string.package_name), mAppLists.get(position).getPackageName());
                    bundle.putString(activity.getString(R.string.app_icon), mAppLists.get(position).getAppIconUrl());
                    paymentDialog.setArguments(bundle);
                    paymentDialog.setDismissListener(new PaymentDialog.DismissListener() {
                        @Override
                        public void onDismissListener(DialogInterface dialog, boolean value) {
                            if (value) {
                                if (mAppLists != null) {
                                    AppList appList = mAppLists.get(position);
                                    if (appList != null) {
                                        appList.setPaymentStatus(activity.getString(R.string.payment_done));
                                    }
                                }
                                notifyDataSetChanged();
                            }

                        }

                    });

                    paymentDialog.setFileDownloadListener(AppAdapter.this);
                    paymentDialog.show(((MainActivity) activity).getSupportFragmentManager(), Utils.TAG_PAYMENT_DIALOG);
                } else {

                    FileDownloadManager.FileDownloadInfo fileDownloadInfo = new FileDownloadManager.FileDownloadInfo(activity, AppAdapter.this, mAppLists.get(position).getApplicationId(), mAppLists.get(position).getAppName(),
                            mAppLists.get(position).getPackageName());
                    fileDownloadInfo.startFileDownloading1(mAppLists.get(position).getAppIconUrl(), mAppLists.get(position).getAppName(), mAppLists.get(position).getApplicationId());
                    AppStoreUtils.sendDownloadInfo(activity, mAppLists.get(position).getPackageName());

                }
            }

        }

        /**
         * method for uninstall application
         *
         * @param packageName
         */
        private void onUnInstallClicked(String packageName) {
            new UninstallReceiver().setOnUnstallReceiverListener(AppAdapter.this);
            Uri packageURI = Uri.parse(activity.getString(R.string.package_colon) + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            activity.startActivity(uninstallIntent);
        }
    }


}
