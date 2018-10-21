package com.bitvault.appstore.app.setting;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.widget.CompoundButton;

import com.bitvault.appstore.R;
import com.bitvault.appstore.databinding.ActivitySettingBinding;
import com.bitvault.appstore.dialog.AppUpdateDialog;
import com.bitvault.appstore.dialog.PurchaseAuthDialog;
import com.bitvault.appstore.preference.AppPreferences;
import com.bitvault.appstore.utils.Utils;

/**
 * Created by anuj on 9/5/17.
 * version 1.0.0
 * setting of app store
 */

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding activitySettingBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySettingBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        initViews();
        setListener();
    }

    /**
     * initialize views
     */
    private void initViews() {

           setAppAutoUpdateText();
        //remove later
           //setPurchaseAuthText();
           activitySettingBinding.idStCheckItemIcon.setChecked(AppPreferences.getInstance(this).getIconOnHomeScreen());
           activitySettingBinding.idNotifyAppUpdateAvailable.setChecked(AppPreferences.getInstance(this).getNotifyAppUpdateAvailable());
           activitySettingBinding.idTvAppStoreVersion.setText(Utils.getVersionName(getApplicationContext(), getPackageName()));


    }

    /**
     * Set listener on view item click
     */
    private void setListener(){

        activitySettingBinding.idLlAutoUpdateApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUpdateDialog appUpdateDialog = new AppUpdateDialog();
                appUpdateDialog.show(getFragmentManager(), Utils.TAG_APP_UPDATE_DIALOG);
                appUpdateDialog.setDismissListener(new AppUpdateDialog.DismissListener() {
                    @Override
                    public void onDismissListener(DialogInterface dialog) {
                                 setAppAutoUpdateText();
                    }
                });
            }
        });


        //remove later
        /*activitySettingBinding.idLlRequiredAuthForPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PurchaseAuthDialog purchaseAuthDialog = new PurchaseAuthDialog();
                purchaseAuthDialog.show(getFragmentManager(), Utils.TAG_PURCHASE_AUTH_DIALOG);
                purchaseAuthDialog.setDismissListener(new PurchaseAuthDialog.DismissListener() {
                    @Override
                    public void onDismissListener(DialogInterface dialog) {
                        setPurchaseAuthText();
                    }
                });
            }
        });*/

        activitySettingBinding.idStCheckItemIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppPreferences.getInstance(SettingActivity.this).setIconOnHomeScreen(b);

            }
        });

        activitySettingBinding.idNotifyAppUpdateAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppPreferences.getInstance(SettingActivity.this).setNotifyAppUpdateAvailable(b);
            }
        });

        activitySettingBinding.idLlClearLocalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPreferences.getInstance(SettingActivity.this).clearSearchBarKeywords();
            }
        });
    }

    /**
     * method set App Auto Update Test
     */
    private void setAppAutoUpdateText() {
        activitySettingBinding.idTvAppAutoUpdate.setText(AppPreferences.getInstance(this).getAutoUpdateApp());
    }

    //remove later
    /**
     * method set Purchase authentication Test
     *//*
    private void setPurchaseAuthText() {
        activitySettingBinding.idTvForAllPurchase.setText(AppPreferences.getInstance(this).getAuthForPurchase());
    }*/


    /**
     * cancel text
     * @param view
     */
    public void onCancelClicked(View view){
        finish();
    }
}
