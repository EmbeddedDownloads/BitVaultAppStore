package com.bitvault.appstore.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.appdetail.PermissionDetailAdapter;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.utils.VerticalDividerItemDecoration;
import com.bitvault.appstore.webservice.response.AppDetailResponse;
import com.bitvault.appstore.webservice.response.AppPermissions;

import java.util.List;

import static android.icu.lang.UProperty.INT_START;

/*
 * Created Dheeraj Bansal root on 11/5/17.
 * version 1.0.0
 */

/**
 * permission details dialog
 */
public class PermissionDialog extends Dialog {


    private final int INT_START = 0;
    private int INT_END = 1;

    /**
     * to check all permission details used by application
     *
     * @param context
     * @param iconUrl
     * @param appName
     * @param appVersion
     * @param permissionsList
     */
    public PermissionDialog(@NonNull Context context, String iconUrl, String appName, String appVersion,
                            List<AppPermissions.Permission> permissionsList) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.permission_detail_dialog);
        NetworkImageView appIcon = (NetworkImageView) findViewById(R.id.id_pmn_app_icon);
        TextView appNameTxt = (TextView) findViewById(R.id.id_pmn_app_name);
        TextView appVersionTxt = (TextView) findViewById(R.id.id_pmn_app_version);
        RecyclerView permissionRecyclerView = (RecyclerView) findViewById(R.id.id_pmn_recylerview);
        permissionRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        appIcon.setDefaultImageResId(R.mipmap.ic_launcher);
        if (iconUrl != null) {
            appIcon.setImageUrl(iconUrl, AppStoreApplication.getInstance().getImageLoader());
        }
        if (appName != null) {
            appNameTxt.setText(appName);
        }
        appVersionTxt.setText(String.format(context.getString(R.string.version_access), appVersion));
        permissionRecyclerView.addItemDecoration(new VerticalDividerItemDecoration(0));
        if (permissionsList != null && permissionsList.size() > 0) {
            permissionRecyclerView.setAdapter(new PermissionDetailAdapter(permissionsList, context));
        } else {
            permissionRecyclerView.setVisibility(View.GONE);
            TextView noPermissionText = (TextView) findViewById(R.id.id_tv_no_permission_dialog);
            noPermissionText.setVisibility(View.VISIBLE);
            SpannableStringBuilder str = new SpannableStringBuilder(Utils.getFirstLetterUpperandOtherSmall(appName) + " " + context.getString(R.string.no_permission_text));
            INT_END = appName.length();
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            noPermissionText.setText(str);
        }
    }

}
