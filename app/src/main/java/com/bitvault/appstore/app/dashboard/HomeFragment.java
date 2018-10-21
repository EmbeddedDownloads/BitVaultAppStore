package com.bitvault.appstore.app.dashboard;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.setting.InternetConnectionFragment;
import com.bitvault.appstore.application.APIs;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.svlibrary.utils.Util;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.HorizontalDividerItemDecoration;
import com.bitvault.appstore.utils.NetworkConnection;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.STRINGRequest;
import com.bitvault.appstore.webservice.response.HomePageAppsResponse;
import com.bitvault.appstore.webservice.response.HomePageBannerCategoryResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/*
 * Created Dheeraj Bansal root on 8/5/17.
 * version 1.0.0
 */


/**
 * Dashboard banner and category wise app
 */
public class HomeFragment extends Fragment implements InternetConnectionFragment.InternetConnectingListener {

    private final static String TAG = HomeFragment.class.getCanonicalName();
    private final static int CATEGORY_BANNER = 1;
    private final static int CATEGORY_APP = 2;
    private int DESHBOARE_SCREENSHOT_DELAY = 10000;

    private FragmentActivity activity;
    private ViewPager viewPagerBanner;
    private RecyclerView recyclerViewCategory;
    private RecyclerView recyclerViewCategoryApp;
    private RelativeLayout relativeLayoutLoading;
    private int mToolbarOffset = 0, mToolbarHeight;
    private AppHeaderCategoryAdapter appHeaderCategoryAdapter;
    private List<HomePageAppsResponse.AppCategoryList> appCategoryLists;
    private boolean loadMoreData = true;
    private boolean isAllDataLoaded = false;
    private int pageNo = 1;
    private View view;
    private float X, Y, dX, dY;
    private TextView tvNoAppText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.home_fragment, container, false);

            activity = getActivity();
            mToolbarHeight = Utils.getToolbarHeight(activity) + Util.dpToPx(activity, getResources().getDimension(R.dimen.dimen10));
            viewPagerBanner = (ViewPager) view.findViewById(R.id.id_viewpager);
            recyclerViewCategory = (RecyclerView) view.findViewById(R.id.id_category_recyclerview);
            relativeLayoutLoading = (RelativeLayout) view.findViewById(R.id.loadingLout);
            tvNoAppText = (TextView) view.findViewById(R.id.id_no_app_text);
            recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewCategory.addItemDecoration(
                    new HorizontalDividerItemDecoration((int) getResources().getDimension(R.dimen.app_item_margin)));

            recyclerViewCategoryApp = (RecyclerView) view.findViewById(R.id.id_app_recyclerview);
            recyclerViewCategoryApp.setLayoutManager(new LinearLayoutManager(getContext()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });

            viewPagerBanner.setVisibility(View.GONE);
            recyclerViewCategory.setVisibility(View.GONE);
            recyclerViewCategoryApp.setVisibility(View.GONE);

            NestedScrollView scrollView = (NestedScrollView) view.findViewById(R.id.id_nested_scroll);
            scrollView.setFillViewport(true);
            scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    ((MainActivity) activity).clearAdapter();
                    mToolbarOffset += -(scrollY - oldScrollY);
                    if (mToolbarOffset < -mToolbarHeight) {
                        mToolbarOffset = -mToolbarHeight;
                    } else if (mToolbarOffset > 0) {
                        mToolbarOffset = 0;
                    }
                    ((MainActivity) activity).setHeader(mToolbarOffset);

                }
            });
            getDataFromServer();
        }
        return view;
    }

    /**
     * get apps data from Server
     */
    public void getDataFromServer() {
        if (NetworkConnection.isNetworkAvailable(getContext())) {
            new STRINGRequest().postRequest(getContext(), new LoadHomeDataListener(), null, APIs.API_DASHBOARD_CATEGORY_BANNER, Request.Method.GET, CATEGORY_BANNER);
            new STRINGRequest().postRequest(getContext(), new LoadHomeDataListener(), null, APIs.API_DASHBOARD_CATEGORY_APP + "?" + activity.getString(R.string.page) + "=" + pageNo + "&" + activity.getString(R.string.size) + "=10&direction=DESC&" +
                    activity.getString(R.string.publicAddress) + "=" + Utils.getPublicAddress(activity), Request.Method.GET, CATEGORY_APP);
            relativeLayoutLoading.setVisibility(View.VISIBLE);
        } else {
            noInternetConnection();

        }
    }

    /**
     * method open no internet Connection screen
     */
    public void noInternetConnection() {
        InternetConnectionFragment internetConnectionFragment = new InternetConnectionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(getResources().getString(R.string.internet_error_type), Utils.TAG_INTERNET_NOT_AVAILABLE);
        internetConnectionFragment.setInternetConnectionListener(this);
        ((MainActivity) activity).replaceFragment(internetConnectionFragment, bundle, Utils.TAG_INTERNET_CONNNECTION_FRAGMENT, true);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) activity).setHeaderVisibility(View.VISIBLE);

        if(appHeaderCategoryAdapter != null) {
            appHeaderCategoryAdapter.notifyData();
        }

        //remove later
