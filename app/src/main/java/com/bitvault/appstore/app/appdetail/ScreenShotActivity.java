package com.bitvault.appstore.app.appdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.bitvault.appstore.R;
import com.bitvault.appstore.utils.DepthPageTransformer;
import com.bitvault.appstore.webservice.response.AppDetailResponse;

import java.util.List;

import pl.tajchert.nammu.Nammu;

/*
 * Created Dheeraj Bansal root on 10/5/17.
 * version 1.0.0
 */

/**
 * tap and finger zoom in and zoom out screen shot
 */
public class ScreenShotActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_screentshot);
        Nammu.init(getApplicationContext());

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(getResources().getString(R.string.bundle));
        List<AppDetailResponse.Result.AppDetail.screenshots> bannerList = (List<AppDetailResponse.Result.AppDetail.screenshots>) bundle.getSerializable(getResources().getString(R.string.banner_list));
        int currentPosition = intent.getIntExtra(getResources().getString(R.string.current_item_position), 0);
        ViewPager pager = (ViewPager) findViewById(R.id.shotViewPager);
        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.setAdapter(new FullScreenShotAdapter(this, bannerList));
        pager.setCurrentItem(currentPosition);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
