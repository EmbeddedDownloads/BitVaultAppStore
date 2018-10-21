package com.bitvault.appstore.app.searchapp;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.bitvault.appstore.app.appdetail.CategoryAppAdapter;
import com.bitvault.appstore.app.appdetail.FileDownloadManager;
import com.bitvault.appstore.app.appdetail.ProductDetailFragment;
import com.bitvault.appstore.app.dashboard.MainActivity;
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
import com.bitvault.appstore.webservice.response.SearchResponse;

import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/*
 * Created Dheeraj Bansal root on 4/5/17.
 * version 1.0.0
 */

/**
 * application list on search keyword
 */
public class SearchAppAdapter extends RecyclerView.Adapter implements FileDownloadManager.FileDownloadListener, UninstallReceiver.UninstallRecieverListener {

    private String TAG = SearchAppAdapter.class.getCanonicalName();
    private final static int VIEW_ITEM = 1;
    private final static int VIEW_PROG = 0;
    private final Activity activity;
    private List<AppList> mAppLists;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    //later remove
    private int lazyLoadingcount;

    public SearchAppAdapter(Activity activity, List<AppList> mAppLists, RecyclerView recyclerView) {
        this.mAppLists = mAppLists;
        this.activity = activity;

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
                        && totalItemCount <= (lastVisibleItem + visibleThreshold)  && lazyLoadingcount < 3 && totalItemCount >  10) {
                    // End has been reached
                    // Do something

                    loading = true;
                    if (onLoadMoreListener != null && !recyclerView.isComputingLayout()) {
                        onLoadMoreListener.onLoadMore();
                    }
                    lazyLoadingcount++;

                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mAppLists.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_search_app, parent, false);
            vh = new SearchAppAdapter.AppViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_progress_vertical, parent, false);

            vh = new SearchAppAdapter.ProgressViewHolder(v);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        AppList item = mAppLists.get(listPosition);

        if (holder instanceof AppViewHolder) {
            ((AppViewHolder) holder).setIcon(item.getAppIconUrl());
            ((AppViewHolder) holder).setAppName(item.getAppName());
            Long downloadAppInfo[] = DbHelper.getInstance(activity).getDownloadingFileInfo(Long.parseLong(item.getApplicationId()));
            if (downloadAppInfo[0] != -1) {
                ((AppViewHolder) holder).mAppPrice.setText(activity.getResources().getString(R.string.installing___));
                ((AppViewHolder) holder).mPriceSymbol.setVisibility(View.GONE);
            } else {
                try {
                    ((AppViewHolder) holder).setAppPrice(item.getAppPrice(), item.getPackageName());
                } catch (NumberFormatException e) {
                    AndroidAppUtils.showLogD(TAG, e.getMessage());
                }
            }
            ((AppViewHolder) holder).setAppRating(item.getAverageRating());
            ((AppViewHolder) holder).mAppThreeDot.setTag(listPosition);
            ((AppViewHolder) holder).mAppLayout.setTag(listPosition);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(false);
        }

    }

    @Override
    public int getItemCount() {
        return mAppLists.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onAppUninstall() {
        notifyDataSetChanged();
    }

    @Override
    public void onFileDownloading(int status, int downloadProgress, int byteDownloaded, int byteTotal, long appId) {
        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_RUNNING) {
            notifyDataSetChanged();
        }
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
        private TextView mAppRating;
        private TextView mAppPrice;
        private TextView mAppThreeDot;
        private FontTextView mPriceSymbol;
        private LinearLayout mAppLayout;


        public AppViewHolder(View itemView) {
            super(itemView);
            this.mAppIcon = (NetworkImageView) itemView.findViewById(R.id.id_searchapp_icon);
            this.mAppName = (TextView) itemView.findViewById(R.id.id_searchapp_name);
            this.mAppRating = (TextView) itemView.findViewById(R.id.id_searchapp_rating);
            this.mAppPrice = (TextView) itemView.findViewById(R.id.id_searchapp_free);
            this.mAppThreeDot = (TextView) itemView.findViewById(R.id.id_searchthree_dot);
            this.mPriceSymbol = (FontTextView) itemView.findViewById(R.id.id_searchprice_symbol);
            this.mAppLayout = (LinearLayout) itemView.findViewById(R.id.id_searchlinear_lout);

            this.mAppLayout.setOnClickListener(this);
            this.mAppThreeDot.setOnClickListener(this);

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
         * set app rating
         *
         * @param rating
         */
        private void setAppRating(double rating) {
            mAppRating.setText(String.format("%s", rating));
        }

        /**
         * set app price for paid app
         * @param price
         * @param packageName
         */
        @SuppressLint("SetTextI18n")
        private void setAppPrice(double price, String packageName) {
            if (Utils.isPackageInstalled(packageName, activity)) {
                mAppPrice.setText(activity.getString(R.string.installed));
                mPriceSymbol.setText(activity.getString(R.string.store_icon));
                mPriceSymbol.setVisibility(View.VISIBLE);
            } else {
                if (price == 0) {
                    mAppPrice.setText(activity.getResources().getString(R.string.FREE));
                    mPriceSymbol.setVisibility(View.GONE);
                } else {
                    mAppPrice.setText("" + price);
                    if(activity != null) {
                        mPriceSymbol.setText(activity.getString(R.string.currency_icon) + " ");
                    }
                    mPriceSymbol.setVisibility(View.VISIBLE);
                }
            }
        }

        /**
         * set app icon url
         *
         * @param url
         */
        private void setIcon(String url) {
            if (url != null) {
                mAppIcon.setImageUrl(url, AppStoreApplication.getInstance().getImageLoader());
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.id_searchthree_dot:
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
                        if(!Utils.isPackageInstalled(mAppLists.get(position).getPackageName(), activity)) {
                            popup.getMenuInflater().inflate(R.menu.popup_installing, popup.getMenu());
                        }else {
                            popup.getMenuInflater().inflate(R.menu.popup_updating, popup.getMenu());
                        }
                    } else {
                        if(!Utils.isPackageInstalled(mAppLists.get(position).getPackageName(), activity)) {
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
                case R.id.id_searchlinear_lout:
                    int positionLout = (int) view.getTag();

                    Bundle bundle = new Bundle();
                    bundle.putString(activity.getResources().getString(R.string.appId), mAppLists.get(positionLout).getApplicationId());
                    ((MainActivity)activity).replaceFragment(new ProductDetailFragment(), bundle, Utils.TAG_SEARCH_FRAGMENT, true);

                    break;
            }
        }

        /**
         * method for install application
         *
         * @param position
         */
        private void onInstallClicked(final int position) {
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
                            if(mAppLists != null) {
                                AppList appList = mAppLists.get(position);
                                if(appList != null) {
                                    appList.setPaymentStatus(activity.getString(R.string.payment_done));
                                }
                            }
                            notifyDataSetChanged();
                        }

                    }

                });

                paymentDialog.setFileDownloadListener(SearchAppAdapter.this);
                paymentDialog.show(((MainActivity) activity).getSupportFragmentManager(), Utils.TAG_PAYMENT_DIALOG);
            } else {
                FileDownloadManager.FileDownloadInfo fileDownloadInfo = new FileDownloadManager.FileDownloadInfo(activity, SearchAppAdapter.this, mAppLists.get(position).getApplicationId(), mAppLists.get(position).getAppName(),
                        mAppLists.get(position).getPackageName());
                fileDownloadInfo.startFileDownloading1(mAppLists.get(position).getAppIconUrl(), mAppLists.get(position).getAppName(), mAppLists.get(position).getApplicationId());
                AppStoreUtils.sendDownloadInfo(activity, mAppLists.get(position).getPackageName());
            }


        }

        /**
         * method for uninstall application
         *
         * @param packageName
         */
        private void onUnInstallClicked(String packageName) {
            new UninstallReceiver().setOnUnstallReceiverListener(SearchAppAdapter.this);
            Uri packageURI = Uri.parse(activity.getString(R.string.package_colon) + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            activity.startActivity(uninstallIntent);
        }
    }
}