//        try {
//            InputStream inputStream = getResources().openRawResource(R.raw.home);
//            String data = Utils.readTextFile(inputStream);
//            Gson gson = new GsonBuilder().create();
//            HomePageAppsResponse response = gson.fromJson(data, HomePageAppsResponse.class);
//            if(response!=null)
//            {
//                jsonParse(response);
//            }
//        }
//        catch (NullPointerException e){
//            AndroidAppUtils.showLogE(TAG,e.toString());
//        }

    }

    /**
     * method for runtime permission and take screen shot of deshboard screen
     */
    private void deshboardScreenShot() {
        final Bitmap mbitmap = getBitmapOFRootView(recyclerViewCategoryApp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Nammu.askForPermission((Activity) activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    createImage(mbitmap);
                }


                @Override
                public void permissionRefused() {
                    AndroidAppUtils.showToast(activity, activity.getResources().getString(R.string.text_permisson_refused));
                }
            });
        } else {
            createImage(mbitmap);
        }
    }

    /**
     * return bitmap of root view
     * @param v
     * @return
     */
    public Bitmap getBitmapOFRootView(View v) {
        View rootview = v.getRootView();
        rootview.setDrawingCacheEnabled(true);
        Bitmap bitmap1 = rootview.getDrawingCache();
        return bitmap1;
    }

    /**
     * creates file for screenshot bitmap
     * @param bmp
     */
    public void createImage(Bitmap bmp) {
        if(bmp != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            File file = new File(Environment.getExternalStorageDirectory() + activity.getString(R.string.deshboard_screenshot));
            try {
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(bytes.toByteArray());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * cancel all pending request
     */
    @Override
    public void onStop() {
        super.onStop();
        AppStoreApplication.getInstance().cancelPendingRequests(AppStoreApplication.getInstance().getRequestQueue());
    }

    /**
     * Display data in to views
     *
     * @param response
     */
    private void jsonParseBannerCategory(HomePageBannerCategoryResponse response) {

        List<HomePageBannerCategoryResponse.Category> categoryList = response.getResult().getCategory();
        if (categoryList == null || categoryList.size() == 0) {
            viewPagerBanner.setVisibility(View.GONE);
            recyclerViewCategory.setVisibility(View.GONE);
        } else {
            if(activity != null) {
                viewPagerBanner.setVisibility(View.VISIBLE);
                viewPagerBanner.setAdapter(new BannerAdapter(activity, categoryList));

                recyclerViewCategory.setVisibility(View.VISIBLE);
                recyclerViewCategory.setAdapter(new CategoryAdapter(activity, categoryList));
            }
        }

    }

    /**
     * Display data in to views
     *
     * @param response
     */
    private void jsonParseCategoryApp(HomePageAppsResponse response) {

            appCategoryLists = response.getResult();
            if (appCategoryLists == null || appCategoryLists.size() == 0) {
                recyclerViewCategoryApp.setVisibility(View.GONE);
                tvNoAppText.setVisibility(View.VISIBLE);
                relativeLayoutLoading.setVisibility(View.GONE);
            } else {
                if(activity != null) {
                    appHeaderCategoryAdapter = new AppHeaderCategoryAdapter(activity, appCategoryLists, recyclerViewCategoryApp);
                    recyclerViewCategoryApp.setAdapter(appHeaderCategoryAdapter);
                    recyclerViewCategoryApp.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deshboardScreenShot();
                        }
                    }, DESHBOARE_SCREENSHOT_DELAY);

                    relativeLayoutLoading.setVisibility(View.GONE);
                    recyclerViewCategoryApp.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            boolean value = false;

                            switch (motionEvent.getAction()) {

                                case MotionEvent.ACTION_DOWN:
                                    X = motionEvent.getX();
                                    Y = motionEvent.getY();

                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    dX = dX + motionEvent.getX() - X;
                                    dY = dY + motionEvent.getY() - Y;
                                    if (dX > dY) {
                                        value = true;
                                    }


                                    break;
                                case MotionEvent.ACTION_UP:

                                    break;
                            }
                            return value;
                        }
                    });

                }
            }
    }

    @Override
    public void onConnectionRetry() {
        getDataFromServer();
    }

    /**
     * for api call later use
     */
    private class LoadHomeDataListener implements STRINGRequest.StringResponseListener {

        @Override
        public void onResponseSuccess(String response, int requestCode) {

            if(activity != null) {

                if (response.contains(activity.getString(R.string.status))) {
                    if (response.contains(activity.getString(R.string.success))) {
                        if (requestCode == CATEGORY_APP) {

                            try {

                                Gson gson = new GsonBuilder().create();
                                HomePageAppsResponse homePageAppsResponse = gson.fromJson(response, HomePageAppsResponse.class);
                                if (homePageAppsResponse != null) {
                                    jsonParseCategoryApp(homePageAppsResponse);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (requestCode == CATEGORY_BANNER) {
                            Gson gson = new GsonBuilder().create();
                            HomePageBannerCategoryResponse homePageBannerCategoryResponse = gson.fromJson(response, HomePageBannerCategoryResponse.class);
                            if (homePageBannerCategoryResponse != null) {
                                jsonParseBannerCategory(homePageBannerCategoryResponse);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onResponseFailure(VolleyError error) {

            if(activity != null) {
                AndroidAppUtils.showLogE(TAG, error.toString());
                try {
                    if (error.toString().equals(activity.getResources().getString(R.string.timeout_error))) {
                        InternetConnectionFragment internetConnectionFragment = new InternetConnectionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt(activity.getResources().getString(R.string.internet_error_type), Utils.TAG_TIMEOUT_ERROR);
                        internetConnectionFragment.setInternetConnectionListener(HomeFragment.this);
                        ((MainActivity) activity).replaceFragment(internetConnectionFragment, bundle, Utils.TAG_INTERNET_CONNNECTION_FRAGMENT, true);

                    } else if (error.toString().contains(activity.getResources().getString(R.string.no_connection_error))) {
                        noInternetConnection();
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
