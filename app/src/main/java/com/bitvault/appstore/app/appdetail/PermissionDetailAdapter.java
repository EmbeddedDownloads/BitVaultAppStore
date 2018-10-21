package com.bitvault.appstore.app.appdetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bitvault.appstore.R;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.response.AppDetailResponse;
import com.bitvault.appstore.webservice.response.AppPermissions;

import java.util.List;

/*
 * Created Dheeraj Bansal root on 11/5/17.
 * version 1.0.0
 */

/**
 * row of permission detail on detail page at bottom
 */
public class PermissionDetailAdapter extends RecyclerView.Adapter<PermissionDetailAdapter.PermissionViewHolder> {

    private Context mContext;

    private List<AppPermissions.Permission> permissionList;

    public PermissionDetailAdapter(List<AppPermissions.Permission> permissionList, Context mContext) {
        this.permissionList = permissionList;
        this.mContext = mContext;
    }


    @Override
    public PermissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_permission_detail, parent, false);
        return new PermissionViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final PermissionViewHolder holder, final int listPosition) {

        AppPermissions.Permission item = permissionList.get(listPosition);
        holder.setIcon(item.getGroup_permission());
        holder.setPermissionName(item.getName());
        holder.setPermissionDetail("\u2022 "+item.getDetail().replace("\n","\n\u2022"));

    }

    @Override
    public int getItemCount() {
        return permissionList.size();
    }


    public class PermissionViewHolder extends RecyclerView.ViewHolder {

        private ImageView permissionIcon;
        private TextView permissionName;
        private TextView permissionDetail;


        public PermissionViewHolder(View itemView) {
            super(itemView);
            this.permissionIcon = (ImageView) itemView.findViewById(R.id.id_permission_icon);
            this.permissionName = (TextView) itemView.findViewById(R.id.id_permission_name);
            this.permissionDetail = (TextView) itemView.findViewById(R.id.id_permission_detail);

        }

        /**
         * set permission name
         * @param name
         */
        private void setPermissionName(String name) {
            if(name!=null) {
                permissionName.setText(name);
            }
        }

        /**
         * set permission detail
         * @param detail
         */
        private void setPermissionDetail(String detail) {
            if(detail!=null) {
                permissionDetail.setText(detail);
            }
        }


        /**
         * set app icon
         * @param groupPermission
         */
        private void setIcon(String groupPermission) {
            if(groupPermission != null && groupPermission.length() > 1) {
                permissionIcon.setImageDrawable(Utils.getPermissionDrawable(mContext, groupPermission));
            }
        }


    }
}