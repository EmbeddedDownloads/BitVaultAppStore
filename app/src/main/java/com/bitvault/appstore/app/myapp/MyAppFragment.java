package com.bitvault.appstore.app.myapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitvault.appstore.R;
import com.bitvault.appstore.app.dashboard.MainActivity;
import com.bitvault.appstore.databinding.FragmentMyappBinding;

/**
 * Created by anuj on 8/5/17.
 * version 1.0.0
 * List of all install apps
 */

public class MyAppFragment extends Fragment {

    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        FragmentMyappBinding fragmentMyappBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_myapp, container, false);
        View view = fragmentMyappBinding.getRoot();

        activity = (MainActivity) getActivity();
        if (activity != null) {

            setupViewPager(fragmentMyappBinding.idViewpager);

            fragmentMyappBinding.idTabs.setupWithViewPager(fragmentMyappBinding.idViewpager);

            fragmentMyappBinding.idMenuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.getmFragmentDrawer().openDrawer();
                }
            });
        }

        fragmentMyappBinding.searchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).displaySearchBox();
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        activity.hideMainSearchView();
    }

    /**
     * initialize view pager
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        MyAppViewPagerAdapter myAppViewPagerAdapter = new MyAppViewPagerAdapter(getChildFragmentManager());
        myAppViewPagerAdapter.addFragment(new MyAllAppListFragment(), activity.getResources().getString(R.string.updates));
        myAppViewPagerAdapter.addFragment(new AllAppListFragment(), activity.getResources().getString(R.string.installed));
        viewPager.setAdapter(myAppViewPagerAdapter);

    }
}
