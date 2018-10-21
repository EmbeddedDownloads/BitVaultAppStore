package com.bitvault.appstore.app.appdetail;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bitvault.appstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created Dheeraj Bansal root on 16/5/17.
 * version 1.0.0
 * rating submit adapter
 */

public class ReviewPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> listItem;
    private RatingFragment ratingFragment;

    public ReviewPagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        listItem=new ArrayList<>();
        listItem.add(context.getResources().getString(R.string.rate_app));
        listItem.add(context.getResources().getString(R.string.write_review));
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ratingFragment = new RatingFragment();
                return ratingFragment;
            case 1:
                return new ReviewFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    /**
     * clear list
     */
    public void clearAdapter(){
        if(listItem!=null)
        {
            listItem.clear();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public RatingFragment getRatingFragment() {
        if(ratingFragment != null) {
            return ratingFragment;
        } else {
            return null;
        }
    }
}