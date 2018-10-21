package com.bitvault.appstore.app.searchapp;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bitvault.appstore.R;
import com.bitvault.appstore.webservice.response.AppList;
import com.bitvault.appstore.webservice.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Dheeraj Bansal on 3/5/2017.
 * version 1.0.0
 */


/**
 * search item adpter in search view list
 */
public class SearchItemAdapter extends BaseAdapter {

    private List<AppList> mList;

    private LayoutInflater inflater;
    private ImageLoader mImageLoader;
    private Context context;

    public SearchItemAdapter(Context context, List<AppList> mList, ImageLoader mImageLoader) {

        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList = mList;
        this.mImageLoader = mImageLoader;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_search, null);
            holder = new ViewHolder();
            holder.mImageView = (NetworkImageView) view.findViewById(R.id.id_search_app_icon);
            holder.mTextViewName = (TextView) view.findViewById(R.id.id_search_app_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AppList mApp = mList.get(position);
        if (mApp != null) {
            if (mApp.getAppIconUrl() != null) {
                holder.setImage(mApp.getAppIconUrl());
            } else {
                holder.mImageView.setDefaultImageResId(R.drawable.search);
                try {
                    if (context != null) {
                        holder.mImageView.setPadding((int) context.getResources().getDimension(R.dimen.dimen10), (int) context.getResources().getDimension(R.dimen.dimen10)
                                , (int) context.getResources().getDimension(R.dimen.dimen10), (int) context.getResources().getDimension(R.dimen.dimen10));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mApp.getAppName() != null) {
                holder.setName(mApp.getAppName());
            }
        }
        return view;
    }

    /**
     * clear list
     */
    public void clear() {
        if (mList != null) {
            mList.clear();
        }
    }

    /**
     * update list
     *
     * @param mList
     */
    public void addAll(List<AppList> mList) {
        if (this.mList == null) {
            this.mList = new ArrayList<>();
        }
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView mTextViewName;
        private NetworkImageView mImageView;

        private void setName(String name) {
            mTextViewName.setText(Html.fromHtml(name));
        }

        private void setImage(String url) {
            mImageView.setImageUrl(url, mImageLoader);
        }
    }

}