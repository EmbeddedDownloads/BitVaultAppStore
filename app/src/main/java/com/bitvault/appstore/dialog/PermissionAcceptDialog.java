package com.bitvault.appstore.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.appdetail.FileDownloadManager;
import com.bitvault.appstore.app.appdetail.OnPermissionAcceptListener;
import com.bitvault.appstore.app.appdetail.PermissionAdapter;
import com.bitvault.appstore.app.appdetail.PermissionCategory;
import com.bitvault.appstore.app.appdetail.PermissionDescription;
import com.bitvault.appstore.app.appdetail.ProductDetailFragment;
import com.bitvault.appstore.app.dashboard.MainActivity;
import com.bitvault.appstore.app.setting.InternetConnectionFragment;
import com.bitvault.appstore.application.APIs;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.svlibrary.utils.Util;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.MyLinearLayoutManager;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.utils.VerticalDividerItemDecoration;
import com.bitvault.appstore.webservice.STRINGRequest;
import com.bitvault.appstore.webservice.response.AppDetailResponse;
import com.bitvault.appstore.webservice.response.AppPermissions;
import com.bitvault.appstore.webservice.response.SearchResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import pl.tajchert.nammu.Nammu;

/*
 * Created Dheeraj Bansal root on 12/5/17.
 * version 1.0.0
 */

/**
 * permission accept dialog
 */
public class PermissionAcceptDialog extends Dialog {

    private static String TAG = PermissionAcceptDialog.class.getCanonicalName();
    private Context mContext;
    private int thresHoldValue = -1;
    private PermissionAdapter permissionAdapter;
    private LinearLayout idllPermissionDialog;
    private NetworkImageView appIcon;
    private TextView appNameTxt, acceptTxt;
    private RecyclerView permissionRecyclerView;
    private OnPermissionAcceptListener mOnPermissionAcceptListener;
    private String iconUrl, appName;
    private RelativeLayout rlLoading;
    private StringRequest request;
    private ArrayList<AppPermissions.Permission> permissionList;
    private boolean heightAdjustFirst = true, setActualHeight;

    /**
     * permission dialog before install application from appstore
     * @param context
     * @param listener
     * @param iconUrl
     * @param appName
     */

