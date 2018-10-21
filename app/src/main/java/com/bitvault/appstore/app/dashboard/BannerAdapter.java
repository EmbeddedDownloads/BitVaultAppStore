package com.bitvault.appstore.app.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitvault.appstore.R;
import com.bitvault.appstore.app.category.CategoryFragment;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.custom.CircularImageView;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.response.HomePageAppsResponse;
import com.bitvault.appstore.webservice.response.HomePageBannerCategoryResponse;

import java.util.List;

/*
 * Created Dheeraj Bansal root on 2/5/17.
 * version 1.0.0
 */

/**
 * show banner of new app
 */
public class BannerAdapter extends PagerAdapter {


    private List<HomePageBannerCategoryResponse.Category> bannerList;
    private LayoutInflater inflater;

    private Activity activity;


    public BannerAdapter(Activity activity, List<HomePageBannerCategoryResponse.Category> bannerList) {
        this.bannerList = bannerList;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
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
        View imageLayout = inflater.inflate(R.layout.item_banner, view, false);

        final HomePageBannerCategoryResponse.Category banner = bannerList.get(position);

        assert imageLayout != null;
        final CircularImageView imageView = (CircularImageView) imageLayout
                .findViewById(R.id.id_banner_image);

        if (bannerList.get(position).getCategoryBannerUrl() != null) {
            imageView.setImageUrl(banner.getCategoryBannerUrl(), AppStoreApplication.getInstance().getImageLoader());
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(activity.getString(R.string.header_title), banner.getCategoryType());
                bundle.putString(activity.getString(R.string.source), activity.getString(R.string.banner));
                bundle.putString(activity.getString(R.string.categoryIdList), String.valueOf(banner.getAppCategoryId()));
                ((MainActivity)activity).replaceFragment(new CategoryFragment(), bundle, Utils.TAG_CATEGORY_FRAGMENT, true);
            }
        });
        if (position == 0) {
            view.setPadding((int) activity.getResources().getDimension(R.dimen.dimen10), 0, 0, 0);
        }

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public float getPageWidth(int position) {
        if(bannerList != null && bannerList.size() == 1) {
            return 1;
        } else {
            return 0.9f;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
