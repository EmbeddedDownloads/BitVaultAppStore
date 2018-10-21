package com.bitvault.appstore.app.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.appdetail.CategoryAppAdapter;
import com.bitvault.appstore.app.appdetail.PermissionCategory;
import com.bitvault.appstore.app.appdetail.ProductDetailFragment;
import com.bitvault.appstore.app.category.CategoryFragment;
import com.bitvault.appstore.app.setting.InternetConnectionFragment;
import com.bitvault.appstore.application.APIs;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.HorizontalDividerItemDecoration;
import com.bitvault.appstore.utils.OnLoadMoreListener;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.STRINGRequest;
import com.bitvault.appstore.webservice.response.CategoryResponse;
import com.bitvault.appstore.webservice.response.HomePageAppsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/*
 * Created Dheeraj Bansal root on 3/5/17.
 * version 1.0.0
 */

/**
 * show the header of category wise apps on Dashboard
 */
public class AppHeaderCategoryAdapter extends RecyclerView.Adapter<AppHeaderCategoryAdapter.AppHeaderViewHolder> {

    private final Activity activity;
    private List<HomePageAppsResponse.AppCategoryList> mAppHeaderList;
    private ArrayList<AppAdapter> appAdapterList;
    private ArrayList<Integer> adapterPageArrayList;
    private float X, Y, dX, dY;

    public AppHeaderCategoryAdapter(Activity activity, List<HomePageAppsResponse.AppCategoryList> mAppHeaderList, RecyclerView recyclerView) {
        this.mAppHeaderList = mAppHeaderList;
        this.activity = activity;

        appAdapterList = new ArrayList<>();
        adapterPageArrayList = new ArrayList<>();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    @Override
    public AppHeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_appcategory, parent, false);
        return new AppHeaderCategoryAdapter.AppHeaderViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final AppHeaderViewHolder holder, final int listPosition) {

        final HomePageAppsResponse.AppCategoryList appCategoryList = mAppHeaderList.get(listPosition);
        if (appCategoryList.getAppList().size() > 0) {
            final AppAdapter appAdapter = new AppAdapter(activity, appCategoryList.getAppList(), holder.mRecyclerViewApp);
            appAdapterList.add(appAdapter);
            adapterPageArrayList.add(1);
            holder.mRecyclerViewApp.setAdapter(appAdapter);

            appAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //later use for lazy loading
                    appAdapter.addItem(null);
                    int pageNo = adapterPageArrayList.get(listPosition);
                    adapterPageArrayList.add(listPosition, ++pageNo);
                    loadMoreApps(appCategoryList.getCategoryId(), adapterPageArrayList.get(listPosition), listPosition);
                }
            });

            if (mAppHeaderList.get(listPosition).getCategoryName() != null) {
                holder.textViewAppName.setText(mAppHeaderList.get(listPosition).getCategoryName());
            }

            if(appCategoryList.getAppList().size() < 5) {
                holder.idTvMore.setVisibility(View.INVISIBLE);
            } else {
                holder.relativeLayoutCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(activity.getString(R.string.header_title), appCategoryList.getCategoryName());
                        bundle.putString(activity.getString(R.string.categoryIdList), appCategoryList.getCategoryId());
                        ((MainActivity) activity).replaceFragment(new CategoryFragment(), bundle, Utils.TAG_CATEGORY_FRAGMENT, true);
                    }

                });
            }
        } else {
            holder.relativeLayoutCategory.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mAppHeaderList.size();
    }

    /**
     * load more app
     */
    private void loadMoreApps(String categoryid, int pageNo, int position) {
        String categoryId = "1";
        if (categoryid != null) {

            categoryId = categoryid;
        }
        new STRINGRequest().postRequest(activity, new LoadAppCategoryWise(), null, APIs.API_CATEGORY + activity.getString(R.string.categoryIdList) + "=" + categoryId + "&" + activity.getString(R.string.page) + "=" + pageNo + "&" + activity.getString(R.string.size) + "=10&"
                + activity.getString(R.string.publicAddress) + "=" + Utils.getPublicAddress(activity), Request.Method.GET, position);

    }

    /**
     * set list of apps based on category
     *
     * @param response
     */
    private void setCategoryApp(CategoryResponse response, int position) {

            appAdapterList.get(position).removeItem(mAppHeaderList.get(position).getAppList().size() - 1);

            if (response.getAppList() != null && response.getAppList().size() > 0) {
                mAppHeaderList.get(position).getAppList().addAll(response.getAppList());
                appAdapterList.get(position).setLoading(false);
            } else {
                appAdapterList.get(position).setLoading(true);
            }

            appAdapterList.get(position).notifyDataSetChanged();

    }

    /**
     * reset data of app list
     */
    public void notifyData() {
        for (int i = 0; i < appAdapterList.size(); i++) {
            appAdapterList.get(i).notifyDataSetChanged();
        }
    }

    public class AppHeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAppName;
        private RelativeLayout relativeLayoutCategory;
        private RecyclerView mRecyclerViewApp;
        private TextView idAppCategoryName, idTvMore;

        public AppHeaderViewHolder(View itemView) {
            super(itemView);
            this.textViewAppName = (TextView) itemView.findViewById(R.id.id_app_category_name);
            this.relativeLayoutCategory = (RelativeLayout) itemView.findViewById(R.id.id_category_rlout);
            this.mRecyclerViewApp = (RecyclerView) itemView.findViewById(R.id.id_home_app_recycler);
            idAppCategoryName = (TextView) itemView.findViewById(R.id.id_app_category_name);
            idTvMore = (TextView) itemView.findViewById(R.id.id_tv_more);
            this.mRecyclerViewApp.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            this.mRecyclerViewApp.addItemDecoration(new HorizontalDividerItemDecoration(
                    (int) activity.getResources().getDimension(R.dimen.dimen5)));
        }
    }

    private class LoadAppCategoryWise implements STRINGRequest.StringResponseListener {

        @Override
        public void onResponseSuccess(String response, int requestCode) {

            if(activity  != null) {
                Gson gson = new GsonBuilder().create();
                CategoryResponse categoryResponse = gson.fromJson(response, CategoryResponse.class);
                if (categoryResponse != null) {
                    setCategoryApp(categoryResponse, requestCode);
                }
            }
        }

        @Override
        public void onResponseFailure(VolleyError error) {


        }
    }

}