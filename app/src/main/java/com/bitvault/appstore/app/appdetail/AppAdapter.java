package com.bitvault.appstore.app.appdetail;

import android.app.Activity;
import android.content.Intent;
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
import com.bitvault.appstore.app.dashboard.MainActivity;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.custom.FontTextView;
import com.bitvault.appstore.utils.OnLoadMoreListener;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.response.SearchResponse;

import java.util.List;

/*
 * Created Dheeraj Bansal root on 2/5/17.
 * version 1.0.0
 * Application Grid adapter
 */


public class AppAdapter extends RecyclerView.Adapter {

    private final static int VIEW_ITEM = 1;
    private final static int VIEW_PROG = 0;
    private final Activity activity;
    private List<SearchResponse.appList> mAppLists;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold =3;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public AppAdapter(Activity activity, List<SearchResponse.appList> mAppLists, RecyclerView recyclerView) {
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
                    R.layout.item_app, parent, false);
            vh = new AppViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_progress_horizontal, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        SearchResponse.appList item = mAppLists.get(listPosition);
        if (item != null && holder instanceof AppViewHolder) {
            ((AppViewHolder) holder).setIcon(item.getAppIconUrl());
            ((AppViewHolder) holder).setAppName(item.getAppName());
            ((AppViewHolder) holder).setAppRating(Double.parseDouble(item.getAverageRating()));
            ((AppViewHolder) holder).setAppPrice(Integer.parseInt(item.getAppPrice()));
            ((AppViewHolder) holder).mAppThreeDot.setTag(listPosition);
            ((AppViewHolder) holder).mAppLayout.setTag(listPosition);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(false);
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
            this.mAppIcon = (NetworkImageView) itemView.findViewById(R.id.id_app_icon);
            this.mAppName = (TextView) itemView.findViewById(R.id.id_app_name);
            this.mAppRating = (TextView) itemView.findViewById(R.id.id_app_rating);
            this.mAppPrice = (TextView) itemView.findViewById(R.id.id_app_free);
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
         * set app ratings
         *
         * @param rating
         */
        private void setAppRating(double rating) {
            mAppRating.setText(String.format("%s", rating));
        }

        /**
         * setapp price if app is paid otherwise free
         *
         * @param price
         */
        private void setAppPrice(int price) {
            if (price == 0) {
                mAppPrice.setText(activity.getString(R.string.free));
                mPriceSymbol.setVisibility(View.GONE);
            } else {
                mAppPrice.setText("" + price);
                mPriceSymbol.setVisibility(View.VISIBLE);
            }
        }

        /**
         * set app icon
         *
         * @param url
         */
        private void setIcon(String url) {
            if (url != null) {
                mAppIcon.setDefaultImageResId(R.drawable.default_icon);
                mAppIcon.setErrorImageResId(R.drawable.default_icon);
                //use later
                //mAppIcon.setImageUrl(url, AppStoreApplication.getInstance().getImageLoader());
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.id_three_dot:
                    PopupMenu popup = new PopupMenu(activity, view);
                    popup.getMenuInflater().inflate(R.menu.popup_install, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            return true;
                        }
                    });
                    popup.show();
                    break;
                case R.id.id_app_linear_lout:
                    int position= (int) view.getTag();

                    ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(activity.getResources().getString(R.string.package_name), mAppLists.get(position).getPackageName());

                    ((MainActivity)activity).replaceFragment(productDetailFragment, bundle, Utils.TAG_PRODUCT_DETAIL_FRAGMENT, true);
                    break;
            }
        }
    }
}
