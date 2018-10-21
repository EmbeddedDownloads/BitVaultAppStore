package com.bitvault.appstore.app.appdetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.setting.InternetConnectionFragment;
import com.bitvault.appstore.application.APIs;
import com.bitvault.appstore.databinding.ActivityFullDescriptionBinding;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.AppConstants;
import com.bitvault.appstore.utils.NetworkConnection;
import com.bitvault.appstore.webservice.STRINGRequest;
import com.bitvault.appstore.webservice.response.AppDescription;
import com.bitvault.appstore.webservice.response.AppDescriptionInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anuj on 2/5/17.
 * version 1.0.0
 */

/**
 * Display Description about app
 */
public class FullDescriptionActivity extends AppCompatActivity {

    private static final int APP_INFO_SAPANCOUNT = 2;
    private final static String TAG = FullDescriptionActivity.class.getCanonicalName();
    private ActivityFullDescriptionBinding activityFullDescriptionBinding;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFullDescriptionBinding = DataBindingUtil.setContentView(this, R.layout.activity_full_description);
        intent = getIntent();
        AppDescriptionInfo appDescriptionInfo = intent.getParcelableExtra(getResources().getString(R.string.app_description));
        activityFullDescriptionBinding.setAppDetaInfo(appDescriptionInfo);

        if(appDescriptionInfo.getWhats_new() == null) {
            activityFullDescriptionBinding.idLlWhatsnew.setVisibility(View.GONE);
        }

        setAppInfolist(appDescriptionInfo);

    }

    /**
     * Set App detail description
     */
    private void setData(ArrayList<String> appInfoList, ArrayList<String> appInfoHeader) {


        activityFullDescriptionBinding.idRvInfo.setLayoutManager(new GridLayoutManager(this, APP_INFO_SAPANCOUNT));
        AppDescriptionAdapter appInfoAdapter = new AppDescriptionAdapter(this, appInfoList, appInfoHeader);
        activityFullDescriptionBinding.idRvInfo.setAdapter(appInfoAdapter);

    }

    /**
     * Create list for appInfo grid
     *
     * @param appDescriptionInfo
     */
    public void setAppInfolist(AppDescriptionInfo appDescriptionInfo) {
        String appInfoTitle[] = getResources().getStringArray(R.array.array_app_info);
        ArrayList<String> appInfoHeader = new ArrayList<>();
        ArrayList<String> appInfolist = new ArrayList<>();
        if(appDescriptionInfo.getVersion() != null && appDescriptionInfo.getVersion().length() > 0) {
            appInfolist.add(appDescriptionInfo.getVersion());
            appInfoHeader.add(appInfoTitle[0]);
        } if(appDescriptionInfo.getTotalDownloads() != null && appDescriptionInfo.getTotalDownloads().length() > 0) {
            appInfolist.add(appDescriptionInfo.getTotalDownloads());
            appInfoHeader.add(appInfoTitle[1]);
        }  if(appDescriptionInfo.getEmail() != null && appDescriptionInfo.getEmail().length() > 0) {
            appInfolist.add(appDescriptionInfo.getEmail());
            appInfoHeader.add(appInfoTitle[2]);
        } if(appDescriptionInfo.getApkFilesize() != null && appDescriptionInfo.getApkFilesize().length() > 0) {
            appInfolist.add(appDescriptionInfo.getApkFilesize());
            appInfoHeader.add(appInfoTitle[3]);
        } if(appDescriptionInfo.getUpdated_date() != null && appDescriptionInfo.getUpdated_date().length() > 0) {
            appInfolist.add(appDescriptionInfo.getUpdated_date());
            appInfoHeader.add(appInfoTitle[4]);
        }

        //use later
//        appInfolist.add(appDescriptionInfo.getOffered_by());
//        appInfolist.add(appDescriptionInfo.getDeveloper_address());

        setData(appInfolist, appInfoHeader);

    }

    /**
     * Activity will finish on Cancel button click
     *
     * @param view
     */
    public void onCancelClicked(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}