    public PermissionAcceptDialog(@NonNull final Context context, final OnPermissionAcceptListener listener, String iconUrl, String appName, ArrayList<AppPermissions.Permission> permissionList, String applicationId) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.permission_dialog);
        mContext = context;
        this.mOnPermissionAcceptListener = listener;
        this.iconUrl = iconUrl;
        this.appName = appName;
        this.permissionList = permissionList;
        idllPermissionDialog = (LinearLayout) findViewById(R.id.id_ll_permission_dialog);
        appIcon = (NetworkImageView) findViewById(R.id.id_pmn_app_icon);
        appNameTxt = (TextView) findViewById(R.id.id_pmn_app_name);
        acceptTxt = (TextView) findViewById(R.id.id_pmn_accept_txt);
        permissionRecyclerView = (RecyclerView) findViewById(R.id.id_pmn_recylerview);
        permissionRecyclerView.setLayoutManager(new MyLinearLayoutManager(mContext, 1, false));
        rlLoading = (RelativeLayout) findViewById(R.id.loadingLout);
        rlLoading.getLayoutParams().height = Util.dpToPx(mContext, 200);
        rlLoading.requestLayout();
        appIcon.setDefaultImageResId(R.mipmap.ic_launcher);

        HashMap param = new HashMap();
        param.put(mContext.getString(R.string.appId), applicationId);


        request = new STRINGRequest().postRequest(context, new LoadProductPermissionsListener(), param, APIs.API_INSTALL_APK_PERMISSIONS, Request.Method.POST, 1);


    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(request != null) {
            request.cancel();
        }
    }

    public void adjustDialogHeight() {

        DisplayMetrics displaymetrics = mContext.getResources().getDisplayMetrics();

        int a = (displaymetrics.heightPixels * 50) / 100;

        if (permissionRecyclerView.getHeight() <= a && (permissionAdapter.isLayoutChange())) {
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            permissionRecyclerView.setLayoutParams(lp);
            permissionAdapter.setLayoutChange(false);
            setActualHeight = true;

        }

        if (permissionRecyclerView.getHeight() > a && (permissionAdapter.isLayoutChange() || heightAdjustFirst || setActualHeight)) {
            heightAdjustFirst = false;
            if (thresHoldValue == -1) {
                thresHoldValue = permissionRecyclerView.getHeight();
            }
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, a);
            permissionRecyclerView.setLayoutParams(lp);
            permissionAdapter.setLayoutChange(false);
            setActualHeight = false;

        }
    }

    private void dismissDialog() {
        if(mOnPermissionAcceptListener != null) {
            mOnPermissionAcceptListener.onPermissionAccept();
        }
        dismiss();
    }

    /**
     * for api call
     */
    private class LoadProductPermissionsListener implements STRINGRequest.StringResponseListener {

        @Override
        public void onResponseSuccess(String response, int requestCode) {

            if(response != null && mContext != null) {

                rlLoading.setVisibility(View.GONE);
                idllPermissionDialog.setVisibility(View.VISIBLE);
                Gson gson = new GsonBuilder().create();
                AppPermissions appPermissions = gson.fromJson(response, AppPermissions.class);


                if (iconUrl != null) {
                    appIcon.setImageUrl(iconUrl, AppStoreApplication.getInstance().getImageLoader());
                }
                if (appName != null) {
                    appNameTxt.setText(appName);
                }
                permissionRecyclerView.addItemDecoration(new VerticalDividerItemDecoration(
                        (int) mContext.getResources().getDimension(R.dimen.dimen5)));
                if (permissionList != null && permissionList.size() > 0) {
                    permissionAdapter = new PermissionAdapter(permissionList, mContext);
                    permissionRecyclerView.setAdapter(permissionAdapter);
                } else if(response.contains(mContext.getString(R.string.status)) && response.contains(mContext.getString(R.string.success))
                        && response.contains(mContext.getString(R.string.result))) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String appPermission = object.getString(mContext.getString(R.string.result));

                        if (appPermission != null && appPermission.length() > 1) {
                            String permissions[] = appPermission.split(",");

                            ArrayList<AppPermissions.Permission> permissionArrayList = new ArrayList<>();
                            InputStream inputStream = mContext.getResources().openRawResource(R.raw.permission_category);
                            String data = Utils.readTextFile(inputStream);
                            gson = new GsonBuilder().create();
                            PermissionCategory permissionCategory = gson.fromJson(data, PermissionCategory.class);
                            ArrayList<PermissionCategory.Category> permissionCategoryArrayList = permissionCategory.getPermission_category();

                            inputStream = mContext.getResources().openRawResource(R.raw.permission_description);
                            data = Utils.readTextFile(inputStream);
                            gson = new GsonBuilder().create();
                            PermissionDescription permissionDescription = gson.fromJson(data, PermissionDescription.class);
                            ArrayList<PermissionDescription.Description> permissionDescriptionArrayList = permissionDescription.getPermission_description();
                            for (int i = 0; i < permissions.length; i++) {
                                for (int j = 0; j < permissionCategoryArrayList.size(); j++) {
                                    if (permissions[i].equals(permissionCategoryArrayList.get(j).getPermission())) {
                                        for (int k = 0; k < permissionDescriptionArrayList.size(); k++) {
                                            PermissionDescription.Description description = permissionDescriptionArrayList.get(k);
                                            if (permissionCategoryArrayList.get(j).getGroup().equals(description.getGroup())) {
                                                AppPermissions.Permission permissionObject = new AppPermissions.Permission(description.getIcon(), description.getHeader(), description.getGroup_permission(), description.getDescription());
                                                permissionArrayList.add(permissionObject);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            HashSet<AppPermissions.Permission> permissionHashSet = new HashSet<>(permissionArrayList);
                            permissionArrayList.clear();
                            permissionArrayList.addAll(permissionHashSet);

                            if (permissionArrayList != null && permissionArrayList.size() > 0) {
                                permissionAdapter = new PermissionAdapter(permissionArrayList, mContext);
                                permissionRecyclerView.setAdapter(permissionAdapter);
                            } else {
                                dismissDialog();
                            }


                        } else {
                            dismissDialog();
                        }
                    } catch (JSONException jsonException) {

                    }

                }
                acceptTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       dismissDialog();
                    }
                });
                if(permissionAdapter != null) {
                    adjustDialogHeight();
                }


                ViewTreeObserver vto = permissionRecyclerView.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        if(permissionAdapter != null) {
                            adjustDialogHeight();
                        }

                    }
                });

            }
        }

        @Override
        public void onResponseFailure(VolleyError error) {

        }
    }

}
