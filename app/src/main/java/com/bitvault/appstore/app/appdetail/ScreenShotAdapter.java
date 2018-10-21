package com.bitvault.appstore.app.appdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bitvault.appstore.R;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.webservice.response.AppDetailResponse;

import java.io.Serializable;
import java.util.List;

/*
 * Created Dheeraj Bansal root on 9/5/17.
 * version 1.0.0
 */

/**
 * screen shot for an app
 */
public class ScreenShotAdapter extends PagerAdapter {


    private List<AppDetailResponse.Result.AppDetail.screenshots> bannerList;
    private LayoutInflater inflater;

    private Activity activity;


    public ScreenShotAdapter(Activity activity, List<AppDetailResponse.Result.AppDetail.screenshots> bannerList) {
        this.bannerList = bannerList;
        inflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.item_screen_shot, view, false);

        imageLayout.findViewById(R.id.id_progressBar);

        assert imageLayout != null;
        final NetworkImageView imageView = (NetworkImageView) imageLayout
                .findViewById(R.id.id_screenshot_img);
        imageView.setErrorImageResId(R.drawable.screenshot_default);
        imageView.setDefaultImageResId(R.drawable.screenshot_default);
        ImageLoader imageLoader = AppStoreApplication.getInstance().getImageLoader();
        imageView.setImageUrl(bannerList.get(position).getAppImageUrl(), imageLoader);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ScreenShotActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(activity.getResources().getString(R.string.banner_list), (Serializable)bannerList);
                intent.putExtra(activity.getResources().getString(R.string.bundle), bundle);
                intent.putExtra(activity.getResources().getString(R.string.current_item_position), position);
                activity.startActivity(intent);
            }
        });
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public float getPageWidth(int position) {
        return 0.4f;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}