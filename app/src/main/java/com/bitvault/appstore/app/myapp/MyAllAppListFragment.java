package com.bitvault.appstore.app.myapp;

import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.dashboard.MainActivity;
import com.bitvault.appstore.app.setting.InternetConnectionFragment;
import com.bitvault.appstore.application.APIs;
import com.bitvault.appstore.databinding.FragmentMyappListBinding;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.NetworkConnection;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.STRINGRequest;
import com.bitvault.appstore.webservice.response.MyAppVersionModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by anuj on 8/5/17.
 * version 1.0.0
 * My All app with tab
 */

public class MyAllAppListFragment extends Fragment implements InternetConnectionFragment.InternetConnectingListener {

    private static final String TAG = MyAllAppListFragment.class.getCanonicalName();
    ArrayList<ResolveInfo> resolveInfoInstalledArrayList;
    private FragmentMyappListBinding fragmentMyappListBinding;
    private MyAllAppListAdapter myAllAppListAdapter;
    private ArrayList<CustomResolveInfo> updatesArrayList;
    private ArrayList<CustomResolveInfo> recentlyUpdatedArrayList;
    private FragmentActivity activity;
    private List<ResolveInfo> pkgAppsList;
    private StringBuffer packageName;
    private RelativeLayout relativeLayoutLoading;
    private ArrayList<MyAppVersionModel.MyAppVersion> myAppVersionModelArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        fragmentMyappListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_myapp_list, container, false);
        View view = fragmentMyappListBinding.getRoot();
        activity = getActivity();

        pkgAppsList = Utils.getAppMetaData(activity);
        pkgAppsList.size();
        ArrayList<ResolveInfo> uniquePkgAppsList = new ArrayList<>();
        int pkgAppsListSize = pkgAppsList.size();
        for (int i = 0; i < pkgAppsListSize; i++) {
            int uniqueCount = 0;
            for(int j = 0; j < uniquePkgAppsList.size(); j++) {
                if(pkgAppsList.get(i).activityInfo.packageName.equals(uniquePkgAppsList.get(j).activityInfo.packageName)) {
                    break;
                } else {
                    uniqueCount++;
                }
            }
            if(uniqueCount == uniquePkgAppsList.size()) {
                uniquePkgAppsList.add(pkgAppsList.get(i));
            }

        }

        pkgAppsList.clear();
        pkgAppsList.addAll(uniquePkgAppsList);
        pkgAppsList.size();

        relativeLayoutLoading = (RelativeLayout) view.findViewById(R.id.loadingLout);
        packageName = new StringBuffer();
        for (int i = 0; i < pkgAppsList.size(); i++) {
            if (i == 0) {
                packageName.append(pkgAppsList.get(i).activityInfo.applicationInfo.packageName);
            } else {
                packageName.append(",").append((pkgAppsList.get(i).activityInfo.applicationInfo.packageName.toString()));
            }
        }

        initViews();
        getDataFromServer();

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    /**
     * hit api for get latest version of all apps
     */
    public void getDataFromServer() {
        if (NetworkConnection.isNetworkAvailable(getContext())) {
            HashMap param = new HashMap<>();
            param.put(activity.getString(R.string.packageNames), packageName.toString());
            new STRINGRequest().postRequest(activity, new AppLatestVersion(), param, APIs.API_LATEST_VERSION, Request.Method.POST, 1);
            relativeLayoutLoading.setVisibility(View.VISIBLE);
        } else {
            InternetConnectionFragment internetConnectionFragment = new InternetConnectionFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(getResources().getString(R.string.internet_error_type), Utils.TAG_INTERNET_NOT_AVAILABLE);
            internetConnectionFragment.setInternetConnectionListener(this);
            ((MainActivity) activity).replaceFragment(internetConnectionFragment, bundle, Utils.TAG_INTERNET_CONNNECTION_FRAGMENT, true);
        }

    }

    /**
     * show search view on scroll down
     */
    private void showViews() {
        ((MainActivity) getActivity()).mSearchView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    /**
     * hide search view on scroll down
     */
    private void hideViews() {
        ((MainActivity) getActivity()).mSearchView.animate().translationY(-(int) getResources().getDimension(R.dimen.dimen56)).setInterpolator(new AccelerateInterpolator(2));
    }

    /**
     * initialize views
     */
    private void initViews() {
        updatesArrayList = new ArrayList<>();
        recentlyUpdatedArrayList = new ArrayList<>();
        resolveInfoInstalledArrayList = new ArrayList<>();
        myAppVersionModelArrayList = new ArrayList<>();
        fragmentMyappListBinding.idRvRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myAllAppListAdapter = new MyAllAppListAdapter(getContext(), updatesArrayList, recentlyUpdatedArrayList,
                resolveInfoInstalledArrayList, myAppVersionModelArrayList, TAG);
        fragmentMyappListBinding.idRvRecyclerView.setAdapter(myAllAppListAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    /**
     * method for set data in app list
     */
    private void setDataLocally() {
        relativeLayoutLoading.setVisibility(View.GONE);
        //remove later
        //if (pkgAppsList.size() > 9) {
        updatesArrayList.clear();
        recentlyUpdatedArrayList.clear();
        resolveInfoInstalledArrayList.clear();

        //replace later
        //resolveInfoInstalledArrayList.addAll(pkgAppsList);

        for (int i = 0; i < pkgAppsList.size(); i++) {
            checkAppVersion(pkgAppsList.get(i));
        }

        myAllAppListAdapter.upDateListItem();
        myAllAppListAdapter.notifyDataSetChanged();
        //}

    }

    /**
     * return true if version of app is update otherwise return false
     *
     * @param resolveInfo
     */
    public boolean checkAppVersion(ResolveInfo resolveInfo) {

        try {
            //myAppVersionModelArrayList.stream().filter(myAppVersionModelArrayList -> myAppVersionModelArrayList.getPackageName().equals(resolveInfo.activityInfo.applicationInfo.packageName.toString()));
            String packageName = resolveInfo.activityInfo.applicationInfo.packageName.toString();
            int currentVersion = getContext().getPackageManager().getPackageInfo(resolveInfo.activityInfo.applicationInfo.packageName.toString(), 0).versionCode;
            for (int i = 0; i < myAppVersionModelArrayList.size(); i++) {
                if (myAppVersionModelArrayList.get(i).getPackageName().equals(packageName)) {
                    if (currentVersion == Integer.parseInt(myAppVersionModelArrayList.get(i).getLatestVersionNo())) {
                        long installed = new File(resolveInfo.activityInfo.applicationInfo.sourceDir).lastModified();
                        long milisecondsDifference = System.currentTimeMillis() - installed;
                        if (TimeUnit.MILLISECONDS.toDays(milisecondsDifference) < 4) {
                            recentlyUpdatedArrayList.add(new CustomResolveInfo(resolveInfo, Long.parseLong(myAppVersionModelArrayList.get(i).getApplicationId()), myAppVersionModelArrayList.get(i).getAverageRating()));
                        } else {

                        }
                        return false;

                    } else {
                        updatesArrayList.add(new CustomResolveInfo(resolveInfo, Long.parseLong(myAppVersionModelArrayList.get(i).getApplicationId()), myAppVersionModelArrayList.get(i).getAverageRating()));
                        return true;
                    }

                }
            }
            return false;
        } catch (Exception e) {
            AndroidAppUtils.showLogE(TAG, e.toString());
            return false;
        }

    }

    @Override
    public void onConnectionRetry() {
        getDataFromServer();
    }

    public class AppLatestVersion implements STRINGRequest.StringResponseListener {

        @Override
        public void onResponseSuccess(String response, int requestCode) {

            if(activity != null) {

                if (response.contains(activity.getString(R.string.status)) && response.contains(activity.getString(R.string.success))
                        && response.contains(activity.getString(R.string.result))) {
                    Gson gson = new GsonBuilder().create();
                    MyAppVersionModel myAppVersionModel = gson.fromJson(response, MyAppVersionModel.class);
                    if (myAppVersionModel != null) {
                        myAppVersionModelArrayList.addAll(myAppVersionModel.getResult());
                        //use later
                        //if(myAppVersionModelArrayList != null && myAppVersionModelArrayList.size() > 0) {
                        setDataLocally();
                    }
                }
            }

        }

        @Override
        public void onResponseFailure(VolleyError error) {

            if(activity != null) {
                AndroidAppUtils.showLogE(TAG, error.toString());
                try {
                    if (error.toString().equals(activity.getResources().getString(R.string.timeout_error)) || error.toString().contains(activity.getResources().getString(R.string.no_connection_error))) {
                        InternetConnectionFragment internetConnectionFragment = new InternetConnectionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt(activity.getResources().getString(R.string.internet_error_type), Utils.TAG_TIMEOUT_ERROR);
                        internetConnectionFragment.setInternetConnectionListener(MyAllAppListFragment.this);
                        ((MainActivity) activity).replaceFragment(internetConnectionFragment, bundle, Utils.TAG_INTERNET_CONNNECTION_FRAGMENT, true);

                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
