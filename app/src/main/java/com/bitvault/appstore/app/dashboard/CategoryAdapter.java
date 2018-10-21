package com.bitvault.appstore.app.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitvault.appstore.R;
import com.bitvault.appstore.app.category.CategoryFragment;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.response.HomePageAppsResponse;
import com.bitvault.appstore.webservice.response.HomePageBannerCategoryResponse;

import java.util.List;

/*
 * Created Dheeraj Bansal root on 2/5/17.
 * version 1.0.0
 */

/**
 * row category name on dashboard
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Activity activity;
    private List<HomePageBannerCategoryResponse.Category> mDrawerItemList;

    public CategoryAdapter(Activity activity, List<HomePageBannerCategoryResponse.Category> mDrawerItemList) {
        this.mDrawerItemList = mDrawerItemList;
        this.activity = activity;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_category, parent, false);
        return new CategoryAdapter.CategoryViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int listPosition) {

        final HomePageBannerCategoryResponse.Category item = mDrawerItemList.get(listPosition);
        if(item!=null)
        {
            holder.mTextViewCategory.setText(item.getCategoryType());
            holder.categoryLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putString(activity.getString(R.string.header_title), item.getCategoryType());
                    bundle.putString(activity.getString(R.string.categoryIdList), String.valueOf(item.getAppCategoryId()));
                    ((MainActivity)activity).replaceFragment(new CategoryFragment(), bundle, Utils.TAG_CATEGORY_FRAGMENT, true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDrawerItemList.size();
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewCategory;
        private LinearLayout categoryLayout;



        public CategoryViewHolder(View itemView) {
            super(itemView);

            this.mTextViewCategory = (TextView) itemView.findViewById(R.id.id_app_category);
            this.categoryLayout = (LinearLayout) itemView.findViewById(R.id.id_home_category_lout);

        }

    }
}
