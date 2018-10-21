package com.bitvault.appstore.app.dashboard;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitvault.appstore.R;
import com.bitvault.appstore.custom.FontTextView;

import java.util.List;

/*
 * Created by Dheeraj Bansal on 13/2/17.
 * version 1.0.0
 */

/**
 * drawer item adapter on slide menu
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    private FragmentDrawer fragment;
    private List<DrawerItem> mDrawerItemList;

    public DrawerAdapter(FragmentDrawer fragment, List<DrawerItem> mDrawerItemList) {
        this.mDrawerItemList = mDrawerItemList;
        this.fragment = fragment;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer, parent, false);
        return new DrawerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final DrawerViewHolder holder, final int listPosition) {

        DrawerItem item = mDrawerItemList.get(listPosition);

        holder.setName(item.getName());
        holder.setIcon(item.getIcon());
        if (fragment.getSelectIndex() == listPosition) {
            holder.mTextViewIcon.setTextColor(ContextCompat.getColor(fragment.getContext(),
                    R.color.colorPrimary));
            holder.mTextViewName.setTextColor(ContextCompat.getColor(fragment.getContext(),
                    R.color.colorPrimary));
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(fragment.getContext(),
                    R.color.drawer_item_bg));
        } else {
            holder.mTextViewIcon.setTextColor(ContextCompat.getColor(fragment.getContext(),
                    R.color.color_black));
            holder.mTextViewName.setTextColor(ContextCompat.getColor(fragment.getContext(),
                    R.color.color_black));
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(fragment.getContext(),
                    R.color.color_white));
        }

        if (listPosition == 1) {
            //only one item contain space from left so I do
            holder.mTextViewIcon.setPadding((int)
                    fragment.getContext().getResources().getDimension(R.dimen.dimen20), 0, 0, 0);
        }
        if (listPosition==0 ||listPosition == mDrawerItemList.size() - 1) {
            if(listPosition == mDrawerItemList.size() - 1) {
                holder.mTextViewIcon.setVisibility(View.GONE);
            }
            holder.lineView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mDrawerItemList.size();
    }


    public class DrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FontTextView mTextViewIcon;
        private TextView mTextViewName;
        private View lineView;

        private LinearLayout linearLayout;


        public DrawerViewHolder(View itemView) {
            super(itemView);

            this.mTextViewIcon = (FontTextView) itemView.findViewById(R.id.id_drawer_icon);
            this.mTextViewName = (TextView) itemView.findViewById(R.id.id_drawer_name);
            this.lineView = itemView.findViewById(R.id.id_drawer_line_view);
            this.linearLayout = (LinearLayout) itemView.findViewById(R.id.id_drawer_lout);

        }

        /**
         * set name
         * @param name
         */
        private void setName(String name) {
            mTextViewName.setText(name);
        }

        /**
         * set icon
         * @param icon
         */
        private void setIcon(String icon) {
            mTextViewIcon.setText(icon);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.id_drawer_lout:
                    fragment.setSelectIndex(getAdapterPosition());
                    fragment.closeDrawer();
                    notifyDataSetChanged();
                    break;
            }
        }
    }
}
